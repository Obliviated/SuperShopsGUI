package mc.obliviate.supershops.commands;

import mc.obliviate.supershops.gui.ShopGUI;
import mc.obliviate.supershops.shop.Shop;
import me.despical.commandframework.Command;
import me.despical.commandframework.CommandArguments;

public class ShopCommand {

	@Command(name = "shop", aliases = "market", senderType = Command.SenderType.PLAYER)
	public void shopCommand(CommandArguments arg) {
		arg.sendMessage("opening");
		new ShopGUI(arg.getSender(), Shop.getShops().get("default")).open();
	}

}
