package mc.obliviate.supershops.handlers;

import mc.obliviate.inventory.GUI;
import mc.obliviate.supershops.SuperShopsGUI;
import mc.obliviate.supershops.shop.Shop;
import org.bukkit.Bukkit;

import java.io.File;

public class GUISerializerHandler implements Handler {

	private static final String shopDirectory = "shops";
	private static final String categoryDirectory = "categories";
	private final SuperShopsGUI plugin;

	public GUISerializerHandler(SuperShopsGUI plugin) {
		this.plugin = plugin;
	}

	@Override
	public void init() {
		saveDefaultShops();
		loadShops();

	}

	private void saveDefaultShops() {
		final File shopFile = new File(plugin.getDataFolder() + File.separator + shopDirectory);
		if (shopFile.exists()) return;
		plugin.saveResource("shops" + File.separator + "default" + File.separator + "shop.yml",false);
		plugin.saveResource("shops" + File.separator + "default" + File.separator + "category-1.yml",false);
	}

	public void loadShops() {
		final File shopFile = new File(plugin.getDataFolder() + File.separator + shopDirectory);
		if (!shopFile.exists() || shopFile.listFiles() == null) return;
		for (final File file : shopFile.listFiles()) {
			Bukkit.broadcastMessage("shop serializing: " + file.getPath());
			Shop.deserialize(file);
		}
	}


	public void putGUIItems(GUI gui) {

	}

}
