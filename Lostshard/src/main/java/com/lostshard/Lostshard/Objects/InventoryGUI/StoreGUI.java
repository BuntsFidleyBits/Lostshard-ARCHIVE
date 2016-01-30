package com.lostshard.Lostshard.Objects.InventoryGUI;

import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Objects.Store.Store;
import com.lostshard.Lostshard.Objects.Store.StoreItem;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class StoreGUI extends GUI {
	public StoreGUI(PseudoPlayer pPlayer, Store store) {
		super("Store", pPlayer, new GUIItem[0]);
		GUIItem[] items = new GUIItem[store.getItems().size()];
		for (int i = 0; i < store.getItems().size(); i++) {
			StoreItem si = (StoreItem) store.getItems().get(i);
			ItemStack item = si.getItem().clone();
			ItemMeta itemMeta = item.getItemMeta();
			List<String> lore = new ArrayList<String>();
			if (si.getStock() < 0) {
				itemMeta.setDisplayName(ChatColor.RED + (item.getItemMeta().hasDisplayName() ? 
						item.getItemMeta().getDisplayName() : item.getType().name()));
			} else {
				itemMeta.setDisplayName(ChatColor.GREEN + (item.getItemMeta().hasDisplayName() ? 
						item.getItemMeta().getDisplayName() : item.getType().name()));
			}
			lore.add("Sale price: " + (si.getSalePrice() > 0 ? Integer.valueOf(si.getSalePrice()) : "not for sale"));
			lore.add("Buy price: " + (si.getSalePrice() > 0 ? Integer.valueOf(si.getSalePrice()) : "not for buy"));
			lore.add("Stock: " + si.getStock());
			lore.add("ID: "+(i+1));
			itemMeta.setLore(lore);

			item.setItemMeta(itemMeta);
			items[i] = new GUIItem(item, new GUIClick() {
				public void click(Player player, PseudoPlayer pPlayer, ItemStack item, ClickType click, Inventory inv,
						int slot) {
				}
			});
		}
		setItems(items);
	}
}
