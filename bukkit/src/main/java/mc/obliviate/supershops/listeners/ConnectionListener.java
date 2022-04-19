package mc.obliviate.supershops.listeners;

import mc.obliviate.supershops.SuperShopsGUI;
import mc.obliviate.supershops.user.ShopUser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {

    private final SuperShopsGUI plugin;

    public ConnectionListener(SuperShopsGUI plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onConnect(PlayerJoinEvent e) {
        new ShopUser(e.getPlayer().getUniqueId(), 1);
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent e) {
        ShopUser.getUserMap().remove(e.getPlayer().getUniqueId());
    }

}
