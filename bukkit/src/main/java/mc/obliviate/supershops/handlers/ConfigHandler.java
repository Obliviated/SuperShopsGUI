package mc.obliviate.supershops.handlers;

import mc.obliviate.supershops.SuperShopsGUI;
import mc.obliviate.supershops.utils.message.MessageUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigHandler implements Handler {

    private static final String CONFIG_FILE_NAME = "config.yml";
    private static final String MESSAGES_FILE_NAME = "messages.yml";
    private static YamlConfiguration config;
    private static YamlConfiguration messages;
    private final SuperShopsGUI plugin;

    public ConfigHandler(SuperShopsGUI plugin) {
        this.plugin = plugin;
    }

    public static YamlConfiguration getConfig() {
        return config;
    }

    public static YamlConfiguration getMessages() {
        return messages;
    }

    @Override
    public void init() {
        final File configFile = new File(plugin.getDataFolder() + File.separator + CONFIG_FILE_NAME);
        final File messagesFile = new File(plugin.getDataFolder() + File.separator + MESSAGES_FILE_NAME);

        validateFile(configFile);
        validateFile(messagesFile);

        config = YamlConfiguration.loadConfiguration(configFile);
        messages = YamlConfiguration.loadConfiguration(messagesFile);

        MessageUtils.setMessageConfig(messages);

    }

    private void validateFile(final File file) {
        if (file.exists() || YamlConfiguration.loadConfiguration(file).getKeys(false).isEmpty()) {
            plugin.saveResource(file.getName(), false);
        }
    }

}
