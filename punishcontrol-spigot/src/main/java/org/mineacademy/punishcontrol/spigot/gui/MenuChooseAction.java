package org.mineacademy.punishcontrol.spigot.gui;

import lombok.NonNull;
import org.bukkit.entity.Player;
import org.mineacademy.fo.menu.Menu;

public final class MenuChooseAction extends Menu {

	public static void showTo(@NonNull final Player player) {
		create().displayTo(player);
	}

	public static MenuChooseAction create() {
		return new MenuChooseAction();
	}

	private MenuChooseAction() {
		super(MenuPlayerBrowser.of(true));
		setSize(9 * 4);
//		setSound();
		setTitle("§3Choose an action");
	}

	@Override
	protected String[] getInfo() {
		return new String[0];
	}
}
