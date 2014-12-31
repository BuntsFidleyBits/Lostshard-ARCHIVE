package com.lostshard.lostshard.Main;

import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.lostshard.lostshard.Commands.AdminCommand;
import com.lostshard.lostshard.Commands.BankCommand;
import com.lostshard.lostshard.Commands.ChatCommand;
import com.lostshard.lostshard.Commands.ClanCommand;
import com.lostshard.lostshard.Commands.ControlPointsCommand;
import com.lostshard.lostshard.Commands.MageryCommand;
import com.lostshard.lostshard.Commands.PlotCommand;
import com.lostshard.lostshard.Commands.UtilsCommand;
import com.lostshard.lostshard.Database.DataSource;
import com.lostshard.lostshard.Database.Database;
import com.lostshard.lostshard.Handlers.PseudoPlayerHandler;
import com.lostshard.lostshard.Listener.BlockListener;
import com.lostshard.lostshard.Listener.CitizensLisenter;
import com.lostshard.lostshard.Listener.EntityListener;
import com.lostshard.lostshard.Listener.PlayerListener;
import com.lostshard.lostshard.Listener.ServerListener;
import com.lostshard.lostshard.Listener.VehicleListener;
import com.lostshard.lostshard.Listener.VoteListener;
import com.lostshard.lostshard.Listener.WorldListener;
import com.lostshard.lostshard.NPC.NPC;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Objects.Registry;
import com.lostshard.lostshard.Objects.Groups.Clan;

/**
 * @author Jacob Rosborg
 *
 */
public class Lostshard extends JavaPlugin {

	public static Logger log;
	
	private static Registry registry = new Registry();
	
	private static BukkitTask gameLoop;
	
	private static Plugin plugin;
	
	private static boolean mysqlError = true;
	
	private static boolean debug = true;
	
	@Override
	public void onEnable() {
		log = this.getLogger();
		log.info(ChatColor.GREEN + "Lostshard has invoke.");
		
		// Lisenters
		new BlockListener(this);
		new EntityListener(this);
		new PlayerListener(this);
		new ServerListener(this);
		new VehicleListener(this);
		if (getServer().getPluginManager().isPluginEnabled("votifier"))
			new VoteListener(this);
		new WorldListener(this);
		new CitizensLisenter(this);
		// Commands
		new PlotCommand(this);
		new ChatCommand(this);
		new BankCommand(this);
		new ControlPointsCommand(this);
		new UtilsCommand(this);
		new AdminCommand(this);
		new MageryCommand(this);
		new ClanCommand(this);
		
		Lostshard.setPlugin(this);
		
		setMysqlError(!Database.testDatabaseConnection());
		
		Database.getClans();
		Database.getPlayers();
		Database.getPlots();
		
		for(Clan c : getRegistry().getClans())
			for(UUID uuid : c.getMembersAndLeders()) {
				PseudoPlayer pPlayer = PseudoPlayerHandler.getPlayer(uuid);
				pPlayer.setClan(c);
			}
		// GameLoop should run last.
		CustomSchedule.Schedule();
		gameLoop = new GameLoop(this).runTaskTimer(this, 0L, 2L);
	}

	@Override
	public void onDisable() {
		for (Player p : Bukkit.getOnlinePlayers())
			p.kickPlayer(ChatColor.RED + "Server restarting.");
//		 Database.saveAll(); 
	}

	public static BukkitTask getGameLoop() {
		return gameLoop;
	}

	public static void shutdown() {
		for (Player p : Bukkit.getOnlinePlayers())
			p.kickPlayer(ChatColor.RED + "Server rebooting.");
	}

	public static Registry getRegistry() {
		return registry;
	}

	public static void setRegistry(Registry registry) {
		Lostshard.registry = registry;
	}

	public static Plugin getPlugin() {
		return plugin;
	}

	public static void setPlugin(Plugin plugin) {
		Lostshard.plugin = plugin;
	}

	public static boolean isMysqlError() {
		return mysqlError;
	}

	public static void setMysqlError(boolean mysqlError) {
		Lostshard.mysqlError = mysqlError;
	}
	
	public static void mysqlError() {
		Lostshard.mysqlError = true;
		System.out.print("ERROR MYSQL");
		for(Player p : Bukkit.getOnlinePlayers())
			p.kickPlayer(ChatColor.RED+"Server ERROR something went wrong..");
	}

	public static boolean isDebug() {
		return debug;
	}

	public static void setDebug(boolean debug) {
		Lostshard.debug = debug;
	}

}
