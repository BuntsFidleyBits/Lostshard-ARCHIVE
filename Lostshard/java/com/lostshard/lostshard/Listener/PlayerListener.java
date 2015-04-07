package com.lostshard.lostshard.Listener;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
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
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Database.Database;
import com.lostshard.lostshard.Handlers.ChatHandler;
import com.lostshard.lostshard.Handlers.DeathHandler;
import com.lostshard.lostshard.Handlers.EnderdragonHandler;
import com.lostshard.lostshard.Handlers.PlotProtectionHandler;
import com.lostshard.lostshard.Handlers.foodHealHandler;
import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Manager.PlotManager;
import com.lostshard.lostshard.Manager.SpellManager;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Objects.PseudoScoreboard;
import com.lostshard.lostshard.Objects.Plot.Capturepoint;
import com.lostshard.lostshard.Objects.Plot.Plot;
import com.lostshard.lostshard.Skills.BlackSmithySkill;
import com.lostshard.lostshard.Skills.FishingSkill;
import com.lostshard.lostshard.Skills.SurvivalismSkill;
import com.lostshard.lostshard.Skills.TamingSkill;
import com.lostshard.lostshard.Spells.Structures.FireWalk;
import com.lostshard.lostshard.Spells.Structures.Gate;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.Utils;

public class PlayerListener implements Listener {

	PlayerManager pm = PlayerManager.getManager();
	PlotManager ptm = PlotManager.getManager();
	
	public PlayerListener(Lostshard plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onPlayerPortal(PlayerPortalEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerBedEnter(PlayerBedEnterEvent event) {
		PlotProtectionHandler.onPlayerBedEnter(event);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerFishEvent(PlayerFishEvent event) {
		FishingSkill.onFish(event);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent event) {
		EnderdragonHandler.respawnDragonCheck(event);
	}

	@EventHandler
	public void onPlayeQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		PseudoPlayer pPlayer = pm.getPlayer(player);
		if(pPlayer.getPvpTicks() > 0) {
			// Drop inventory
        	for(ItemStack itemStack : player.getInventory().getContents()) {
        		if(itemStack == null)
        			continue;
        		player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
        	}
        	// Drop armor
        	for(ItemStack itemStack : player.getInventory().getArmorContents()) {
        		if(itemStack == null || itemStack.getType().equals(Material.AIR))
        			continue;
        		player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
        	}
        	
        	//clear the inventory
        	player.getInventory().setHelmet(new ItemStack(Material.AIR, 0));
        	player.getInventory().setChestplate(new ItemStack(Material.AIR, 0));
        	player.getInventory().setLeggings(new ItemStack(Material.AIR, 0));
        	player.getInventory().setBoots(new ItemStack(Material.AIR, 0));
        	player.getInventory().clear();

			Bukkit.broadcastMessage(ChatColor.RED+player.getName()+" left the game while in combat.");
			event.setQuitMessage(null);
		}
		pm.onPlayerQuit(event);
		TamingSkill.onLave(event);
		for(Plot plot : ptm.getPlots())
			plot.failCaptureLeft(player);
	}
	
	@EventHandler
	public void onPlayerSpawn(PlayerRespawnEvent event) {
		PlotProtectionHandler.onPlayerSpawn(event);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		PlotProtectionHandler.onButtonPush(event);
		PlotProtectionHandler.onPlayerInteract(event);
		Gate.onPlayerInteractEvent(event);
		BlackSmithySkill.anvilProtect(event);
		SurvivalismSkill.onHoe(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		ChatHandler.onPlayerChat(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerLogin(PlayerLoginEvent event) {
		if(Lostshard.isMysqlError()) {
			event.setKickMessage(ChatColor.RED+"Something is wrong. We are working on it.");
			event.setResult(Result.KICK_OTHER);
			return;
		}
	}
	
	@EventHandler
	public void onPlayerLoginMonitor(PlayerLoginEvent event) {
		pm.onPlayerLogin(event);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		Output.displayLoginMessages(player);
		PseudoPlayer pPlayer = pm.getPlayer(player);
		pPlayer.setScoreboard(new PseudoScoreboard(player.getUniqueId()));
		PlotProtectionHandler.onPlayerJoin(event);
		List<String> msgs = Database.getOfflineMessages(player.getUniqueId());
		for(String msg : msgs)
			player.sendMessage(ChatColor.BLUE+msg);
		event.setJoinMessage(null);
		for(Player p : Bukkit.getOnlinePlayers())
			if(p != player)
				p.sendMessage(ChatColor.YELLOW+player.getName()+" joined the game");
		Database.deleteMessages(player.getUniqueId());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerMove(PlayerMoveEvent event) {
		PlotProtectionHandler.onPlotEnter(event);
		SpellManager.move(event);
		Gate.onPlayerMove(event);
		FireWalk.onPlayerMove(event);
		
		Player player = event.getPlayer();
		Plot plot = ptm.findPlotAt(player.getLocation());
		if(plot != null) {
		Capturepoint cp = Capturepoint.getByName(plot.getName());
		    for(Plot cP : ptm.getPlots()) {
				if(cp == null && !Utils.isWithin(player.getLocation(), plot.getLocation(), 10) || cp != null && !Utils.isWithin(player.getLocation(), new Location(plot.getLocation().getWorld(), cp.getPoint().x, cp.getPoint().y, cp.getPoint().z), 10)) {
	    			cP.failCaptureLeft(player);
	    		}
		    }
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerBuckitFill(PlayerBucketFillEvent event) {
		PlotProtectionHandler.onBuckitFill(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerBuckitEmpty(PlayerBucketEmptyEvent event) {
		PlotProtectionHandler.onBuckitEmpty(event);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		PseudoPlayer pPlayer = pm.getPlayer(player);
		if(event.getInventory().getTitle().equals(pPlayer.getBank().getInventory().getTitle()))
			pPlayer.update();
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerInteract(PlayerInteractEvent event) {
		foodHealHandler.foodHeal(event);
		SpellManager.onPlayerInteract(event);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDeath(PlayerDeathEvent event) {
		DeathHandler.handleDeath(event);
		for(Plot plot : ptm.getPlots())
			plot.failCaptureDied(event.getEntity());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
		PlotProtectionHandler.onPlayerInteractEntity(event);
	}
	
	@EventHandler
	public void onPrepareItemEnchant(PrepareItemEnchantEvent event) {
		BlackSmithySkill.Enchanting(event);
	}
}
