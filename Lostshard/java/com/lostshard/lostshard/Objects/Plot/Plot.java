package com.lostshard.lostshard.Objects.Plot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Data.Variables;
import com.lostshard.lostshard.Database.Database;
import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Manager.PlotManager;
import com.lostshard.lostshard.NPC.NPC;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Utils.Serializer;

/**
 * @author Jacob Rosborg
 *
 */
public class Plot {

	PlotManager ptm = PlotManager.getManager();
	PlayerManager pm = PlayerManager.getManager();
	
	// String's
	private String name;

	// Int's
	private int id;
	private int size = 10;
	private int money = 0;
	private int salePrice = 0;

	// Array's
	private List<UUID> friends = new ArrayList<UUID>();
	private List<UUID> coowners = new ArrayList<UUID>();
	// UUID
	private UUID owner;

	// Boolean's
	private boolean protection = true;

	private boolean allowExplosions = false;
	private boolean privatePlot = true;
	private boolean friendBuild = false;
	private boolean update = false;
	private boolean titleEntrence = false;
	// Upgrade's
	private List<PlotUpgrade> upgrades = new ArrayList<PlotUpgrade>();

	// Location
	private Location location;

	// NPCS
	private ArrayList<NPC> npcs = new ArrayList<NPC>();

	// Admin
	private boolean allowMagic = true;
	private boolean allowPvp = true;

	public Plot(int id, String name, UUID owner, Location location) {
		super();
		this.name = name;
		this.id = id;
		this.owner = owner;
		this.location = location;
	}

	// Getters and Setters

	public void addCoowner(Player player) {
		coowners.add(player.getUniqueId());
		update();
	}

	// Friends
	public void addFriend(Player player) {
		friends.add(player.getUniqueId());
		update();
	}

	public void addMoney(int money) {
		this.money += money;
		update();
	}

	public void addUpgrade(PlotUpgrade upgrade) {
		this.upgrades.add(upgrade);
		update();
	}

	public void disband() {
		for(NPC npc : npcs)
			Database.deleteNPC(npc);
		ptm.removePlot(this);
	}

	public void expandSize(int size) {
		this.size += size;
		update();
	}

	public List<UUID> getCoowners() {
		return coowners;
	}

	/**
	 * @author Frank Oliver
	 * @param toSize
	 * @return Expand price from current size to size.
	 */
	public int getExpandPrice(int toSize) {
		return (int) Math.abs((((Math.pow((toSize-1), 2)+toSize-1)/2)-((Math.pow(size, 2)+size)/2))*Variables.plotExpandPrice+100);
	}

	public List<UUID> getFriends() {
		return friends;
	}

	public int getId() {
		return id;
	}

	public Location getLocation() {
		return location;
	}

	public int getMaxVendors() {
		return size / 15;
	}

	public int getMoney() {
		return money;
	}

	public String getName() {
		return name;
	}

	public ArrayList<NPC> getNpcs() {
		return npcs;
	}

	public UUID getOwner() {
		return owner;
	}

	public String getPlayerStatusOfPlotString(Player player) {
		return isOwner(player) ? "You are the owner of this plot."
				: isCoowner(player) ? "You are a co-owner of this plot."
						: isFriend(player) ? "You are a friend of this plot."
								: "You are not friend of this plot.";
	}

	public int getSalePrice() {
		return salePrice;
	}

	public int getSize() {
		return size;
	}

	public int getTax() {
		return size*10;
	}

	public List<PlotUpgrade> getUpgrades() {
		return upgrades;
	}

	public int getValue() {
		int plotValue = (int) Math.floor(Math.abs((((Math.pow(size-1, 2)+size-1)/2-45)*Variables.plotExpandPrice)));
		if (isUpgrade(PlotUpgrade.TOWN))
			plotValue += Variables.plotTownPrice;
		if (isUpgrade(PlotUpgrade.DUNGEON))
			plotValue += Variables.plotDungeonPrice;
		if (isUpgrade(PlotUpgrade.AUTOKICK))
			plotValue += Variables.plotAutoKickPrice;
		if (isUpgrade(PlotUpgrade.NEUTRALALIGNMENT))
			plotValue += Variables.plotNeutralAlignmentPrice;

		if (this.getLocation().getWorld().getEnvironment()
				.equals(Environment.NETHER))
			return plotValue;
		return (int) Math.floor(plotValue * .75);
	}

	public boolean isAllowedToBuild(Player player) {
		return isAllowedToBuild(player.getUniqueId());
	}

	public boolean isAllowedToBuild(UUID uuid) {
		PseudoPlayer pPlayer = PlayerManager.getManager().getPlayer(uuid);
		if(pPlayer.getTestPlot() != null && pPlayer.getTestPlot() == this)
			return false;
		return  isCoownerOrAbove(uuid) ? true : isFriend(uuid)
				&& isFriendBuild() ? true : !isProtected() ? true : false;
	}

	public boolean isAllowedToInteract(Player player) {
		return isAllowedToInteract(player.getUniqueId());
	}

