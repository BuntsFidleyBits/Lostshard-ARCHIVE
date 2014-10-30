package com.lostshard.Listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import com.lostshard.Data.Variables;
import com.lostshard.Main.Lostshard;

public class ServerListener implements Listener {
	
	public ServerListener(Lostshard plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPing(ServerListPingEvent event) {
		event.setMotd(Variables.motd);
		event.setMaxPlayers(Variables.maxPlayers);
	}
	
}
