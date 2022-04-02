package mc.obliviate.supershops.shop.category;

import mc.obliviate.supershops.product.ItemStackProduct;
import mc.obliviate.supershops.product.ShopProduct;
import mc.obliviate.supershops.shop.Shop;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Category {

	private final Shop shop;
	private final String name;
	private final List<ShopProduct> products = new ArrayList<>();

	private Category(Shop shop, String name) {
		this.shop = shop;
		this.name = name;
	}

	public static Category deserialize(Shop shop, ConfigurationSection section) {
		final String name = section.getString("category-name");
		final Category category = new Category(shop, name);
		for (String product : section.getConfigurationSection("products").getKeys(false)) {

			ConfigurationSection productSection = section.getConfigurationSection("products." + product);
			if (productSection == null) throw new IllegalArgumentException("Product configuration cannot found.");

			switch (Objects.requireNonNull(productSection.getString("product-type")).toLowerCase()) {
				case "item":
					category.registerProduct(ItemStackProduct.deserialize(productSection));
			}
		}

		int i = 0;
		for (Material mat : Material.values()) {
			if (i++ == 300) break;
			if (i == 1) continue;
			category.registerProduct(new ItemStackProduct(new ItemStack(mat), 1, 1, i%2 == 0, i%3 == 0));
		}

		return category;
	}

	public void registerProduct(ShopProduct product) {
		products.add(product);
	}

	public Shop getShop() {
		return shop;
	}

	public List<ShopProduct> getProducts() {
		return products;
	}

	public String getName() {
		return name;
	}
}
