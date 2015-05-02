package com.lostshard.lostshard.Handlers;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.inventory.ItemStack;

public class DamageHandler {

	public static void damage(EntityDamageByEntityEvent event) {

		double weapon = 1d;

		final Entity attacker = event.getDamager();

		if (attacker instanceof Arrow) {
			if (((Arrow) attacker).getShooter() instanceof Player)
				weapon = arrow;
		} else if (attacker instanceof Player) {
			final Player aPlayer = (Player) attacker;
			final Material wep = aPlayer.getItemInHand().getType();
			if (wep == Material.AIR)
				weapon = hand;
			else if (wep == Material.DIAMOND_SWORD)
				weapon = diamondSword * swords;
			else if (wep == Material.IRON_SWORD)
				weapon = ironSword * swords;
			else if (wep == Material.GOLD_SWORD) {
				weapon = goldSword * swords;
				event.getEntity().getLocation().getWorld()
						.strikeLightningEffect(event.getEntity().getLocation());
			} else if (wep == Material.STONE_SWORD)
				weapon = stoneSword * swords;
			else if (wep == Material.WOOD_SWORD)
				weapon = woodSword * swords;
		}

		if (event.getEntity() instanceof Player) {
			if (event.isApplicable(DamageModifier.BASE))
				event.setDamage(DamageModifier.BASE,
						event.getDamage(DamageModifier.BASE) * base * weapon);
			if (event.isApplicable(DamageModifier.BLOCKING))
				event.setDamage(DamageModifier.BLOCKING, 0);
			if (event.isApplicable(DamageModifier.ARMOR))
				event.setDamage(DamageModifier.ARMOR,
						event.getDamage(DamageModifier.ARMOR) * armor);
			if (event.isApplicable(DamageModifier.MAGIC))
				event.setDamage(DamageModifier.MAGIC,
						event.getDamage(DamageModifier.MAGIC) * magic);
			if (event.isApplicable(DamageModifier.RESISTANCE))
				event.setDamage(DamageModifier.RESISTANCE,
						event.getDamage(DamageModifier.RESISTANCE) * resistance);
			if (event.isApplicable(DamageModifier.HARD_HAT))
				event.setDamage(DamageModifier.HARD_HAT,
						event.getDamage(DamageModifier.HARD_HAT) * hardhat);

		}
	}

	public static void goldArmor(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			final Player player = (Player) event.getEntity();
			final ItemStack helmet = player.getInventory().getHelmet();
			final ItemStack chest = player.getInventory().getChestplate();
			final ItemStack legs = player.getInventory().getLeggings();
			final ItemStack boots = player.getInventory().getBoots();
			if (event.getCause().equals(DamageCause.DROWNING)) {
				if (helmet != null
						&& helmet.getType().equals(Material.GOLD_HELMET))
					event.setCancelled(true);
			} else if (event.getCause().equals(DamageCause.ENTITY_EXPLOSION)) {
				if (chest != null
						&& chest.getType().equals(Material.GOLD_CHESTPLATE))
					event.setCancelled(true);
			} else if (event.getCause().equals(DamageCause.LAVA)
					|| event.getCause().equals(DamageCause.FIRE)
					|| event.getCause().equals(DamageCause.FIRE_TICK)) {
				if (legs != null
						&& legs.getType().equals(Material.GOLD_LEGGINGS))
					event.setCancelled(true);
			} else if (event.getCause().equals(DamageCause.FALL))
				if (boots != null
						&& boots.getType().equals(Material.GOLD_BOOTS))
					event.setCancelled(true);
		}
	}

	public static double base = .9d;
	public static double armor = .7d;
	public static double magic = 1d;

	public static double resistance = 1d;
	public static double hardhat = 1d;
	public static double arrow = 1d;
	public static double hand = 1d;
	public static double swords = 1d;
	public static double diamondSword = 1d;
	public static double ironSword = 1d;
	public static double goldSword = 2d;

	public static double stoneSword = 1d;

	public static double woodSword = 1d;

}
