package com.lostshard.lostshard.Handlers;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.lostshard.lostshard.Objects.Plot;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Objects.Groups.Clan;
import com.lostshard.lostshard.Objects.Groups.Party;
import com.lostshard.lostshard.Objects.Recent.RecentAttacker;

/**
 * @author Jacob Handling entity's hitting entity's.
 */
public class PVPHandler {

	/**
	 * @param attacker
	 * @param defender
	 * @returnEntity can damage Entity. Checking if they are in clan or other
	 *         reasons if they should not be able to damage each other.
	 */
	public static boolean canEntityAttackEntity(Entity attacker, Entity defender) {
		
		/**
		 * Checking if its an NPC.
		 */
		if (defender.hasMetadata("NPC"))
			return false;

		/**
		 * Ensuring that the defender and is a Player.
		 */
		if (!(defender instanceof Player))
			return true;

		/**
		 * Handling player hitting with feather.
		 */
		if (attacker instanceof Player
				&& defender instanceof Player
				&& ((Player) attacker).getItemInHand().getType()
						.equals(Material.FEATHER)) {
			((Player) defender).sendMessage(ChatColor.GRAY
					+ ((Player)attacker).getName()+" tickled you with a feather.");
			return false;
		}

		/**
		 * Allowing potion to be thrown on all if positive.
		 */
		if (attacker instanceof ThrownPotion)
			for (PotionEffect pe : ((ThrownPotion) attacker)
					.getEffects())
				if (pe.getType().equals(PotionEffectType.HEAL)
						|| pe.getType().equals(PotionEffectType.REGENERATION)
						|| pe.getType().equals(PotionEffectType.ABSORPTION)
						|| pe.getType().equals(PotionEffectType.DAMAGE_RESISTANCE)
						|| pe.getType().equals(PotionEffectType.FAST_DIGGING)
						|| pe.getType().equals(PotionEffectType.FIRE_RESISTANCE)
						|| pe.getType().equals(PotionEffectType.HEALTH_BOOST)
						|| pe.getType().equals(PotionEffectType.INCREASE_DAMAGE)
						|| pe.getType().equals(PotionEffectType.INVISIBILITY)
						|| pe.getType().equals(PotionEffectType.SPEED)
						|| pe.getType().equals(PotionEffectType.WATER_BREATHING)
						|| pe.getType().equals(PotionEffectType.NIGHT_VISION))
					return true;

		/**
		 * Handling attacker if the damage came from an projectile and getting
		 * the shooter.
		 */
		if (attacker instanceof Projectile)
			attacker = (Entity) ((Projectile) attacker).getShooter();

		/**
		 * Ensuring that the attacker is a Player.
		 */
		if (!(attacker instanceof Player))
			return true;

		/**
		 * Checking if the defender is standing in a none PVP plot.
		 */
		Plot plotAtDefender = PlotHandler.findPlotAt(defender.getLocation());
		if (plotAtDefender != null && plotAtDefender.isAllowPvp())
			return false;

		/**
		 * Checking if the attacker is standing a none PVP plot.
		 */
		Plot plotAtAttacker = PlotHandler.findPlotAt(attacker.getLocation());
		if (plotAtAttacker != null && plotAtAttacker.isAllowPvp())
			return false;

		/**
		 * Allowing players to damage them self.
		 */
		if (attacker == defender)
			return true;
		
		PseudoPlayer pAttacker = PseudoPlayerHandler.getPlayer((Player) attacker);
		PseudoPlayer pDefender = PseudoPlayerHandler.getPlayer((Player) defender);
		
		/**
		 * Checking if they are in same clan or not.
		 */
		 if(pAttacker.getClan() != null && pDefender.getClan() != null && pAttacker.getClan() ==
		 pDefender.getClan())
			 return false;

		/**
		 * Checking if they are in same party or not.
		 */
		 if(pAttacker.getParty() != null && pDefender.getParty() != null && pAttacker.getParty() ==
		 pDefender.getParty())
			 return false;

		return true;
	}
	
	public static void criminalAction(Player player, Player playerDamager) {
		if (player.hasMetadata("NPC"))
			return;
		if(player == null || playerDamager == null)
			return;
		
		if(player.getName().equals(playerDamager.getName()))
			return;
		
		PseudoPlayer pseudoPlayerDefender = PseudoPlayerHandler.getPlayer(player);
		PseudoPlayer pseudoPlayerAttacker = PseudoPlayerHandler.getPlayer(playerDamager);
		
		//add pvp ticks
		pseudoPlayerDefender.setPvpTicks(150); // 10 seconds
		pseudoPlayerDefender.setEngageInCombatTicks(300);; // attacker attacked someone, he is engaging in combat
		
		pseudoPlayerAttacker.setPvpTicks(150);
		pseudoPlayerAttacker.setEngageInCombatTicks(300); // attacker attacked someone, he is engaging in combat
		
		// only criminal if it happens in the normal world
		if(true) {//player.getWorld().equals(RPG._normalWorld) || player.getWorld().equals(RPG._newmap)) {
			boolean notCrim = false;
			if(!player.getWorld().getEnvironment().equals(Environment.NORMAL)) {
				notCrim = true;
			}
			Plot plot = PlotHandler.findPlotAt(player.getLocation());
			// devender is on a plot
			if(plot != null) {
				if(plot.isCapturePoint())
					notCrim = true;
				// and the attacker is a member of the plot
				if(plot.isFriendOrAbove(playerDamager)) {
					// and the defender is NOT a member of the plot
					notCrim = true;
				}
			}
			
			// If the defender has attacked anyone within 30 seconds they engaged in combat willingly
			// so attacking them will not be criminal even if they are blue
			
			Party party = pseudoPlayerDefender.getParty();
			if(party != null) {
				if(party.isMember(playerDamager.getUniqueId())) {
					return;
				}
			}
			Clan clan = pseudoPlayerDefender.getClan();
			if(clan != null) {
				if(clan.isMember(playerDamager.getUniqueId())) {
					return;
				}
			}
			
			// Determine if a criminal action has taken place
			if(!pseudoPlayerDefender.isCriminal() && !notCrim) {
				// attacked non criminal, thats a criminal action
				if(!pseudoPlayerAttacker.isCriminal()) {
					playerDamager.sendMessage(ChatColor.RED+"You have committed a criminal action");
				}
				pseudoPlayerAttacker.setCriminal(3000);
				
				if(!pseudoPlayerAttacker.isMurderer()) {
					playerDamager.setDisplayName(ChatColor.GRAY+playerDamager.getName());
//					SimpleScoreBoard.updatePlayers();
				}
			}
			
			RecentAttacker recentAttacker = new RecentAttacker(playerDamager.getUniqueId(), 300);
			recentAttacker.setNotCrim(notCrim);
			pseudoPlayerDefender.addRecentAttacker(recentAttacker);
		}
	}
}
