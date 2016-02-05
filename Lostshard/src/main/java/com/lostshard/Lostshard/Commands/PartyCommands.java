package com.lostshard.Lostshard.Commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Objects.Groups.Party;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Utils.Output;
import com.lostshard.Lostshard.Utils.TabUtils;
import com.lostshard.Lostshard.Utils.Utils;

public class PartyCommands extends LostshardCommand {

	static PlayerManager pm = PlayerManager.getManager();

	public static void partyInfo(Player player) {
		// Output party details
		final PseudoPlayer pseudoPlayer = pm.getPlayer(player);
		final Party party = pseudoPlayer.getParty();
		if (party != null) {
			player.sendMessage(ChatColor.GOLD + "-Your Party-");
			final String partyMembersString = Utils.listToString(Utils.UUIDArrayToUsernameArray(party.getMembers()));
			player.sendMessage(ChatColor.YELLOW + "Party Members: " + ChatColor.WHITE + partyMembersString);

		} else
			Output.simpleError(player, "You are not currently in a party.");
	}

	public static void partyInvite(Player player, String[] split) {
		if (split.length > 1) {
			final PseudoPlayer pseudoPlayer = pm.getPlayer(player);
			Party party = pseudoPlayer.getParty();
			if (party == null) {
				party = new Party();
				party.addMember(player.getUniqueId());
				pseudoPlayer.setParty(party);
			}
			final Player invitedPlayer = Bukkit.getPlayer(split[1]);
			if (invitedPlayer != null || Lostshard.isVanished(invitedPlayer) && !player.isOp()) {
				if (invitedPlayer == player) {
					Output.simpleError(player, "You cant invite your self.");
					return;
				}
				if (!party.isMember(invitedPlayer)) {
					if (!party.isInvited(invitedPlayer.getUniqueId())) {
						party.addInvited(invitedPlayer.getUniqueId());
						Utils.sendSmartTextCommand(invitedPlayer,
								ChatColor.GOLD + player.getName() + " has invited you to a party, Click to join.",
								ChatColor.LIGHT_PURPLE + "Click to join the party.", "/party join " + player.getName());
						// Output.positiveMessage(invitedPlayer,
						// player.getName()
						// + " has invited you to join a party.");
						// Output.positiveMessage(invitedPlayer,
						// "Use /party join " + player.getName());
						Output.positiveMessage(player,
								"You have invited " + invitedPlayer.getName() + " to your party.");
					} else
						Output.simpleError(player, invitedPlayer.getName() + " has already been invited to the party.");
				} else
					Output.simpleError(player, invitedPlayer.getName() + " is already a member of the party.");
			} else
				Output.simpleError(player, "That player is not online.");
		} else
			Output.simpleError(player, "Use \"/party invite (player name)\"");
	}

	public static void partyJoin(Player player, String[] split) {
		final PseudoPlayer pseudoPlayer = pm.getPlayer(player);
		final Party party = pseudoPlayer.getParty();
		if (party == null) {
			if (split.length > 1) {
				final Player inviterPlayer = Bukkit.getPlayer(split[1]);
				if (inviterPlayer != null) {
					final PseudoPlayer inviterPseudoPlayer = pm.getPlayer(inviterPlayer);
					final Party inviterParty = inviterPseudoPlayer.getParty();
					if (inviterParty != null) {
						if (inviterParty.isInvited(player.getUniqueId())) {
							if (inviterParty.getMembers().size() >= 8) {
								Output.simpleError(player, "The party is full, max clan size is 8.");
								return;
							}
							inviterParty.sendMessage(player.getName() + " has joined the party.");
							inviterParty.removeInvited(player.getUniqueId());
							inviterParty.addMember(player.getUniqueId());
							pseudoPlayer.setParty(inviterParty);
							Output.positiveMessage(player, "You have joined " + inviterPlayer.getName() + "'s party.");
						} else
							Output.simpleError(player, "You have not been invited to that party.");
					} else
						Output.simpleError(player, inviterPlayer.getName() + " is not in a party.");
				} else
					Output.simpleError(player, "That player is not online.");
			} else
				Output.simpleError(player, "Invalid syntax.");
		} else
			Output.simpleError(player, "You are already in a party.");
	}

	public static void partyLeave(Player player) {
		final PseudoPlayer pseudoPlayer = pm.getPlayer(player);
		final Party party = pseudoPlayer.getParty();
		if (party != null) {
			party.removeMember(player.getUniqueId());
			pseudoPlayer.setParty(null);
			party.sendMessage(player.getName() + " has left the party.");
			Output.positiveMessage(player, "You have left the party.");
		} else
			Output.simpleError(player, "You are not currently in a party.");
	}

	public PartyCommands(Lostshard plugin) {
		super(plugin, "party");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
		if (cmd.getName().equalsIgnoreCase("party")) {
			if (!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			final Player player = (Player) sender;
			if (args.length > 0) {
				final String secondaryCommand = args[0];
				if (secondaryCommand.equalsIgnoreCase("invite"))
					partyInvite(player, args);
				else if (secondaryCommand.equalsIgnoreCase("join"))
					partyJoin(player, args);
				else if (secondaryCommand.equalsIgnoreCase("leave"))
					partyLeave(player);
				else if (secondaryCommand.equalsIgnoreCase("info"))
					partyInfo(player);
				else if (secondaryCommand.equalsIgnoreCase("help")) {
					player.sendMessage(ChatColor.GOLD + "-Party Commands-");
					player.sendMessage(ChatColor.YELLOW + "/party invite (player name)");
					player.sendMessage(ChatColor.YELLOW + "/party join");
					player.sendMessage(ChatColor.YELLOW + "/party leave");
				}
			} else
				Output.simpleError(player, "Use \"/party help\" for commands.");

			return true;
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String string, String[] args) {
		if (cmd.getName().equalsIgnoreCase("party") && args.length == 1)
			return TabUtils.StringTab(args, "invite", "leave", "join", "info", "help");
		return TabUtils.empty();
	}

}
