package com.lostshard.lostshard.Utils;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Objects.Plot;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Objects.Groups.Clan;

public class Output {

	static PlayerManager pm = PlayerManager.getManager();
	
	public static void mustBePlayer(CommandSender sender) {
		sender.sendMessage(ChatColor.RED + "Only players may perform this command!");
	}

	public static void gainSkill(Player player, String skillName, int gainAmount, int totalSkill) {
		if(gainAmount <= 0)
			return;
		player.sendMessage(ChatColor.GOLD+"You have gained "+Utils.scaledIntToString(gainAmount)+" "+skillName+", it is now "+Utils.scaledIntToString(totalSkill)+".");
	}
	
	public static void plotHelp(Player player) {
		player.sendMessage(ChatColor.GOLD + "-Plot Help-");
		player.sendMessage(ChatColor.YELLOW + "/plot create (name)");
		player.sendMessage(ChatColor.YELLOW + "/plot deposit (amount)");
		player.sendMessage(ChatColor.YELLOW + "/plot expand (amount)");
	}

	public static void outputPlayerlist(CommandSender sender) {
		ArrayList<String> filteredPlayers = new ArrayList<String>();
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.isOp())
				continue;
			filteredPlayers.add(Utils.getDisplayName(p));
		}

		Collections.sort(filteredPlayers, String.CASE_INSENSITIVE_ORDER);

		String message = ChatColor.YELLOW + "Online Players ("
				+ filteredPlayers.size() + "/" + Bukkit.getMaxPlayers() + "):"
				+ ChatColor.WHITE + " ";
		message+=StringUtils.join(filteredPlayers, ", ");
		sender.sendMessage(message);
	}

	public static void outputSkills(Player player) {
		PseudoPlayer pseudoPlayer = pm.getPlayer(player);
		player.sendMessage(ChatColor.GOLD+"-"+player.getName()+"'s Skills-");
		player.sendMessage(ChatColor.YELLOW+"You currently have "+Utils.scaledIntToString(pseudoPlayer.getCurrentBuild().getTotalSkillVal())+"/"+Utils.scaledIntToString(pseudoPlayer.getMaxSkillValTotal()) + " skill points.");
		if(pseudoPlayer.getCurrentBuild().getArchery().isLocked())
			player.sendMessage(ChatColor.YELLOW+"Archery(L): " +ChatColor.WHITE+ Utils.scaledIntToString(pseudoPlayer.getCurrentBuild().getArchery().getLvl()));
		else
			player.sendMessage(ChatColor.YELLOW+"Archery: " +ChatColor.WHITE+ Utils.scaledIntToString(pseudoPlayer.getCurrentBuild().getArchery().getLvl()));
		
		if(pseudoPlayer.getCurrentBuild().getBlackSmithy().isLocked())
			player.sendMessage(ChatColor.YELLOW+"Blacksmithy(L): " +ChatColor.WHITE+ Utils.scaledIntToString(pseudoPlayer.getCurrentBuild().getBlackSmithy().getLvl()));
		else
			player.sendMessage(ChatColor.YELLOW+"Blacksmithy: " +ChatColor.WHITE+ Utils.scaledIntToString(pseudoPlayer.getCurrentBuild().getBlackSmithy().getLvl()));
		
		if(pseudoPlayer.getCurrentBuild().getBrawling().isLocked())
			player.sendMessage(ChatColor.YELLOW+"Brawling(L): " +ChatColor.WHITE+ Utils.scaledIntToString(pseudoPlayer.getCurrentBuild().getBrawling().getLvl()));
		else
			player.sendMessage(ChatColor.YELLOW+"Brawling: " +ChatColor.WHITE+ Utils.scaledIntToString(pseudoPlayer.getCurrentBuild().getBrawling().getLvl()));
		
		if(pseudoPlayer.getCurrentBuild().getMagery().isLocked())
			player.sendMessage(ChatColor.YELLOW+"Magery(L): " +ChatColor.WHITE+ Utils.scaledIntToString(pseudoPlayer.getCurrentBuild().getMagery().getLvl()));
		else
			player.sendMessage(ChatColor.YELLOW+"Magery: " +ChatColor.WHITE+ Utils.scaledIntToString(pseudoPlayer.getCurrentBuild().getMagery().getLvl()));
		
		if(pseudoPlayer.getCurrentBuild().getBlades().isLocked())
			player.sendMessage(ChatColor.YELLOW+"bladess(L): " +ChatColor.WHITE+ Utils.scaledIntToString(pseudoPlayer.getCurrentBuild().getBlades().getLvl()));
		else
			player.sendMessage(ChatColor.YELLOW+"bladess: " +ChatColor.WHITE+ Utils.scaledIntToString(pseudoPlayer.getCurrentBuild().getBlades().getLvl()));
		
		if(pseudoPlayer.getCurrentBuild().getSurvivalism().isLocked())
			player.sendMessage(ChatColor.YELLOW+"Survivalism(L): " +ChatColor.WHITE+ Utils.scaledIntToString(pseudoPlayer.getCurrentBuild().getSurvivalism().getLvl()));
		else
			player.sendMessage(ChatColor.YELLOW+"Survivalism: " +ChatColor.WHITE+ Utils.scaledIntToString(pseudoPlayer.getCurrentBuild().getSurvivalism().getLvl()));
		
		if(pseudoPlayer.getCurrentBuild().getMining().isLocked())
			player.sendMessage(ChatColor.YELLOW+"Mining(L): " +ChatColor.WHITE+ Utils.scaledIntToString(pseudoPlayer.getCurrentBuild().getMining().getLvl()));
		else
			player.sendMessage(ChatColor.YELLOW+"Mining: " +ChatColor.WHITE+ Utils.scaledIntToString(pseudoPlayer.getCurrentBuild().getMining().getLvl()));
		
		if(pseudoPlayer.getCurrentBuild().getLumberjacking().isLocked())
			player.sendMessage(ChatColor.YELLOW+"Lumberjacking(L): " +ChatColor.WHITE+ Utils.scaledIntToString(pseudoPlayer.getCurrentBuild().getLumberjacking().getLvl()));
		else
			player.sendMessage(ChatColor.YELLOW+"Lumberjacking: " +ChatColor.WHITE+ Utils.scaledIntToString(pseudoPlayer.getCurrentBuild().getLumberjacking().getLvl()));
		
		if(pseudoPlayer.getCurrentBuild().getTaming().isLocked())
			player.sendMessage(ChatColor.YELLOW+"Taming(L): " +ChatColor.WHITE+ Utils.scaledIntToString(pseudoPlayer.getCurrentBuild().getTaming().getLvl()));
		else
			player.sendMessage(ChatColor.YELLOW+"Taming: " +ChatColor.WHITE+ Utils.scaledIntToString(pseudoPlayer.getCurrentBuild().getTaming().getLvl()));
		if(pseudoPlayer.getCurrentBuild().getFishing().isLocked())
			player.sendMessage(ChatColor.YELLOW+"Fishing(L): " +ChatColor.WHITE+ Utils.scaledIntToString(pseudoPlayer.getCurrentBuild().getFishing().getLvl()));
		else
			player.sendMessage(ChatColor.YELLOW+"Fishing: " +ChatColor.WHITE+ Utils.scaledIntToString(pseudoPlayer.getCurrentBuild().getFishing().getLvl()));
	}
	
	public static void displayLoginMessages(Player player) {
		player.sendMessage(ChatColor.GOLD
				+ "Welcome to Lost Shard, www.lostshard.com for more info.");
		player.sendMessage(ChatColor.GOLD + "Lostshard is curently in beta!");
		player.sendMessage(ChatColor.GOLD
				+ "Visit wiki.lostshard.com for more info");
		player.sendMessage(ChatColor.GOLD
				+ "The guide has valuable information for getting started.");
		player.sendMessage(ChatColor.GOLD
				+ "Use /rules for rules and /help for help");
		player.sendMessage(ChatColor.GOLD
				+ "Please communicate in English when using Global Chat.");
		player.sendMessage(ChatColor.RED
				+ "-Combat logging drops your items on logout.");

	}

	public static void plotInfo(Player player, Plot plot) {
		player.sendMessage(ChatColor.GOLD + "-" + plot.getName()
				+ "'s Plot Info-");
		String infoText = "";
		if (plot.isProtected())
			infoText += ChatColor.YELLOW + "Protected: " + ChatColor.WHITE
					+ "Yes";
		else
			infoText += ChatColor.YELLOW + "Protected: " + ChatColor.WHITE
					+ "No";
		infoText += ChatColor.YELLOW + ", ";
		if (plot.isPrivatePlot())
			infoText += ChatColor.YELLOW + "Status: " + ChatColor.WHITE
					+ "Private";
		else
			infoText += ChatColor.YELLOW + "Status: " + ChatColor.WHITE
					+ "Public";
		infoText += ChatColor.YELLOW + ", ";
		if (plot.isNeutralAlignment())
			infoText += ChatColor.YELLOW + "Alignment: " + ChatColor.WHITE
					+ "Neutral";
		else if (pm.getPlayer(plot.getOwner())
				.getMurderCounts() < 5)
			infoText += ChatColor.YELLOW + "Alignment: " + ChatColor.BLUE
					+ "Lawful";
		else
			infoText += ChatColor.YELLOW + "Alignment: " + ChatColor.RED
					+ "Criminal";

		player.sendMessage(infoText);

		infoText = "";
		if (plot.isAllowExplosions())
			infoText += ChatColor.YELLOW + "Allow Explosions: "
					+ ChatColor.WHITE + "Yes";
		else
			infoText += ChatColor.YELLOW + "Allow Explosions: "
					+ ChatColor.WHITE + "No";

		player.sendMessage(infoText);

		// Display your position in the plot
		if (plot.isOwner(player))
			player.sendMessage(ChatColor.YELLOW
					+ "You are the owner of this plot.");
		else if (plot.isCoowner(player))
			player.sendMessage(ChatColor.YELLOW
					+ "You are a co-owner of this plot.");
		else if (plot.isFriend(player))
			player.sendMessage(ChatColor.YELLOW
					+ "You are a friend of this plot.");
		else
			player.sendMessage(ChatColor.YELLOW
					+ "You are not a friend of this plot.");

		if (plot.isCapturePoint()) {
			Clan clan = plot.getOwningClan();
			if (clan != null)
				player.sendMessage(ChatColor.YELLOW + "Owning Clan: "
						+ ChatColor.WHITE + clan.getName());
			else
				player.sendMessage(ChatColor.YELLOW + "Owning Clan: "
						+ ChatColor.RED + "NONE");
		}

		// Show this stuff to everyone
		if (plot.getSalePrice() > 0)
			player.sendMessage(ChatColor.YELLOW + "Owner: " + ChatColor.WHITE
					+ Bukkit.getOfflinePlayer(plot.getOwner()).getName() + ", "
					+ ChatColor.YELLOW + "Sale Price: " + ChatColor.WHITE
					+ plot.getSalePrice());
		else
			player.sendMessage(ChatColor.YELLOW + "Owner: " + ChatColor.WHITE
					+ Bukkit.getOfflinePlayer(plot.getOwner()).getName() + ", "
					+ ChatColor.YELLOW + "Sale Price: " + ChatColor.RED
					+ "Not for sale");
		// Only show the owner/co-owner the amount of money in the region bank
		if (plot.isCoownerOrAbove(player)) {
			int moneyPerDay = 10 * plot.getSize();
			player.sendMessage(ChatColor.YELLOW + "Size: " + ChatColor.WHITE
					+ plot.getSize() + ChatColor.YELLOW + ", Funds: "
					+ ChatColor.WHITE + plot.getMoney() + ChatColor.YELLOW
					+ ", Tax: " + ChatColor.WHITE + moneyPerDay
					+ ChatColor.YELLOW + ", Plot Value: " + ChatColor.WHITE
					+ plot.getValue());
			int distanceFromCenter = (int) Math.round(Utils.distance(
					player.getLocation(), plot.getLocation()));
			player.sendMessage(ChatColor.YELLOW + "Center: " + ChatColor.WHITE
					+ "(" + plot.getLocation().getBlockX() + ","
					+ plot.getLocation().getBlockY() + ","
					+ plot.getLocation().getBlockZ() + ")" + ChatColor.YELLOW
					+ ", Distance From Center: " + ChatColor.WHITE
					+ distanceFromCenter);
		} else {
			player.sendMessage(ChatColor.YELLOW + "Size: " + ChatColor.WHITE
					+ plot.getSize());
		}
		// Show member lists to everyone who is at least a friend
		if (plot.isFriendOrAbove(player)) {
			player.sendMessage(ChatColor.YELLOW + "Co-Owners: "+ChatColor.WHITE
					+ Utils.listToString(Utils.UUIDArrayToUsernameArray(plot.getCoowners())));
			player.sendMessage(ChatColor.YELLOW + "Friends: "+ChatColor.WHITE
					+ Utils.listToString(Utils.UUIDArrayToUsernameArray(plot.getFriends())));
		}
	}

	public static void outputWho(CommandSender sender,
			PseudoPlayer whoPseudoPlayer) {
		sender.sendMessage(ChatColor.GOLD + "-Player Information-");
		sender.sendMessage(ChatColor.YELLOW + "Name: "
				+ whoPseudoPlayer.getColoredName());
		if (whoPseudoPlayer.isMurderer())
			sender.sendMessage(ChatColor.RED + "This player is a murderer.");
		else if (whoPseudoPlayer.isCriminal())
			sender.sendMessage(ChatColor.RED + "This player is a criminal.");
		sender.sendMessage(ChatColor.YELLOW + "Murder Counts: "
				+ ChatColor.WHITE + whoPseudoPlayer.getMurderCounts());
		sender.sendMessage(ChatColor.YELLOW + "Rank: " + ChatColor.WHITE
				+ whoPseudoPlayer.getRank());
		Clan clan = whoPseudoPlayer.getClan();
		if (clan != null)
			sender.sendMessage(ChatColor.YELLOW + "Clan: " + ChatColor.WHITE
					+ clan.getName());
		else
			sender.sendMessage(ChatColor.YELLOW + "Clan: " + ChatColor.WHITE
					+ "none");
	}

	public static void simpleError(CommandSender sender, String message) {
		sender.sendMessage(ChatColor.DARK_RED + message);
	}

	public static void positiveMessage(CommandSender sender, String message) {
		sender.sendMessage(ChatColor.GOLD + message);
	}

	public static void notNumber(Player player) {
		simpleError(player, "Need to be a number.");
	}

	public static void plotNotIn(Player player) {
		simpleError(player, "You are not currently in a plot.");
	}

	public static void capturePointsInfo(Player player) {
		player.sendMessage(ChatColor.GOLD + "-Control Points-");
		player.sendMessage(ChatColor.YELLOW + "Hostility: 0, 0, 0");
	}

	public static void playerStats(Player player) {
		PseudoPlayer pseudoPlayer = pm.getPlayer(player);
		player.sendMessage(ChatColor.GOLD + "-" + player.getName()
				+ "'s Statistics-");
		player.sendMessage(ChatColor.YELLOW + "Gold Coins: " + ChatColor.WHITE
				+ pseudoPlayer.getMoney());
		player.sendMessage(ChatColor.YELLOW + "Mana: " + ChatColor.WHITE
				+ pseudoPlayer.getMana() + "/" + 100);
		player.sendMessage(ChatColor.YELLOW + "Stamina: " + ChatColor.WHITE
				+ pseudoPlayer.getStamina() + "/" + 100);
		player.sendMessage(ChatColor.YELLOW+"Build: "+ChatColor.WHITE+
		pseudoPlayer.getCurrentBuildId());
		player.sendMessage(ChatColor.YELLOW + "Reputation: "
				+ ChatColor.WHITE + pseudoPlayer.getReputation().getReputation());
		player.sendMessage(ChatColor.YELLOW+"Rank: " +
		ChatColor.WHITE+pseudoPlayer.getRank());
	}

	public static void displayStats(CommandSender sender) {
		if (sender instanceof Player)
			Output.playerStats((Player) sender);
		else
			sender.sendMessage(ChatColor.DARK_RED
					+ "You must be a player to perform this command.");
	}

	public static void displayWho(CommandSender sender, String[] args) {
		if (args.length < 1) {
			Output.simpleError(sender,
					"Invalid syntax, use /whois (player name)");
			return;
		}
		
		String targetName = args[0];
		@SuppressWarnings("deprecation")
		Player p = Bukkit.getPlayer(targetName);
		if (p != null) {
			PseudoPlayer targetPseudoPlayer = pm.getPlayer(p);
			if (p.isOp()) {
				Output.simpleError(sender, "That player is not online.");
			} else {
				Output.outputWho(sender, targetPseudoPlayer);
			}
		} else {
			Output.simpleError(sender, "That player is not online.");
		}
		return;
	}

	public static void displayRules(CommandSender sender) {
		Output.positiveMessage(sender, "-Lost Shard Rules-");
		sender.sendMessage(ChatColor.YELLOW + "Short Version: Don't Cheat");
		sender.sendMessage(ChatColor.YELLOW
				+ "Long Version: http://wiki.lostshard.com/index.php?title=Rules");
	}

	public static void outputClanInfo(Player player, Clan clan) {
		player.sendMessage(ChatColor.GOLD+"-"+clan.getName()+"'s Info-");
		player.sendMessage(ChatColor.YELLOW+"Clan Owner: " +ChatColor.WHITE+ Bukkit.getOfflinePlayer(clan.getOwner()).getName());
		
		player.sendMessage(ChatColor.YELLOW+"Clan Leaders: " +ChatColor.WHITE+Utils.listToString(Utils.UUIDArrayToUsernameArray(clan.getLeaders())));
		
		player.sendMessage(ChatColor.YELLOW+"Clan Members: " +ChatColor.WHITE+Utils.listToString(Utils.UUIDArrayToUsernameArray(clan.getMembers())));
	}
}
