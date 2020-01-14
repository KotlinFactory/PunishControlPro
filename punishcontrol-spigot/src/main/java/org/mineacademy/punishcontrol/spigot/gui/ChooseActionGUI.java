package org.mineacademy.punishcontrol.spigot.gui;

import lombok.NonNull;
import org.bukkit.entity.Player;
import org.mineacademy.fo.menu.Menu;

public final class ChooseActionGUI extends Menu {

	public static void showTo(@NonNull final Player player) {
		create().displayTo(player);
	}

	public static ChooseActionGUI create() {
		return new ChooseActionGUI();
	}

	private ChooseActionGUI() {
		super(PlayerBrowser.of(true));
		setSize(9 * 4);
//		setSound();
		setTitle("§3Choose an action");
	}

	@Override
	protected String[] getInfo() {
		return new String[0];
	}
}
