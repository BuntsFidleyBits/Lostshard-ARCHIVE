package com.lostshard.Lostshard.Listener;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.lostshard.Lostshard.Database.Mappers.MessagesMapper;
import com.lostshard.Lostshard.Handlers.CapturepointHandler;
import com.lostshard.Lostshard.Handlers.ChatHandler;
import com.lostshard.Lostshard.Handlers.DeathHandler;
import com.lostshard.Lostshard.Handlers.EnderdragonHandler;
import com.lostshard.Lostshard.Handlers.GuardHandler;
import com.lostshard.Lostshard.Handlers.InventoryGUIHandler;
import com.lostshard.Lostshard.Handlers.PlotProtectionHandler;
import com.lostshard.Lostshard.Handlers.foodHealHandler;
import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Lostshard.Manager.NPCManager;
import com.lostshard.Lostshard.Objects.Managers;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Objects.Player.PseudoScoreboard;
import com.lostshard.Lostshard.Objects.Plot.Plot;
import com.lostshard.Lostshard.Objects.Plot.PlotCapturePoint;
import com.lostshard.Lostshard.Skills.BlackSmithySkill;
import com.lostshard.Lostshard.Skills.FishingSkill;
import com.lostshard.Lostshard.Skills.SurvivalismSkill;
import com.lostshard.Lostshard.Skills.TamingSkill;
import com.lostshard.Lostshard.Spells.Structures.FireWalk;
import com.lostshard.Lostshard.Spells.Structures.Gate;
import com.lostshard.Lostshard.Utils.Output;
import com.lostshard.Lostshard.Utils.SpellUtils;

public class PlayerListener extends LostshardListener implements Managers {

	public PlayerListener(Lostshard plugin) {
		super(plugin);
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		InventoryGUIHandler.onInventoryClick(event);
	}

