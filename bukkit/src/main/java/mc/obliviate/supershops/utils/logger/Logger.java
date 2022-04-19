package mc.obliviate.supershops.utils.logger;

import org.bukkit.Bukkit;

public class Logger {

    public static void error(String message) {
        Bukkit.getLogger().severe("[SuperShopsGUI] " + message);
    }

}
