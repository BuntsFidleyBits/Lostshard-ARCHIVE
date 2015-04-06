package com.lostshard.lostshard.Skills;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Objects.Plot.Plot;
import com.lostshard.lostshard.Utils.ItemUtils;
import com.lostshard.lostshard.Utils.Output;

public class BlackSmithySkill extends Skill {

	private static final int REPAIR_STAMINA_COST = 10;
	private static final int SMELT_STAMINA_COST = 15;
	private static final int ENHANCE_STAMINA_COST = 25;
	
	public BlackSmithySkill() {
		super();
		setName("Blacksmithy");
		setBaseProb(.2);
		setScaleConstant(30);
		setMaxGain(15);
		setMinGain(5);
	}
	
	public static void repair(Player player) {
		ItemStack item = player.getItemInHand();
		if(item == null || item.getType().equals(Material.AIR)) {
			Output.simpleError(player, "You cannot repair air.");
			return;
		}
		PseudoPlayer pPlayer = pm.getPlayer(player);
		if(pPlayer.getPvpTicks() > 0) {
			Output.simpleError(player,  "You cannot repair while in or shortly after combat.");
			return;
		}
		if(pPlayer.getStamina() < REPAIR_STAMINA_COST) {
			Output.simpleError(player, "Not enough stamina - Repair requires "+REPAIR_STAMINA_COST+".");
			return;
		}
		Skill skill = pPlayer.getCurrentBuild().getBlackSmithy();
		int lvl = skill.getLvl();
		if(!canRepair(item)) {
			Output.simpleError(player, "You cannot repair "+item.getType().name().toLowerCase().replace("_", " ")+".");
			return;
		}
		Material cost = null;
		boolean cangain = false;
		int costAmount = 1;
		if(ItemUtils.isDiamond(item)) {
			cost = Material.DIAMOND;
			cangain = true;
		}else if(ItemUtils.isGold(item)){
			cost = Material.GOLD_INGOT;
			cangain = true;
		}else if(ItemUtils.isIron(item)){
			cost = Material.IRON_INGOT;
			if(lvl < 750)
				cangain = true;
		}else if(ItemUtils.isStone(item)){
			cost = Material.COBBLESTONE;
			if(lvl < 500)
				cangain = true;
		}else if(ItemUtils.isWood(item)){
			cost = Material.WOOD;
			if(lvl < 250)
				cangain = true;
		}
		
		if(cost == null) {
			Output.simpleError(player, "You cannot smelt "+item.getType().name().toLowerCase().replace("_", " ")+".");
			return;
		}
		
		if(!player.getInventory().contains(cost, costAmount)) {
			Output.simpleError(player, "You do not have enough "+StringUtils.lowerCase(cost.name()).replace("_", " ")+" to repair that tool, requires "+costAmount+".");
			return;
		}
		
		
		double dblSkillVal = (double)skill.getLvl()/1000;
		double rand = Math.random();
		if(dblSkillVal >= rand) {
			item.setDurability((short)0);
			pPlayer.setStamina(pPlayer.getStamina()-REPAIR_STAMINA_COST);
			ItemUtils.removeItem(player.getInventory(), cost, costAmount);
			Output.positiveMessage(player, "You repair the item.");
		}
		else {
			item.setDurability((short)((int)item.getDurability()+(item.getDurability()*.5)+2));
			if(item.getDurability() < (short)item.getDurability()) {
				player.sendMessage(ChatColor.GRAY+"You failed to repair the "+item.getType().name().toLowerCase().replace("_", " ")+", it was damaged in the process.");
			}
			else {
				player.getInventory().clear(player.getInventory().getHeldItemSlot());
				player.sendMessage(ChatColor.GRAY+"You failed to repair the "+item.getType().name().toLowerCase().replace("_", " ")+", it was destroyed in the process.");
			}
			ItemUtils.removeItem(player.getInventory(), cost, costAmount);
		}
		if(cangain) {
			int gain = skill.skillGain(pPlayer);
			Output.gainSkill(player, "Blacksmithy", gain, skill.getLvl());
			if(gain > 0)
				pPlayer.update();
		}
	}
	
