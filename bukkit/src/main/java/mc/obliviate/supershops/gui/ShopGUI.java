package mc.obliviate.supershops.gui;

import mc.obliviate.inventory.GUI;
import mc.obliviate.inventory.Icon;
import mc.obliviate.supershops.shop.Shop;
import mc.obliviate.supershops.shop.category.Category;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class ShopGUI extends GUI {

    private final Shop shop;

    public ShopGUI(Player player, Shop shop) {
        super(player, "shop-gui", shop.getShopDisplayName(), 6);
        this.shop = shop;
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        int slot = 0;
        for (Category category : shop.getCategories().values()) {
            addItem(slot++, new Icon(Material.EMERALD_BLOCK).setName(category.getName()).onClick(e -> {
                new CategoryGUI(player, category).open();
            }));
        }
    }
}
