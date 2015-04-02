package com.lostshard.lostshard.Spells.Spells;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Objects.Groups.Clan;
import com.lostshard.lostshard.Spells.Spell;
import com.lostshard.lostshard.Utils.Output;

public class SPL_ClanTeleport extends Spell {

	public SPL_ClanTeleport() {
		setName("Clan Teleport");
		setSpellWords("Arg Matius Teleportus");
		setCastingDelay(20);
		setCooldown(20);
		setManaCost(50);
		addReagentCost(new ItemStack(Material.FEATHER));
		addReagentCost(new ItemStack(Material.REDSTONE));
		setMinMagery(720);
		setPage(7);
		setPrompt("Who would you like to toeleport to?");
	}

	@Override
	public boolean verifyCastable(Player player) {
		return true;
	}

	@Override
	public void preAction(Player player) {
		Output.positiveMessage(player, "You begin casting Clan Teleport");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void doAction(Player player) {
		//System.out.println("RSPNS: "+_response);
		PseudoPlayer pseudoPlayer = pm.getPlayer(player);
		Player targetPlayer = Bukkit.getPlayer(getResponse());
		
		if(targetPlayer == null) {
			Output.simpleError(player, "Player not found.");
			return;
		}
		else {			
			PseudoPlayer targetPseudoPlayer = pm.getPlayer(targetPlayer);
			Clan playerClan = pseudoPlayer.getClan();
			Clan targetPlayerClan = targetPseudoPlayer.getClan();
			if((playerClan != null) && (targetPlayerClan != null) && playerClan.getName().endsWith(targetPlayerClan.getName())) {
				if(targetPseudoPlayer.isPrivate()) {
					Output.positiveMessage(player, "Can't teleport to that player, they are set to private.");
					return;
				}
				
				if(targetPlayer.getHealth() <= 0) {
					Output.positiveMessage(player, "You cannot teleport to that player, as they are dead.");
					return;
				}
				
				//check for lapis below you
				if(player.getLocation().getBlock().getRelative(0,-1,0).getType().equals(Material.LAPIS_BLOCK) ||
				   player.getLocation().getBlock().getRelative(0,-2,0).getType().equals(Material.LAPIS_BLOCK)){
					Output.simpleError(player, "Cannot teleport from a Lapis Lazuli block.");
					return;
				}
				
				//check for lapis below your target location
				if(targetPlayer.getLocation().getBlock().getRelative(0,-1,0).getType().equals(Material.LAPIS_BLOCK) ||
						targetPlayer.getLocation().getBlock().getRelative(0,-2,0).getType().equals(Material.LAPIS_BLOCK)){
					Output.simpleError(player, "Cannot teleport to a Lapis Lazuli block.");
					return;
				}
				
				player.getWorld().strikeLightningEffect(player.getLocation());
				
				player.teleport(targetPlayer.getLocation());
				
				player.getWorld().strikeLightningEffect(player.getLocation());
				
				pseudoPlayer.setMana(0);
				pseudoPlayer.setStamina(0);
				Output.sendEffectTextNearby(player, "You hear a loud crack and the fizzle of electricity.");
				player.sendMessage(ChatColor.GRAY+"Teleporting without a rune has exausted you.");
			}
			else {
				Output.simpleError(player, "That player is not in your clan.");
				return;
			}
		}
	}

}
