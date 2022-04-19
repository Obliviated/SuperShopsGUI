package mc.obliviate.supershops.utils.translateutils;

import mc.obliviate.supershops.utils.message.MessageUtils;
import mc.obliviate.supershops.utils.message.placeholder.PlaceholderUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.Material;

public class TranslateUtils {

    public static String getKey(Material material) {
        if (material.isBlock())
            return ("block.minecraft." + material.getKey().getKey());
        return ("item.minecraft." + material.getKey().getKey());
    }

    public static BaseComponent[] translateText(String message, Material material, PlaceholderUtil placeholderUtil) {
        final String key = TranslateUtils.getKey(material);

        message = MessageUtils.applyPlaceholders(message, placeholderUtil);
        message = MessageUtils.parseColor(message);

        final String[] splittedMessage = message.split("\\{item}");

        return new BaseComponent[]{new TextComponent(splittedMessage[0]), new TranslatableComponent(key), new TextComponent(splittedMessage[1])};

    }
}
