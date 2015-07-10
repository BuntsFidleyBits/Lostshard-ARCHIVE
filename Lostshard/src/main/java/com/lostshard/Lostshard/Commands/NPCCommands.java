package com.lostshard.Lostshard.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.Lostshard.Manager.NPCManager;
import com.lostshard.Lostshard.Manager.StoreManager;
import com.lostshard.Lostshard.NPC.NPC;
import com.lostshard.Lostshard.Objects.Store.Store;
import com.lostshard.Lostshard.Objects.Store.StoreItem;
import com.lostshard.Lostshard.Utils.Output;

public class NPCCommands {

	static NPCManager npcm = NPCManager.getManager();
	static StoreManager sm = StoreManager.getManager();

	
	
	public boolean onCommand(CommandSender sender, Command cmd, String string,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("vendor")) {
			if (!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			if (args.length < 1) {
				Output.simpleError(sender,
						"/vendor (list|buy|sell|add|remove|)");
				return true;
			}
			final Player player = (Player) sender;
			final String subCommand = args[0];
			if (subCommand.equalsIgnoreCase("list"))
				this.vendorList(player);
			else if (subCommand.equalsIgnoreCase("buy"))
				this.vendorBuy(player, args);
			else if (subCommand.equalsIgnoreCase("sell"))
				this.vendorSell(player, args);
			else if (subCommand.equalsIgnoreCase("remove"))
				this.vendorRemove(player, args);
			else if (subCommand.equalsIgnoreCase("add"))
				this.vendorAdd(player, args);
			return true;
		}
		return false;
	}

	private void vendorAdd(Player player, String[] args) {
		// Get nearest vendor
		final NPC npc = npcm.getVendor(player.getLocation());
		if (npc == null) {
			Output.simpleError(player, "Theres no vendors near you");
			return;
		}

		// Check command length
		if (args.length < 4 && args.length > 2) {
			Output.simpleError(player, "/npc add (Price) (Amount) (buy|sell)");
			return;
		}

		// Get store
		Store store = sm.getStore(npc);
		if (store == null)
			store = new Store(npc.getId());

		// Get price and amount from command
		int price = 0;
		int amount = 0;
		try {
			price = Integer.parseInt(args[1]);
			amount = Integer.parseInt(args[2]);
		} catch (final Exception e) {

		}

		// Check if amount and price != 0
		if (price == 0 || amount == 0) {
			Output.simpleError(player, "Error");
			return;
		}

		// Get store item if exists and if not create one
		final ItemStack item = player.getItemInHand().clone();
		StoreItem storeItem = store.getStoreItem(item);
		if (storeItem == null) {
			storeItem = new StoreItem(item);
			item.setAmount(1);
			storeItem.setItem(item);
		}
		// For sell
		if (args[3].equalsIgnoreCase("sell")) {
			if (!player.getInventory().contains(item.getType(), amount)) {
				Output.simpleError(player, "Error");
				return;
			}
			storeItem.setStock(amount);
			storeItem.setSalePrice(price);
		}// For buy
		else if (args[3].equalsIgnoreCase("buy")) {
			storeItem.setMaxBuyAmount(amount);
			storeItem.setBuyPrice(price);
		}
	}

	private void vendorBuy(Player player, String[] args) {
		// TODO Auto-generated method stub

	}

	private void vendorList(Player player) {
		final NPC npc = npcm.getVendor(player.getLocation());
		if (npc == null) {
			Output.simpleError(player, "Theres no vendors near you");
			return;
		}
		final Store store = sm.getStore(npc);
		int currentItem = 0;
		// Display items for sale
		if (!store.getItemsForSale().isEmpty())
			Output.positiveMessage(player, "-" + npc.getName()
					+ "'s Items for sale-");
		for (final StoreItem item : store.getItemsForSale()) {
			currentItem++;
			player.sendMessage(ChatColor.YELLOW + "" + currentItem + ". "
					+ item.getItem().getType().toString() + "(price: "
					+ item.getBuyPrice() + ") (In Stock: " + item.getStock()
					+ ")");
		}
		// Display items for buy
		if (!store.getItemsForBuy().isEmpty())
			Output.positiveMessage(player, "-" + npc.getName()
					+ "'s Items for buy-");
		for (final StoreItem item : store.getItemsForBuy()) {
			currentItem++;
			player.sendMessage(ChatColor.YELLOW + "" + currentItem + ". "
					+ item.getItem().getType().toString() + "(price: "
					+ item.getBuyPrice() + ") (In Stock: " + item.getStock()
					+ "/" + item.getMaxBuyAmount() + ")");
		}
	}

	private void vendorRemove(Player player, String[] args) {
		// TODO Auto-generated method stub

	}

	private void vendorSell(Player player, String[] args) {
		// TODO Auto-generated method stub

	}

}
