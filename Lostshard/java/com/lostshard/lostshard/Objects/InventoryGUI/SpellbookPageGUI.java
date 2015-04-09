package com.lostshard.lostshard.Objects.InventoryGUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Objects.SpellBook;
import com.lostshard.lostshard.Spells.Scroll;
import com.lostshard.lostshard.Utils.Utils;

public class SpellbookPageGUI extends InventoryGUI {

	private int page;
	
	public SpellbookPageGUI(PseudoPlayer pPlayer, int page) {
		super(9, "Spellbook page: "+page, GUIType.SPELLBOOKPAGE, pPlayer);
		this.page = page;
		optionSelectorOveride();
	}

	public void optionSelectorOveride() {
		SpellBook spellbook = getPlayer().getSpellbook();
		ItemStack pageBack = new ItemStack(Material.BOOK);
		ItemMeta pageBackMeta = pageBack.getItemMeta();
		pageBackMeta.setDisplayName(ChatColor.GOLD+"Back to Spellbook.");
		pageBack.setItemMeta(pageBackMeta);
		addOption(pageBack);
		for(Scroll s : spellbook.getSpellsOnPage(page)) {
			ItemStack item = new ItemStack(s.getReagentCost().get(0).getType());
			ItemMeta itemMeta = item.getItemMeta();
			List<String> lore = new ArrayList<String>();
			itemMeta.setDisplayName(ChatColor.GOLD+s.getName());
			int magery = getPlayer().getCurrentBuild().getMagery().getLvl();
			if(magery < s.getMinMagery())
				lore.add(ChatColor.RED+"Minimum magery: "+Utils.scaledIntToString(s.getMinMagery()));
			else 
				lore.add(ChatColor.GREEN+"Minimum magery: "+Utils.scaledIntToString(s.getMinMagery()));
			lore.add(ChatColor.BLUE+"Mana cost: "+s.getManaCost());
			lore.add(ChatColor.LIGHT_PURPLE+"Reagent Cost");
			for(ItemStack i : s.getReagentCost())
				lore.add(i.getAmount()+" "+i.getType().name().toLowerCase().replace("_", " "));
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			addOption(item);
		}
	}

	@Override
	public void onClick(InventoryClickEvent event) {
		if(event.getCurrentItem() != null && event.getCurrentItem().getItemMeta().hasDisplayName()) {
			if(event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GOLD+"Back to Spellbook.")) {
				InventoryGUI spellbookGUI = new SpellbookGUI(getPlayer());
				spellbookGUI.openInventory((Player)event.getWhoClicked());
			}
		}
	}

	@Override
	public void onClose(InventoryCloseEvent event) {
		
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	@Override
	public void optionSelector() {
		
	}

}
