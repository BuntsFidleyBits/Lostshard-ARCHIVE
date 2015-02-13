package com.lostshard.lostshard.Listener;

import java.util.Iterator;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PotionSplashEvent;

import com.lostshard.lostshard.Handlers.DamageHandler;
import com.lostshard.lostshard.Handlers.DeathHandler;
import com.lostshard.lostshard.Handlers.PVPHandler;
import com.lostshard.lostshard.Handlers.PlotProtectionHandler;
import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Skills.BladesSkill;
import com.lostshard.lostshard.Skills.BrawlingSkill;
import com.lostshard.lostshard.Skills.LumberjackingSkill;

public class EntityListener implements Listener {

	public EntityListener(Lostshard plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamaged(EntityDamageEvent event) {
		if(event.getEntity().getLocation().subtract(0, 1, 0).getBlock().getType() == Material.WOOL)
			event.setCancelled(true);
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPotion(PotionSplashEvent event) {
		if(event.getEntity().getShooter() instanceof Player) {
			for(LivingEntity e : event.getAffectedEntities()) {
				if(!PVPHandler.canEntityAttackEntity(event.getEntity(), e));
					event.setIntensity(e, 0d);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDamagedByEntityLow(EntityDamageByEntityEvent event) {
		Entity attacker = event.getDamager();
		Entity defender = event.getEntity();
		if (defender.hasMetadata("NPC"))
			return;
		event.setCancelled(!PVPHandler.canEntityAttackEntity(attacker, defender));
		PVPHandler.Attack(event);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamageByEntityHigh(EntityDamageByEntityEvent event) {
		DamageHandler.damage(event);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void monitorEntityAttackEntity(EntityDamageByEntityEvent event) {
		BladesSkill.playerDamagedEntityWithSword(event);
		LumberjackingSkill.playerDamagedEntityWithAxe(event);
		BrawlingSkill.playerDamagedEntityWithMisc(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamagedByEntityHighest(EntityDamageByEntityEvent event) {
		Entity defender = event.getEntity();
		if (defender.hasMetadata("NPC"))
			return;
		PlotProtectionHandler.onEntityDamageByEntity(event);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityExplodeEvent(EntityExplodeEvent event) {
		PlotProtectionHandler.onBlockExplode(event);
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		if (event.getEntity().hasMetadata("NPC"))
			return;
		DeathHandler.handleDeath(event);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityChangeBlock(EntityChangeBlockEvent event) {
		PlotProtectionHandler.onEntityChangeBlock(event);
	}
	
}
