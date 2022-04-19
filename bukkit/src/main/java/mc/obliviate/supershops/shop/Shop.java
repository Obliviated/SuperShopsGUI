package mc.obliviate.supershops.shop;

import mc.obliviate.supershops.shop.category.Category;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Shop {

    private static final Map<String, Shop> shops = new HashMap<>();
    private static final String shopConfigName = "shop.yml";
    private final Map<String, Category> categories = new HashMap<>();
    private final String shopName;
    private final String shopDisplayName;

    private Shop(String shopName, String shopDisplayName) {
        this.shopName = shopName;
        this.shopDisplayName = shopDisplayName;
        shops.put(this.shopName, this);
    }

    public static Shop deserialize(File file) {
        final YamlConfiguration shopConfig = YamlConfiguration.loadConfiguration(new File(file.getPath() + File.separator + shopConfigName));

        final Shop shop = new Shop(file.getName(), shopConfig.getString("shop-name", "unknown-shop"));

        for (File category : Objects.requireNonNull(file.listFiles())) {
            if (category.getName().endsWith(".yml") && !category.getName().equalsIgnoreCase(shopConfigName)) {
                Bukkit.broadcastMessage("category serializing: " + category.getPath());
                shop.registerCategory(Category.deserialize(shop, YamlConfiguration.loadConfiguration(category)));
            }
        }

        return shop;
    }

    public void registerCategory(final Category category) {
        categories.put(category.getName(), category);
    }

    public Map<String, Category> getCategories() {
        return categories;
    }

    public static Map<String, Shop> getShops() {
        return shops;
    }

    public String getShopDisplayName() {
        return shopDisplayName;
    }

    public String getShopName() {
        return shopName;
    }
}
