package mc.obliviate.supershops.product;

import mc.obliviate.supershops.SuperShopsGUI;
import mc.obliviate.supershops.handlers.ConfigHandler;
import mc.obliviate.supershops.utils.InventoryUtils;
import mc.obliviate.supershops.utils.ItemBuilder;
import mc.obliviate.supershops.utils.logger.Logger;
import mc.obliviate.supershops.utils.message.MessageUtils;
import mc.obliviate.supershops.utils.message.placeholder.PlaceholderUtil;
import mc.obliviate.supershops.utils.translateutils.TranslateUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemStackProduct implements ShopProduct {

    private final ItemStack item;
    private final double sellPrice;
    private final double buyPrice;
    private final boolean sellable;
    private final boolean buyable;

    public ItemStackProduct(ItemStack item, double sellPrice, double buyPrice, boolean sellable, boolean buyable) {
        this.item = item;
        this.sellPrice = sellPrice;
        this.buyPrice = buyPrice;
        this.sellable = sellable;
        this.buyable = buyable;

    }

    public static ItemStackProduct deserialize(ConfigurationSection section) {
        Bukkit.getLogger().info("item deserialzing: " + section.getName());
        final ItemStack item = deserializeItemStack(section.getConfigurationSection("item-stack"));
        final int sellPrice = section.getInt("sell-price", -1);
        final int buyPrice = section.getInt("buy-price", -1);
        final boolean sellable = section.getBoolean("sellable", false);
        final boolean buyable = section.getBoolean("buyable", false);
        if (sellable && sellPrice < 0) {
            Logger.error("Product sell price is invalid. (" + section.getName() + ")");
            return null;
        }
        if (buyable && buyPrice < 0) {
            Logger.error("Product buy price is invalid. (" + section.getName() + ")");
            return null;
        }
        return new ItemStackProduct(item, sellPrice, buyPrice, sellable, buyable);

    }


    private static ItemStack deserializeItemStack(ConfigurationSection section) {
        final ItemStack item = findMaterial(section);
        final ItemMeta meta = item.getItemMeta();
        applyLore(section, meta);
        applyName(section, meta);
        applyEnchantments(section, meta);
        applyFlags(section, meta);

        item.setItemMeta(meta);
        return item;

    }

    private static void applyFlags(ConfigurationSection section, ItemMeta meta) {
        final List<String> flags = section.getStringList("flags");
        if (flags.isEmpty()) return;
        for (String flagName : flags) {
            try {
                meta.addItemFlags(ItemFlag.valueOf(flagName));
            } catch (IllegalArgumentException exception) {
                Bukkit.getLogger().severe("ItemFlag cannot found: " + flagName + " at " + productName(section));
                continue;
            }
        }
    }

    private static void applyEnchantments(ConfigurationSection section, ItemMeta meta) {
        final List<String> enchantmentList = section.getStringList("enchantments");
        if (enchantmentList.isEmpty()) return;
        for (final String enchantmentString : enchantmentList) {
            final String[] data = enchantmentString.replace(" ", "").split(":");

            final Enchantment enchantment = Enchantment.getByName(data[0]);
            final int level;
            try {
                level = Integer.parseInt(data[1]);
            } catch (NumberFormatException exception) {
                Bukkit.getLogger().severe("Enchantment level cannot parsed as integer: " + data[1] + "at" + productName(section));
                continue;
            }
            if (enchantment == null) {
                Bukkit.getLogger().severe("Enchantment cannot found: " + data[0] + "at" + productName(section));
                continue;
            }

            meta.addEnchant(enchantment, level, true);
        }
    }

    private static void applyName(ConfigurationSection section, ItemMeta meta) {
        final String name = section.getString("name");
        if (name == null) return;
        meta.setDisplayName(MessageUtils.parseColor(name));
    }

    private static void applyLore(ConfigurationSection section, ItemMeta meta) {
        final List<String> lore = section.getStringList("lore");
        if (lore.isEmpty()) return;
        meta.setLore(MessageUtils.parseColor(lore));
    }

    private static ItemStack findMaterial(ConfigurationSection section) {
        final String materialName = section.getString("material-type");
        if (materialName == null)
            throw new IllegalArgumentException("Material type is not configured: " + productName(section));
        final Material material = Material.getMaterial(materialName);
        if (material == null) throw new IllegalArgumentException("Material type cannot found: " + materialName);
        return new ItemStack(material);
    }

    public static String productName(ConfigurationSection section) {
        return section.getName();
    }

    public static List<String> formatLore(PlaceholderUtil placeholderUtil, String type) {
        return MessageUtils.parseColor(
                MessageUtils.applyPlaceholders(
                        ConfigHandler.getConfig().getStringList(
                                "icons." + type + ".lore"), placeholderUtil));
    }

    @Override
    public void sell(Player player, int amount) {
        if (!validate(player, amount)) return;
        if (!sellable) return;

        final int totalAmount = InventoryUtils.countItemAmount(getItem(), player.getInventory().getContents());
        amount = Math.min(totalAmount, amount);

        if (totalAmount <= 0) {
            MessageUtils.sendMessage(player, "you-dont-have-enough-item");
            return;
        }

        final double totalPrice = amount * sellPrice;

        InventoryUtils.removeItem(getItem(), amount, player.getInventory());

        String message = MessageUtils.getMessage("successfully-sold");
        assert message != null;
        if (message.contains("{item}")) {
            player.spigot().sendMessage(TranslateUtils.translateText(message, item.getType(), new PlaceholderUtil().add("{money}", totalPrice + "")));
        } else {
            MessageUtils.sendMessage(player, "successfully-purchased", new PlaceholderUtil().add("{money}", totalPrice + ""));
        }
        SuperShopsGUI.getEconomy().depositPlayer(player, totalPrice);
    }

    @Override
    public void buy(Player player, int amount) {
        if (!validate(player, amount)) return;
        if (!buyable) return;

        final int space = InventoryUtils.countSpaces(player.getInventory());

        amount = Math.min(space, amount);

        if (amount <= 0) {
            MessageUtils.sendMessage(player, "you-dont-have-space");
            return;
        }

        final double totalPrice = amount * sellPrice;
        final boolean isTransactionSuccess = SuperShopsGUI.getEconomy().withdrawPlayer(player, totalPrice).transactionSuccess();

        if (isTransactionSuccess) {
            InventoryUtils.giveItem(getItem(), amount, player.getInventory());

            String message = MessageUtils.getMessage("successfully-purchased");
            assert message != null;
            if (message.contains("{item}")) {
                player.spigot().sendMessage(TranslateUtils.translateText(message, item.getType(), new PlaceholderUtil().add("{money}", totalPrice + "")));
            } else {
                MessageUtils.sendMessage(player, "successfully-purchased", new PlaceholderUtil().add("{money}", totalPrice + ""));
            }
        } else {
            MessageUtils.sendMessage(player, "you-cannot-afford");
        }

    }

    @Override
    public ItemStack getIconItem(int amount) {
        final PlaceholderUtil placeholderUtil = new PlaceholderUtil()
                .add("{amount}", amount + "")
                .add("{buy-price}", (buyPrice * amount) + "")
                .add("{sell-price}", (sellPrice * amount) + "");

        final ItemBuilder builder = new ItemBuilder(getItem());
        if (sellable && buyable) {
            builder.appendLore(formatLore(placeholderUtil, "buyable-and-sellable"));
        } else if (!sellable && buyable) {
            builder.appendLore(formatLore(placeholderUtil, "buy-only"));
        } else if (sellable) { //unbuyable
            builder.appendLore(formatLore(placeholderUtil, "sell-only"));
        } else {
            builder.appendLore(formatLore(placeholderUtil, "dysfunctional"));
        }
        return builder.build();
    }

    public ItemStack getItem() {
        return item.clone();
    }

}
