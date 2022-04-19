package mc.obliviate.supershops.gui;

import mc.obliviate.inventory.GUI;
import mc.obliviate.inventory.Icon;
import mc.obliviate.inventory.pagination.Pagination;
import mc.obliviate.supershops.product.ShopProduct;
import mc.obliviate.supershops.shop.category.Category;
import mc.obliviate.supershops.user.ShopUser;
import mc.obliviate.supershops.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class CategoryGUI extends GUI {

    private final Category category;
    private final ShopUser user;

    public CategoryGUI(Player player, Category category) {
        super(player, "category-gui", category.getShop().getShopDisplayName() + " > " + category.getName(), 6);
        this.category = category;
        user = ShopUser.getUser(player.getUniqueId());
        getPagination().firstPage();
        getPagination().addSlotsBetween(9, 44);
    }


    @Override
    public void onOpen(InventoryOpenEvent event) {
        fillRow(new Icon(Material.BLACK_STAINED_GLASS_PANE), 0);
        calculateAndUpdatePagination();
        addItem(0, new Icon(new ItemBuilder(Material.ARROW).setName("&aPrevious").build()).onClick(e -> {
            getPagination().previousPage();
            open();
        }));
        addItem(8, new Icon(new ItemBuilder(Material.ARROW).setName("&aNext").build()).onClick(e -> {
            getPagination().nextPage();
            open();
        }));

        addItem(2, new Icon(Material.CHEST_MINECART).onClick(e -> {
            user.setStackSize(1);
            open();
        }));
        addItem(3, new Icon(Material.CHEST_MINECART).setAmount(8).onClick(e -> {
            user.setStackSize(8);
            open();
        }));
        addItem(4, new Icon(Material.CHEST_MINECART).setAmount(16).onClick(e -> {
            user.setStackSize(16);
            open();
        }));
        addItem(5, new Icon(Material.CHEST_MINECART).setAmount(32).onClick(e -> {
            user.setStackSize(32);
            open();
        }));
        addItem(6, new Icon(Material.CHEST_MINECART).setAmount(64).onClick(e -> {
            user.setStackSize(64);
            open();
        }));
    }

    private void calculateProducts() {
        final Pagination pagination = getPagination();
        pagination.getItems().clear();
        for (final ShopProduct product : category.getProducts()) {
            pagination.addHytem(new Icon(product.getIconItem(user.getStackSize())).setAmount(user.getStackSize()).onClick(e -> {
                if (e.isLeftClick()) {
                    product.buy(player, user.getStackSize());
                } else if (e.isRightClick()) {
                    product.sell(player, user.getStackSize());
                }
            }));
        }
    }

    private void calculateAndUpdatePagination() {
        calculateProducts();
        getPagination().update();
    }
}
