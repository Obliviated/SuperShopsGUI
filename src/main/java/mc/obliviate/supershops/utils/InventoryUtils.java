package mc.obliviate.supershops.utils;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryUtils {

	public static int countSpaces(Inventory inventory) {
		int total = 0;
		for (ItemStack i : inventory.getStorageContents()) {
			if (i == null || i.getType().equals(Material.AIR)) {
				total++;
			}
		}
		return total * 64; //64 item for each empty slot
	}

	public static int countItemAmount(ItemStack item, ItemStack... items) {
		int total = 0;
		for (ItemStack i : items) {
			if (item.isSimilar(i)) {
				total += i.getAmount();
			}
		}
		return total;
	}

	public static void giveItem(ItemStack item, int amount, Inventory inventory) {
		item.setAmount(amount);
		inventory.addItem(item);
	}

	public static void removeItem(ItemStack item, int amount, Inventory inventory) {
		for (final ItemStack invItem : inventory.getContents()) {
			if (amount <= 0) return;
			if (invItem == null) continue;
			if (invItem.isSimilar(item)) {
				int req = Math.min(Math.min(64, amount), invItem.getAmount());
				if (invItem.getAmount() >= req) {
					invItem.setAmount(invItem.getAmount() - req);
					amount -= req;
				}
			}
		}
	}

}
