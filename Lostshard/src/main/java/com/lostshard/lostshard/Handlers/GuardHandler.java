package com.lostshard.lostshard.Handlers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.NPC.NPC;
import com.lostshard.lostshard.NPC.NPCManager;
import com.lostshard.lostshard.Objects.Plot;
import com.lostshard.lostshard.Objects.PseudoPlayer;

public class GuardHandler {

	public static void  Guard(Player player) {
		//Checking if the player are inside a plot
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if(plot == null)
			return;
		//Checking to see if the player himself are criminal
		PseudoPlayer pPlayer = PseudoPlayerHandler.getPlayer(player);
		if(pPlayer.isCriminal() || pPlayer.isMurder())
			return;
		//Finding the nearest guard, on the same plot.
		NPC guard = null;
		for(NPC g : plot.getNpcs()) {
			Plot gP = PlotHandler.findPlotAt(g.getLocation());
			if(guard == null || gP.getLocation().distance(player.getLocation()) < guard.getLocation().distance(player.getLocation()))
				guard = g;
		}
		//Check if the plot is guarded
		if(guard == null)
			return;
		//Storing all criminals in range of the player
		List<Player> criminals = new ArrayList<Player>();
		//Going trough all players and checking for who's in range and who's criminal
		for(Player pCP : Bukkit.getOnlinePlayers()) {
			PseudoPlayer pPCP = PseudoPlayerHandler.getPlayer(pCP);
			if(pPCP.isCriminal() || pPCP.isMurder())
				criminals.add(pCP);
		}
		//Slaying all criminals that are in range
		for(Player c : criminals) {
			NPCManager.getNPC(guard.getId()).teleport(c.getLocation(), TeleportCause.PLUGIN);
			c.damage(0);
			c.setHealth(0);
		}
		NPCManager.getNPC(guard.getId()).teleport(guard.getLocation(), TeleportCause.PLUGIN);
	}
	
}
