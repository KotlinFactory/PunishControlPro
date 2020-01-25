package org.mineacademy.punishcontrol.spigot.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.MenuPagged;

//TODO Punishes from Type/Player/This/That
public class MenuPunishBrowser extends MenuPagged {

	protected MenuPunishBrowser(final Iterable pages) {
		super(pages);
	}

	@Override
	protected String[] getInfo() {
		return new String[0];
	}

	@Override
	protected ItemStack convertToItemStack(final Object o) {
		return null;
	}

	@Override
	protected void onPageClick(final Player player, final Object o, final ClickType clickType) {

	}
}
    