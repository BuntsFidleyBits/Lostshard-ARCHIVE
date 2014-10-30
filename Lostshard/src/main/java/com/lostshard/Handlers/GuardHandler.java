package com.lostshard.Handlers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.lostshard.Main.Lostshard;
import com.lostshard.NPC.NPC;
import com.lostshard.Objects.Plot;
import com.lostshard.Objects.PseudoPlayer;

public class GuardHandler {

	public static void  Guard(Player player) {
		//Checking if the player are inside a plot
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if(plot == null)
			return;
		//Finding the nearst guard, on the same plot.
		NPC guard = null;
		for(NPC g : Lostshard.getNpcs()) {
			Plot gP = PlotHandler.findPlotAt(g.getLocation());
			if(guard == null || gP.getLocation().distance(player.getLocation()) < guard.getLocation().distance(player.getLocation()))
				if(gP == plot)
					guard = g;
		}
		//Check if the plot is guarded
		if(guard == null)
			return;
		//Checking to se if the player himself are criminal
		PseudoPlayer pPlayer = PseudoPlayerHandler.getPlayer(player);
		if(pPlayer.isCriminal())
			return;
		//Storing all criminals in range
		List<Player> criminals = new ArrayList<Player>();
		//Going trough all players and checking for who's in range and who's criminal
		for(Player pCP : Bukkit.getOnlinePlayers()) {
			PseudoPlayer pPCP = PseudoPlayerHandler.getPlayer(pCP);
			if(pPCP.isCriminal())
				criminals.add(pCP);
		}
		//Slaying all criminals
		for(Player c : criminals) {
			c.damage(0);
			c.setHealth(0);
		}
	}
	
}