	public boolean isAllowedToInteract(UUID uuid) {
		PseudoPlayer pPlayer = PlayerManager.getManager().getPlayer(uuid);
		if(pPlayer.getTestPlot() != null && pPlayer.getTestPlot() == this)
			return false;
		return  isFriendOrAbove(uuid) ? true : false;
	}

	public boolean isAllowExplosions() {
		return allowExplosions;
	}

	public boolean isAllowMagic() {
		return allowMagic;
	}

	public boolean isAllowPvp() {
		return allowPvp;
	}

	// Coowners
	public boolean isCoowner(Player player) {
		return coowners.contains(player.getUniqueId());
	}

	public boolean isCoowner(UUID uuid) {
		return coowners.contains(uuid);
	}

	public boolean isCoownerOrAbove(Player player) {
		return isCoownerOrAbove(player.getUniqueId());
	}

	public boolean isCoownerOrAbove(UUID uuid) {
		return isOwner(uuid) ? true : isCoowner(uuid) ? true : false;
	}

	public boolean isForSale() {
		return (this.salePrice > 0) ? true : false;
	}

	public boolean isFriend(Player player) {
		return friends.contains(player.getUniqueId());
	}

	public boolean isFriend(UUID uuid) {
		return friends.contains(uuid);
	}

	public boolean isFriendBuild() {
		return friendBuild;
	}

	public boolean isFriendOrAbove(Player player) {
		return isFriendOrAbove(player.getUniqueId());
	}
	
	public boolean isFriendOrAbove(UUID uuid) {
		return isOwner(uuid) ? true : isCoowner(uuid) ? true
				: isFriend(uuid) ? true : false;
	}

	public boolean isOwner(Player player) {
		return isOwner(player.getUniqueId());
	}

	public boolean isOwner(UUID uuid) {
		return uuid.equals(owner);
	}
	
	public boolean isPrivatePlot() {
		return privatePlot;
	}

	public boolean isProtected() {
		return protection;
	}

	public boolean isTitleEntrence() {
		return titleEntrence;
	}

	public boolean isUpdate() {
		return update;
	}

	public boolean isUpgrade(PlotUpgrade upgrade) {
		return this.upgrades.contains(upgrade);
	}

	// Getting next id
	public int nextId() {
		int nextId = 0;
		for (Plot p : ptm.getPlots())
			if (p.getId() > nextId)
				nextId = p.getId();
		return nextId + 1;
	}

	public void removeCoowner(Player player) {
		coowners.remove(player.getUniqueId());
		update();
	}

	public void removeFriend(Player player) {
		friends.remove(player.getUniqueId());
		update();
	}

	public void removeUpgrade(PlotUpgrade upgrade) {
		this.upgrades.remove(upgrade);
		update();
	}

	public void setAllowExplosions(boolean allowExplosions) {
		this.allowExplosions = allowExplosions;
		update();
	}

	public void setAllowMagic(boolean allowMagic) {
		this.allowMagic = allowMagic;
		update();
	}

	public void setAllowPvp(boolean allowPvp) {
		this.allowPvp = allowPvp;
		update();
	}
	
	public void setCoowners(List<UUID> coowners) {
		this.coowners = coowners;
	}

	public void setFriendBuild(boolean friendBuild) {
		this.friendBuild = friendBuild;
		update();
	}
	
	public void setFriends(List<UUID> friends) {
		this.friends = friends;
	}

	public void setId(int id) {
		this.id = id;
		update();
	}

	public void setLocation(Location location) {
		this.location = location;
		update();
	}

	public void setMoney(int money) {
		this.money = money;
		update();
	}

	public void setName(String name) {
		this.name = name;
		update();
	}
	
	public void setNpcs(ArrayList<NPC> npcs) {
		this.npcs = npcs;
	}

	public void setOwner(UUID owner) {
		this.owner = owner;
		update();
	}

	public void setPrivatePlot(boolean privatePlot) {
		this.privatePlot = privatePlot;
		update();
	}

	public void setProtected(boolean protection) {
		this.protection = protection;
		update();
	}

	public void setSalePrice(int salePrice) {
		this.salePrice = salePrice;
		update();
	}
	
	public void setSize(int size) {
		this.size = size;
		update();
	}
	
	public void setTitleEntrence(boolean titleEntrence) {
		this.titleEntrence = titleEntrence;
		update();
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}
	
	public void setUpgrades(List<PlotUpgrade> upgrades) {
		this.upgrades = upgrades;
	}

	public void shrinkSize(int size) {
		this.size -= size;
		update();
	}

	public void subtractMoney(int money) {
		this.money -= money;
		update();
	}
	
	public void update() {
		this.update = true;
	}

	public String upgradesToJson() {
		List<String> tjson = new ArrayList<String>();
		for(PlotUpgrade upgrade : upgrades)
			tjson.add(upgrade.name());
		return Serializer.serializeStringArray(tjson);
	}
}
