package com.lostshard.lostshard.Objects.Store;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

public class Store {

	private int id;
	private int npcId;
	private List<StoreItem> items = new ArrayList<StoreItem>();
	
	public Store(int npcID) {
		this.npcId = npcID;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getNpcId() {
		return npcId;
	}
	
	public void setNpcId(int npcId) {
		this.npcId = npcId;
	}
	
	public List<StoreItem> getItemsForSale() {
		List<StoreItem> rs = new ArrayList<StoreItem>();
		for(StoreItem item : items) {
			if(item.getSalePrice() > 0)
				rs.add(item);
		}
		return rs;
	}
	
	public List<StoreItem> getItemsForBuy() {
		List<StoreItem> rs = new ArrayList<StoreItem>();
		for(StoreItem item : items) {
			if(item.getBuyPrice() > 0)
				rs.add(item);
		}
		return rs;
	}
	
	public List<StoreItem> getItems() {
		return items;
	}
	
	public void setItems(List<StoreItem> items) {
		this.items = items;
	}
	
	public boolean itemsContains(ItemStack item) {
		for(StoreItem si : items) {
			if(si.equals(item))
				return true;
		}
		return false;
	}
	public StoreItem getStoreItem(ItemStack item) {
		for(StoreItem si : items) {
			if(si.equals(item))
				return si;
		}
		return null;
	}
}
