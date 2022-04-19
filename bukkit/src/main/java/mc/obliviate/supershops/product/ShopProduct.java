package mc.obliviate.supershops.product;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface ShopProduct {

    void sell(Player player, int amount);

    void buy(Player player, int amount);

    ItemStack getIconItem(int amount);

    default boolean validate(Player player, int amount) {
        if (player == null) return false;
        if (amount <= 0) return false;
        return true;
    }

}
