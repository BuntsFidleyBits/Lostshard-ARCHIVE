package com.lostshard.lostshard.Skills;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;

import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Utils.Output;

public class ArcherySkill extends Skill {

	public ArcherySkill() {
		super();
		setName("Archery");
		setBaseProb(.2);
		setScaleConstant(60);
	}
	
	public static void EntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		if(!(event.getDamager() instanceof Arrow))
			return;
		Arrow arrow = (Arrow) event.getDamager();
		if(!(arrow.getShooter() instanceof Player))
			return;
		
		Player attacker = (Player) arrow.getShooter();
		Entity entity = event.getEntity();
		
		PseudoPlayer pPlayer = pm.getPlayer(attacker);
		
		Skill skill = pPlayer.getCurrentBuild().getArchery();
		int lvl = skill.getLvl();
		
		
		double damage = 0.004*lvl;
		
		if(entity instanceof Player && lvl >= 500 && Math.random() < .25 && ((Player) entity).getHealth() > 4) {
			Player player = (Player) event.getEntity();
			double health = event.getDamage(DamageModifier.BASE)*.45+damage*.7;
			health = Math.max(health, 4);
			player.setHealth(health);
			event.setDamage(0);
			player.sendMessage(ChatColor.GREEN+"The arrow pierces through your armor!");
			attacker.sendMessage(ChatColor.GREEN+"Your arrow pierced through "+player.getName()+"'s armor.");
		} else {
			if(event.isApplicable(DamageModifier.BASE))
				event.setDamage(DamageModifier.BASE, event.getDamage(DamageModifier.BASE)+damage);
		}
		
		if(entity instanceof Monster || entity instanceof Player || entity instanceof Slime)
			skill.setBaseProb(.5);
		else
			skill.setBaseProb(.2);
		
		int gain = skill.skillGain();
		Output.gainSkill(attacker, "archery", gain, skill.getLvl());
		pPlayer.update();
	}
	
}
