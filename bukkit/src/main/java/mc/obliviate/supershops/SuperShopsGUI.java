package mc.obliviate.supershops;

import mc.obliviate.inventory.InventoryAPI;
import mc.obliviate.supershops.commands.ShopCommand;
import mc.obliviate.supershops.handlers.ConfigHandler;
import mc.obliviate.supershops.handlers.GUISerializerHandler;
import mc.obliviate.supershops.listeners.ConnectionListener;
import me.despical.commandframework.CommandFramework;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class SuperShopsGUI extends JavaPlugin {

    private static Economy economy;
    private final ConfigHandler configHandler = new ConfigHandler(this);
    private final GUISerializerHandler guiSerializerHandler = new GUISerializerHandler(this);
    private final InventoryAPI inventoryAPI = new InventoryAPI(this);
    private CommandFramework commandFramework;

    public static Economy getEconomy() {
        return economy;
    }

    @Override
    public void onEnable() {
        configHandler.init();
        guiSerializerHandler.init();
        inventoryAPI.init();
        commandFramework = new CommandFramework(this);
        commandFramework.registerCommands(new ShopCommand());
        setupEconomy();

        Bukkit.getPluginManager().registerEvents(new ConnectionListener(this), this);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        final RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }
}
