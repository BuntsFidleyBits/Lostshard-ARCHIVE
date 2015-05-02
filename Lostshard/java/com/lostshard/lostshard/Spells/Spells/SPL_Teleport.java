package com.lostshard.lostshard.Spells.Spells;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Spells.RangedSpell;
import com.lostshard.lostshard.Spells.Scroll;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.SpellUtils;

public class SPL_Teleport extends RangedSpell {
	
	public SPL_Teleport(Scroll scroll) {
		super(scroll);
		setRange(25);
	}
	
	/* The meat of the spell code, this is what happens when the spell is
	 * actually activated and should be doing something.
	 */
	@Override
	public void doAction(Player player) {
		Location teleportTo = new Location(player.getWorld(), getFoundBlock().getX()+.5, (double)getFoundBlock().getY()+1, getFoundBlock().getZ()+.5);
		teleportTo.setPitch(player.getLocation().getPitch());
		teleportTo.setYaw(player.getLocation().getYaw());
		player.teleport(teleportTo);
	}

	private boolean isRoom(Block block) {
		if(!SpellUtils.invisibleBlocks.contains(block.getRelative(0, 1, 0).getType()) ||
				!SpellUtils.invisibleBlocks.contains(block.getRelative(0, 2, 0).getType())) {
			return false;
		}
		return true;
	}
	
	/* Used for anything that must be handled as soon as the spell is cast,
	 * for example targeting a location for a delayed spell.
	 */
	@Override
	public void preAction(Player player) {
		
	}
	
	@Override
	public boolean verifyCastable(Player player) {
		Block blockAt = player.getLocation().getBlock();
		if(!blockAt.getType().equals(Material.IRON_DOOR_BLOCK)) {
			List<Block> lastTwoBlocks = player.getLastTwoTargetBlocks(SpellUtils.invisibleBlocks, getRange());
			
			if(lastTwoBlocks.size() < 2) {
				Output.simpleError(player, "Invalid target.");
				return false;
			}
			
			boolean ceiling = false;
			Block blockAboveFace = lastTwoBlocks.get(0).getRelative(0,1,0);
			if(!SpellUtils.invisibleBlocks.contains(blockAboveFace.getType()) && SpellUtils.invisibleBlocks.contains(blockAboveFace.getRelative(0,-1,0).getType())) {
				setFoundBlock(blockAboveFace.getRelative(0,-3,0));
				ceiling = true;
			}
			boolean wall = false;
			if(!ceiling) {
				if(!isRoom(lastTwoBlocks.get(1))) {
					wall=true;
					setFoundBlock(lastTwoBlocks.get(0));
				}
			}
			
			if(!ceiling && !wall)
				setFoundBlock(SpellUtils.blockInLOS(player, getRange()));
				
			if(getFoundBlock() == null) {
				Output.simpleError(player, "That location is too far away.");
				return false;
			}
			
			if(!SpellUtils.invisibleBlocks.contains(getFoundBlock().getRelative(0, 1, 0).getType()) &&
			   !SpellUtils.invisibleBlocks.contains(getFoundBlock().getRelative(0, 2, 0))) {
				Output.simpleError(player, "There is not enough room to teleport there.");
				return false;
			}
			
			//check for lapis
			for(int x=getFoundBlock().getX()-3; x<=getFoundBlock().getX()+3; x++) {
				for(int y=getFoundBlock().getY()-3; y<=getFoundBlock().getY()+3; y++) {
					for(int z=getFoundBlock().getZ()-3; z<=getFoundBlock().getZ()+3; z++) {
						if(getFoundBlock().getWorld().getBlockAt(x,y,z).getType().equals(Material.LAPIS_BLOCK)) {
							Output.simpleError(player, "can't teleport to a location near Lapis Lazuli blocks.");
							return false;
						}
					}
				}
			}	
			return true;
		}
		else Output.simpleError(player, "can't teleport from an iron door.");
		return false;
	}

	
}