	public static void smelt(Player player) {
		ItemStack item = player.getItemInHand();
		if(item == null) {
			Output.simpleError(player, "You cannot smelt air.");
			return;
		}
		PseudoPlayer pPlayer = pm.getPlayer(player);
		Skill skill = pPlayer.getCurrentBuild().getBlackSmithy();
		int lvl = skill.getLvl();
		if(pPlayer.getPvpTicks() > 0) {
			Output.simpleError(player,  "You cannot smelt while in or shortly after combat.");
			return;
		}
		if(!canRepair(item)) {
			Output.simpleError(player, "You cannot smelt "+item.getType().name().toLowerCase().replace("_", " ")+".");
			return;
		}
		int amount = 1;
		Material cost = null;
		boolean cansmelt = false;
		if(ItemUtils.isDiamond(item)) {
			cost = Material.DIAMOND;
			if(lvl >= 750)
				cansmelt = true;
		}else if(ItemUtils.isGold(item)){
			cost = Material.GOLD_INGOT;
			if(lvl >= 500)
				cansmelt = true;
		}else if(ItemUtils.isIron(item)){
			cost = Material.IRON_INGOT;
			if(lvl >= 250)
				cansmelt = true;
		}
		
		if(cost == null) {
			Output.simpleError(player, "You cannot smelt "+item.getType().name().toLowerCase().replace("_", " ")+".");
			return;
		}
		if(ItemUtils.isArmor(cost))
			amount = 3;
		player.getInventory().setItemInHand(null);
		pPlayer.setStamina(pPlayer.getStamina()-SMELT_STAMINA_COST);
		if(cansmelt) {
			player.getWorld().dropItem(player.getLocation(), new ItemStack(cost, amount)); 
			Output.positiveMessage(player, "You have smeltet your "+item.getType().name().toLowerCase().replace("_", " ")+" into "+cost.name().toLowerCase()+".");
		}else{
			Output.simpleError(player, "You have smeltet your "+item.getType().name().toLowerCase().replace("_", " ")+" but failed to recover any use full resources from the smelting.");
		}
	}
	
	public static void enhance(Player player) {
		ItemStack item = player.getItemInHand();
		if(item == null) {
			Output.simpleError(player, "You cannot enhance air.");
			return;
		}
		PseudoPlayer pPlayer = pm.getPlayer(player);
		Skill skill = pPlayer.getCurrentBuild().getBlackSmithy();
		int lvl = skill.getLvl();
		if(pPlayer.getPvpTicks() > 0) {
			Output.simpleError(player,  "You cannot enhance while in or shortly after combat.");
			return;
		}
		if(!canRepair(item)) {
			Output.simpleError(player, "You cannot enhance "+item.getType().name().toLowerCase().replace("_", " ")+".");
			return;
		}
		if(lvl < 250) {
			Output.simpleError(player, "You are not skilled enough to enhance this tool.");
			return;
		}
		
		int costAmount = 2;
		Material cost = null;
		if(ItemUtils.isDiamond(item)) {
			cost = Material.DIAMOND;
		}else if(ItemUtils.isGold(item)){
			cost = Material.GOLD_INGOT;
		}else if(ItemUtils.isIron(item)){
			cost = Material.IRON_INGOT;
		}else if(ItemUtils.isStone(item))
			cost = Material.COBBLESTONE;
		
		if(cost == null) {
			Output.simpleError(player, "You cannot enhance "+item.getType().name().toLowerCase().replace("_", " ")+".");
			return;
		}
		
		boolean isTool = false;
		boolean isSword = false;
		boolean isBow = false;
		boolean isArmor = false;
		if(ItemUtils.isArmor(item))
			isArmor = true;
		else if(ItemUtils.isSword(item) || ItemUtils.isAxe(item))
			isSword = true;
		else if(ItemUtils.isTool(item))
			isTool = true;
		else if(item.getType().equals(Material.BOW))
			isBow = true;
		else {
			Output.simpleError(player, "You cannot enhance "+item.getType().name().toLowerCase().replace("_", " ")+".");
			return;
		}
		
		if(isTool) {
			costAmount = item.getItemMeta().getEnchantLevel(Enchantment.DIG_SPEED)+2;
			if(costAmount > 6) {
				Output.simpleError(player, "You cannot enhance this tool above lvl 5");
				return;
			}
			Plot plot = ptm.findPlotAt(player.getLocation());
			if(costAmount > 5 && plot == null) {
				return;
			}
			if(!player.getInventory().contains(cost, costAmount)) {
				Output.simpleError(player, "You do not have enough "+StringUtils.lowerCase(cost.name()).replace("_", " ")+" to repair that tool, requires "+costAmount+".");
				return;
			}
			pPlayer.setStamina(pPlayer.getStamina()-ENHANCE_STAMINA_COST);
			ItemUtils.removeItem(player.getInventory(), cost, costAmount);
			if(costAmount > 4) {
				item.addEnchantment(Enchantment.DIG_SPEED, costAmount-1);
			}else{
				item.addEnchantment(Enchantment.DIG_SPEED, costAmount-1);
				item.addEnchantment(Enchantment.DIG_SPEED, costAmount-1);
			}
			Output.positiveMessage(player, "You have enhanced "+item.getType().name().toLowerCase().replace("_", " ")+" to lvl "+(costAmount-1));
		}else if(isSword) {
			costAmount = item.getItemMeta().getEnchantLevel(Enchantment.DAMAGE_ALL)+2;
			
		}else if(isBow) {
			costAmount = item.getItemMeta().getEnchantLevel(Enchantment.ARROW_DAMAGE)+2;
			
		}else if(isArmor) {
			
		}else{
			Output.simpleError(player, "You cannot enhance "+item.getType().name().toLowerCase().replace("_", " ")+".");
			return;
		}
	}
	
