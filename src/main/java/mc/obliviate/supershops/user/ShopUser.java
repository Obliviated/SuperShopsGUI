package mc.obliviate.supershops.user;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShopUser {

	private static final Map<UUID, ShopUser> userMap = new HashMap<>();
	private final UUID uuid;
	private int stackSize;


	public ShopUser(UUID uuid, int stackSize) {
		this.uuid = uuid;
		this.stackSize = stackSize;
		userMap.put(uuid, this);
	}

	public static Map<UUID, ShopUser> getUserMap() {
		return userMap;
	}

	public static ShopUser getUser(UUID uuid) {
		final ShopUser user = userMap.get(uuid);
		if (user == null) return new ShopUser(uuid,1);
		return user;
	}

	public int getStackSize() {
		return stackSize;
	}

	public void setStackSize(int stackSize) {
		this.stackSize = stackSize;
	}

	public UUID getUuid() {
		return uuid;
	}
}
