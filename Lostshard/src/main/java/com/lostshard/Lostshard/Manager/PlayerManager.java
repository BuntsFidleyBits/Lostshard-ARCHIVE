package com.lostshard.Lostshard.Manager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.lostshard.Lostshard.Database.Mappers.PlayerMapper;
import com.lostshard.Lostshard.Objects.PseudoPlayer;
import com.lostshard.Lostshard.Utils.Utils;

public class PlayerManager {

	public static PlayerManager getManager() {
		return manager;
	}

	static PlayerManager manager = new PlayerManager();

	private List<PseudoPlayer> players = new ArrayList<PseudoPlayer>();

	public PseudoPlayer getPlayer(OfflinePlayer player) {
		return this.getPlayer(player.getUniqueId());
	}
	
	public PseudoPlayer getPlayer(OfflinePlayer player, boolean create) {
		return this.getPlayer(player.getUniqueId(), create);
	}

	public PseudoPlayer getPlayer(Player player) {
		return this.getPlayer(player.getUniqueId());
	}

	public PseudoPlayer getPlayer(Player player, boolean create) {
		return this.getPlayer(player.getUniqueId(), create);
	}
	
	public PseudoPlayer getPlayer(UUID uuid, boolean create) {
		if (uuid == null)
			return null;
		for (final PseudoPlayer pPlayer : this.players)
			if (pPlayer.getPlayerUUID().equals(uuid))
				return pPlayer;
		PseudoPlayer pPlayer = PlayerMapper.getPlayer(uuid);
		if (pPlayer == null && create)
			pPlayer = PlayerMapper.insertPlayer(new PseudoPlayer(uuid, 1));
		return pPlayer;
	}

	public PseudoPlayer getPlayer(UUID uuid) {
		return getPlayer(uuid, false);
	}
	
	public List<PseudoPlayer> getPlayers() {
		return this.players;
	}

	public PseudoPlayer onPlayerLogin(PlayerJoinEvent event) {
		final Player player = event.getPlayer();
		final PseudoPlayer pPlayer = this.getPlayer(player, true);
		this.players.add(pPlayer);
		player.setDisplayName(Utils.getDisplayName(player));
		return pPlayer;
	}

	public void onPlayerQuit(PlayerQuitEvent event) {
		final PseudoPlayer pPlayer = this.getPlayer(event.getPlayer());
		if(pPlayer.getParty() != null) {
			pPlayer.getParty().removeMember(event.getPlayer().getUniqueId());
			pPlayer.getParty().sendMessage(event.getPlayer().getName() + " has left the party.");
		}
		System.out.println("updateing on quit");
		PlayerMapper.updatePlayer(pPlayer);
		System.out.println("updated on quit");
		this.players.remove(pPlayer);
	}

	public void setPlayers(List<PseudoPlayer> players) {
		this.players = players;
	}

	public void tick(double delta, long tick) {
		for (final PseudoPlayer pPlayer : this.players)
			pPlayer.tick(delta, tick);
	}
}