	public static boolean canRepair(Material item) {
		if(
				//Diamond
				item.equals(Material.DIAMOND_AXE)
				|| item.equals(Material.DIAMOND_PICKAXE)
				|| item.equals(Material.DIAMOND_BOOTS)
				|| item.equals(Material.DIAMOND_CHESTPLATE)
				|| item.equals(Material.DIAMOND_HELMET)
				|| item.equals(Material.DIAMOND_HOE)
				|| item.equals(Material.DIAMOND_LEGGINGS)
				|| item.equals(Material.DIAMOND_SPADE)
				|| item.equals(Material.DIAMOND_SWORD)
				//Iron
				|| item.equals(Material.IRON_AXE)
				|| item.equals(Material.IRON_BOOTS)
				|| item.equals(Material.IRON_CHESTPLATE)
				|| item.equals(Material.IRON_HELMET)
				|| item.equals(Material.IRON_HOE)
				|| item.equals(Material.IRON_LEGGINGS)
				|| item.equals(Material.IRON_PICKAXE)
				|| item.equals(Material.IRON_SPADE)
				|| item.equals(Material.IRON_SWORD)
				//Gold
				|| item.equals(Material.GOLD_AXE)
				|| item.equals(Material.GOLD_BOOTS)
				|| item.equals(Material.GOLD_CHESTPLATE)
				|| item.equals(Material.GOLD_HELMET)
				|| item.equals(Material.GOLD_HOE)
				|| item.equals(Material.GOLD_LEGGINGS)
				|| item.equals(Material.GOLD_PICKAXE)
				|| item.equals(Material.GOLD_SPADE)
				|| item.equals(Material.GOLD_SWORD)
				//Stone
				|| item.equals(Material.STONE_AXE)
				|| item.equals(Material.STONE_HOE)
				|| item.equals(Material.STONE_PICKAXE)
				|| item.equals(Material.STONE_SPADE)
				|| item.equals(Material.STONE_SWORD)
				//Wood
				||item.equals(Material.WOOD_AXE)
				|| item.equals(Material.WOOD_HOE)
				|| item.equals(Material.WOOD_PICKAXE)
				|| item.equals(Material.WOOD_SPADE)
				|| item.equals(Material.WOOD_SWORD)
				)
			return true;
		return false;
	}
	
	public static boolean canRepair(ItemStack item) {
		return canRepair(item.getType());
	}
}
