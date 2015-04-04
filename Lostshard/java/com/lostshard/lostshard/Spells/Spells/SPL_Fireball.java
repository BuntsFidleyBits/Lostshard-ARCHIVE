package com.lostshard.lostshard.Spells.Spells;

import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.lostshard.lostshard.Spells.Scroll;
import com.lostshard.lostshard.Spells.Spell;

public class SPL_Fireball extends Spell {

	public SPL_Fireball(Scroll scroll) {
		super(scroll);
	}

	@Override
	public boolean verifyCastable(Player player) {
		return true;
	}

	@Override
	public void preAction(Player player) {
		
	}

	@Override
	public void doAction(Player player) {
		Vector v = player.getLocation().getDirection();
		
		Fireball fb = player.getWorld().spawn(player.getEyeLocation(), Fireball.class);
		fb.setShooter(player);
		fb.setVelocity(v);
	}

}
