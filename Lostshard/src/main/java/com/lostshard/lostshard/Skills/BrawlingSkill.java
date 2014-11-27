package com.lostshard.lostshard.Skills;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.lostshard.lostshard.Handlers.PseudoPlayerHandler;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Utils.ItemUtils;
import com.lostshard.lostshard.Utils.Output;

public class BrawlingSkill extends Skill {

	public BrawlingSkill() {
		super();
		setName("Brawling");
		setBaseProb(.2);
		setScaleConstant(60);
	}
	
	public static void playerDamagedEntityWithMisc(EntityDamageByEntityEvent event) {
		if(event.isCancelled())
			return;
		if(!(event.getDamager() instanceof Player))
			return;
		if(!(event.getEntity() instanceof LivingEntity))
			return;
		Player player = (Player) event.getDamager();
		Material item = player.getItemInHand().getType();
		if(ItemUtils.isSword(item) || ItemUtils.isAxe(item))
			return;
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPlayer(player);
		Skill skill = pseudoPlayer.getCurrentBuild().getBrawling();
		Entity damagedEntity = event.getEntity();
		int brawlingSkill = skill.getLvl();
		double damage = event.getDamage();
		int additionalDamage = 0;
		
		if(brawlingSkill >= 1000)
			additionalDamage = 4;
		else if(brawlingSkill >= 750)
			additionalDamage = 3;
		else if(brawlingSkill >= 500)
			additionalDamage = 2;
		else if(brawlingSkill >= 250)
				additionalDamage = 1;
		
		/*double chanceOfEffect = (double)brawlingSkill / 1000;
		double stunChance = chanceOfEffect * .25;
		
		double rand = Math.random();
		//System.out.println(rand);
		if(stunChance > rand) {
			//stun
			if(damagedEntity instanceof Player) {
				Player defenderPlayer = (Player)damagedEntity;
				PseudoPlayer defenderPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(defenderPlayer.getName());
				if(defenderPseudoPlayer._stunTicks <= 0) {
					defenderPseudoPlayer._stunTicks = 30;
					defenderPlayer.sendMessage(ChatColor.GREEN+"You have been stunned!");
					player.sendMessage(ChatColor.GREEN+"You stunned "+defenderPlayer.getName()+"!");
				}
			}
		}
		
		if(damagedEntity instanceof Player) {
			if(brawlingSkill >= 1000) {
				Player defenderPlayer = (Player)damagedEntity;
				PseudoPlayer defenderPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(defenderPlayer.getName());
				if(defenderPseudoPlayer._stunTicks <= 0)
					defenderPseudoPlayer._stunTicks = 17;
			}
		}*/
		
		damage += additionalDamage;
		event.setDamage(damage);
		
		if(damagedEntity instanceof Monster || damagedEntity instanceof Player || damagedEntity instanceof Slime)
			skill.setBaseProb(.5);
		else
			skill.setBaseProb(.2);
		int gain = skill.skillGain();
		Output.gainSkill(player, "Brawling", gain, skill.getLvl());
	}
	
}