	@EventHandler
	public void onInventoryClick(InventoryCloseEvent event) {
		InventoryGUIHandler.onInventoryClose(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClose(InventoryCloseEvent event) {
		final Player player = (Player) event.getPlayer();
		final PseudoPlayer pPlayer = pm.getPlayer(player);
		if (event.getInventory().getTitle()
				.equals(pPlayer.getBank().getInventory().getTitle()))
			pPlayer.update();
	}

	@EventHandler
	public void onPlayeQuit(PlayerQuitEvent event) {
		final Player player = event.getPlayer();
		final PseudoPlayer pPlayer = pm.getPlayer(player);
		if (pPlayer.getPvpTicks() > 0) {
			// Drop inventory
			for (final ItemStack itemStack : player.getInventory()
					.getContents()) {
				if (itemStack == null)
					continue;
				player.getWorld().dropItemNaturally(player.getLocation(),
						itemStack);
			}
			// Drop armor
			for (final ItemStack itemStack : player.getInventory()
					.getArmorContents()) {
				if (itemStack == null
						|| itemStack.getType().equals(Material.AIR))
					continue;
				player.getWorld().dropItemNaturally(player.getLocation(),
						itemStack);
			}

			// clear the inventory
			player.getInventory().setHelmet(new ItemStack(Material.AIR, 0));
			player.getInventory().setChestplate(new ItemStack(Material.AIR, 0));
			player.getInventory().setLeggings(new ItemStack(Material.AIR, 0));
			player.getInventory().setBoots(new ItemStack(Material.AIR, 0));
			player.getInventory().clear();

			Bukkit.broadcastMessage(ChatColor.RED + player.getName()
					+ " left the game while in combat.");
			event.setQuitMessage(null);
		}
		pm.onPlayerQuit(event);
		TamingSkill.onLave(event);
		CapturepointHandler.onPlayerQuit(event);
	}

	@EventHandler
	public void onPlayerBedEnter(PlayerBedEnterEvent event) {
		PlotProtectionHandler.onPlayerBedEnter(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerBuckitEmpty(PlayerBucketEmptyEvent event) {
		PlotProtectionHandler.onBuckitEmpty(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerBuckitFill(PlayerBucketFillEvent event) {
		PlotProtectionHandler.onBuckitFill(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent event) {
		EnderdragonHandler.respawnDragonCheck(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		ChatHandler.onPlayerChat(event);
		if(StringUtils.containsIgnoreCase(event.getMessage(), "guard"))
			GuardHandler.Guard(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDeath(PlayerDeathEvent event) {
		DeathHandler.handleDeath(event);
		for (final Plot plot : ptm.getPlots())
			if (plot instanceof PlotCapturePoint)
				((PlotCapturePoint) plot).failCaptureDied(event.getEntity());
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerFishEvent(PlayerFishEvent event) {
		FishingSkill.onFish(event);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerInteract(PlayerInteractEvent event) {
		foodHealHandler.foodHeal(event);
		sm.onPlayerInteract(event);
		
		Player p = event.getPlayer();
		Block b = event.getClickedBlock();
		Action a = event.getAction();
		if(a.equals(Action.RIGHT_CLICK_BLOCK) && b != null && b.getState() instanceof InventoryHolder) {
			Block block = SpellUtils.blockInLOS(p, 6);
			if(block != null && !(block.getState() instanceof InventoryHolder))
				event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		NPCManager.getManager().interac(event);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
		PlotProtectionHandler.onPlayerInteractEntity(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		PlotProtectionHandler.onButtonPush(event);
		PlotProtectionHandler.onPlayerInteract(event);
		Gate.onPlayerInteractEvent(event);
		BlackSmithySkill.anvilProtect(event);
		SurvivalismSkill.onHoe(event);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) {
		final Player player = event.getPlayer();
		Output.displayLoginMessages(player);
		final PseudoPlayer pPlayer = pm.onPlayerLogin(event);
		pPlayer.setScoreboard(new PseudoScoreboard(player.getUniqueId()));
		PlotProtectionHandler.onPlayerJoin(event);
		final List<String> msgs = MessagesMapper.getOfflineMessages(player
				.getUniqueId());
		for (final String msg : msgs)
			player.sendMessage(ChatColor.BLUE + msg);
		event.setJoinMessage(null);
		for (final Player p : Bukkit.getOnlinePlayers())
			if (p != player)
				p.sendMessage(ChatColor.YELLOW + player.getName()
						+ " joined the game");
		if(!msgs.isEmpty())
			MessagesMapper.deleteMessages(player.getUniqueId());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerLogin(PlayerLoginEvent event) {
		if (getPlugin().isMysqlError()) {
			event.setKickMessage(ChatColor.RED
					+ "Something is wrong. We are working on it.");
			event.setResult(Result.KICK_OTHER);
			return;
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerMove(PlayerMoveEvent event) {
		PlotProtectionHandler.onPlotEnter(event);
		onMoveBlock(event);
		Gate.onPlayerMove(event);
		FireWalk.onPlayerMove(event);
		CapturepointHandler.onPlayerMove(event);
		GuardHandler.move(event);
	}

	private void onMoveBlock(PlayerMoveEvent event) {
		if (event.getTo() != null
				&& event.getFrom() != null
				&& !event.getTo().getBlock().getState()
				.equals(event.getFrom().getBlock().getState())) {
			sm.move(event);
			restAndMeditate(event);
		}
	}

	private void restAndMeditate(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		PseudoPlayer pPlayer = pm.getPlayer(player);
		if(pPlayer.isResting() && pPlayer.isMeditating()) {
			pPlayer.setMeditating(false);
			pPlayer.setResting(false);
			Output.simpleError(player, "You have moved and stopped resting and meditating.");
		}else if(pPlayer.isResting()) {
			pPlayer.setResting(false);
			Output.simpleError(player, "You have moved and stopped resting.");
		}else if(pPlayer.isMeditating()) {
			pPlayer.setMeditating(false);
			Output.simpleError(player, "You have moved and stopped meditating.");
		}
	}

	@EventHandler
	public void onPlayerPortal(PlayerPortalEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onPlayerSpawn(PlayerRespawnEvent event) {
		PlotProtectionHandler.onPlayerSpawn(event);
	}

	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		PlotProtectionHandler.onPlayerTeleport(event);
	}

	@EventHandler
	public void onPrepareItemEnchant(PrepareItemEnchantEvent event) {
		BlackSmithySkill.Enchanting(event);
	}
}
