package com.lostshard.Lostshard.NPC;

import java.util.UUID;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Lostshard.Manager.PlotManager;
import com.lostshard.Lostshard.NPC.NPCLib.NPCLibManager;
import com.lostshard.Lostshard.Objects.CustomObjects.SavableLocation;
import com.lostshard.Lostshard.Objects.Plot.Plot;

/**
 * @author Jacob Rosborg
 *
 */
@Embeddable
@Access(AccessType.PROPERTY)
public class NPC {

	PlotManager ptm = PlotManager.getManager();

	private int id;
	private NPCType type;
	private String name;
	private SavableLocation location;
	private int plotId;
	private UUID uuid = UUID.randomUUID();

	/**
	 * @param type
	 * @param name
	 * @param location
	 * @param plotId
	 */
	public NPC(NPCType type, String name, Location location, int plotId) {
		super();
		this.type = type;
		this.name = name;
		this.location = new SavableLocation(location);
		this.plotId = plotId;
	}

	/**
	 * Delete this npc
	 */
	public void fire() {
		final Plot plot = this.getPlot();
		plot.getNpcs().remove(this);
		delete();
		NPCLibManager.getManager().despawnNPC(this);
	}

	/**
	 * @return return npc id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * @return
	 */
	@Transient
	public Location getLocation() {
		return this.location.getLocation();
	}

	/**
	 * @return name of npc
	 */
	public String getName() {
		return this.name;
	}

	@Transient
	public Plot getPlot() {
		return this.ptm.getPlot(this.plotId);
	}

	/**
	 * @return
	 */
	public int getPlotId() {
		return this.plotId;
	}

	/**
	 * @return NPCType
	 */
	public NPCType getType() {
		return this.type;
	}

	/**
	 * @param move
	 *            npc to a location
	 */
	public void move(Location location) {
		NPCLibManager.getManager().moveNPC(this);
	}

	/**
	 * @param set
	 *            npc id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @param location
	 */
	public void setLocation(Location location) {
		this.location = new SavableLocation(location);
		this.getPlot().update();
	}

	/**
	 * @param set
	 *            name of npc
	 */
	public void setName(String name) {
		this.name = name;
		this.getPlot().update();
	}

	/**
	 * @param plotId
	 */
	public void setPlotId(int plotId) {
		this.plotId = plotId;
	}

	/**
	 * @param set
	 *            NPCType
	 */
	public void setType(NPCType type) {
		this.type = type;
		this.getPlot().update();
	}

	/**
	 * Spawns NPC in
	 */
	public void spawn() {
		NPCLibManager.getManager().spawnNPC(this);
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	@Transient
	public String getDisplayName() {
		return (getType().equals(NPCType.BANKER) ? "[BANKER] " : getType().equals(NPCType.GUARD) ? "[GUARD] " : getType().equals(NPCType.VENDOR) ? "[VENDOR] " : "") + getName();
	}

	public void teleport(Location location, TeleportCause reason) {
		NPCLibManager.getManager().teleportNPC(this.id, location, reason);
	}

	@Transient
	public net.citizensnpcs.api.npc.NPC getCitizensNPC() {
		return NPCLibManager.getManager().getNPC(id);
	}
	
	public SavableLocation getSavableLocation() {
		return this.location;
	}

	public void setSavableLocation(SavableLocation savableLocation) {
		this.location = savableLocation;
	}
	
	public void save() {
		Session s = Lostshard.getSession();
		Transaction t = s.beginTransaction();
		t.begin();
		s.update(this);
		t.commit();
		s.close();
	}
	
	public void insert() {
		Session s = Lostshard.getSession();
		Transaction t = s.beginTransaction();
		t.begin();
		s.save(this);
		t.commit();
		s.close();
	}
	
	public void delete() {
		Session s = Lostshard.getSession();
		Transaction t = s.beginTransaction();
		t.begin();
		s.delete(this);
		t.commit();
		s.close();
	}
}
