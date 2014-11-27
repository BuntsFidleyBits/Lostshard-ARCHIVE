package com.lostshard.lostshard.Skills;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Handlers.PseudoPlayerHandler;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Utils.Output;

public class MiningSkill extends Skill {

	static double miningdropprob = .15;
	static double coaldroprate = .305344;
	static double coaloredroprate = .381679;
	static double ironordroprate = .534351;
	static double redstonedroprate = .656489;
	static double redstoneoredroprate = .687023;
	static double lapisblockdroprate = .717557;
	static double lapisoreblockdroprate = .839695;
	static double goldoredroprate = .916031;
	static double emeralddroprate = .946565;
	static double emeraldoredroprate = .954198;
	static double diamonddroprate = .984733;
	static double diamondoredroprate = .992366;

	public MiningSkill() {
		super();
		setName("Mining");
		setScaleConstant(37);
		setBaseProb(.2);
	}

	public static void onBlockBreak(BlockBreakEvent event) {
		if (event.isCancelled())
			return;
		Player player = event.getPlayer();
		if (player.getGameMode().equals(GameMode.CREATIVE))
			return;
		Block block = event.getBlock();
		PseudoPlayer pPlayer = PseudoPlayerHandler.getPlayer(player);
		if (block.getType().equals(Material.STONE)
				&& (player.getItemInHand().getType().equals(Material.DIAMOND_PICKAXE) ||
						player.getItemInHand().getType().equals(Material.GOLD_PICKAXE) ||
						player.getItemInHand().getType().equals(Material.IRON_PICKAXE) ||
						player.getItemInHand().getType().equals(Material.STONE_PICKAXE) || 
						player.getItemInHand().getType().equals(Material.WOOD_PICKAXE))) {

			int gain = pPlayer.getCurrentBuild().getMining().skillGain();

			int curSkill = pPlayer.getCurrentBuild().getMining().getLvl();

			double percent = (double) curSkill / 1000.0;

			Output.gainSkill(player, "Mining", gain, curSkill);

			// If we are not capped, see if we gained skill

			double chanceOfDrop = miningdropprob * percent;

			if (Math.random() < chanceOfDrop) {

				double itemSelect = Math.random();

				if (coaldroprate > itemSelect) {
					block.getWorld().dropItemNaturally(block.getLocation(),
							new ItemStack(Material.COAL, 1));
				} else if (coaloredroprate > itemSelect) {
					block.getWorld().dropItemNaturally(block.getLocation(),
							new ItemStack(Material.COAL_ORE, 1));
				} else if (ironordroprate > itemSelect) {
					block.getWorld().dropItemNaturally(block.getLocation(),
							new ItemStack(Material.IRON_ORE, 1));
				} else if (redstonedroprate > itemSelect) {
					block.getWorld().dropItemNaturally(block.getLocation(),
							new ItemStack(Material.REDSTONE, 1));
				} else if (redstoneoredroprate > itemSelect) {
					block.getWorld().dropItemNaturally(block.getLocation(),
							new ItemStack(Material.REDSTONE_ORE, 1));
				} else if (lapisblockdroprate > itemSelect) {
					block.getWorld().dropItemNaturally(block.getLocation(),
							new ItemStack(Material.LAPIS_BLOCK, 1));
				} else if (lapisoreblockdroprate > itemSelect) {
					block.getWorld().dropItemNaturally(block.getLocation(),
							new ItemStack(Material.LAPIS_ORE, 1));
				} else if (goldoredroprate > itemSelect) {
					block.getWorld().dropItemNaturally(block.getLocation(),
							new ItemStack(Material.GOLD_ORE, 1));
				} else if (emeralddroprate > itemSelect) {
					block.getWorld().dropItemNaturally(block.getLocation(),
							new ItemStack(Material.EMERALD, 1));
				} else if (emeraldoredroprate > itemSelect) {
					block.getWorld().dropItemNaturally(block.getLocation(),
							new ItemStack(Material.EMERALD_ORE, 1));
				} else if (diamonddroprate > itemSelect) {
					block.getWorld().dropItemNaturally(block.getLocation(),
							new ItemStack(Material.DIAMOND, 1));
				} else if (diamondoredroprate > itemSelect) {
					block.getWorld().dropItemNaturally(block.getLocation(),
							new ItemStack(Material.DIAMOND_ORE, 1));
				} else {
					block.getWorld().dropItemNaturally(block.getLocation(),
							new ItemStack(Material.OBSIDIAN, 1));
				}
			}
		}
	}

}
