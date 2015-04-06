package com.lostshard.lostshard.Listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;

import com.lostshard.lostshard.Handlers.PlotProtectionHandler;
import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Skills.LumberjackingSkill;
import com.lostshard.lostshard.Skills.MiningSkill;
import com.lostshard.lostshard.Spells.Structures.Gate;

public class BlockListener implements Listener {

	public BlockListener(Lostshard plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreakHigh(BlockBreakEvent event) {
		PlotProtectionHandler.breakeBlockInPlot(event);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockBreakLowest(BlockBreakEvent event) {
		MiningSkill.onBlockBreak(event);
		LumberjackingSkill.blockBrokeWithAxe(event);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPlace(BlockPlaceEvent event) {
		PlotProtectionHandler.placeBlockInPlot(event);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockPlaceLow(BlockPlaceEvent event) {
		if(event.isCancelled())
			return;
		Block block = event.getBlock();
		block.setMetadata("placed", new FixedMetadataValue(Lostshard.getPlugin(), true));
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBurn(BlockBurnEvent event) {
		PlotProtectionHandler.burnBlockInPlot(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockIgnite(BlockIgniteEvent event) {
		PlotProtectionHandler.igniteBlockInPlot(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockFromTo(BlockFromToEvent event) {
		PlotProtectionHandler.fromBlockToBlock(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPistonExtend(BlockPistonExtendEvent event) {
		PlotProtectionHandler.onPistonExtend(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockFade(BlockFadeEvent event) {
		PlotProtectionHandler.onBlockFade(event);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPhysics(BlockPhysicsEvent event) {
		if (event.isCancelled())
			return;
		if (event.getBlock().getType().equals(Material.PORTAL)) {
			event.setCancelled(true);
		}
		Gate.onBlockPhysics(event);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onHangingBreak(HangingBreakEvent event) {
		PlotProtectionHandler.onHangingDestory(event);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onHangingPlace(HangingPlaceEvent event) {
		PlotProtectionHandler.onHangingPlace(event);
	}
}