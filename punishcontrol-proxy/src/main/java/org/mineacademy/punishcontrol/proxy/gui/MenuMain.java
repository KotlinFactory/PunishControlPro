package org.mineacademy.punishcontrol.proxy.gui;

import de.exceptionflug.mccommons.inventories.api.InventoryType;
import lombok.NonNull;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.mineacademy.burst.menu.OnePageMenu;

public final class MenuMain extends OnePageMenu {


	public static void showTo(@NonNull final ProxiedPlayer player) {
		new MenuMain(player).showUp();
	}

	private MenuMain(final ProxiedPlayer player) {
		super(player, "MenuMain", InventoryType.GENERIC_9X5);
		setTitle("§3Punish§5Control");
	}


}
