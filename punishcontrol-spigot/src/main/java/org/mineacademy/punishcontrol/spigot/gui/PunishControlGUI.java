package org.mineacademy.punishcontrol.spigot.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.model.SimpleSound;
import org.mineacademy.fo.remain.CompSound;

public final class PunishControlGUI extends Menu {

	//TODO See boss for design.
	public PunishControlGUI() {
		setSize(9 * 5);
		setTitle("§3Punish§5Control");
		setSound(new SimpleSound(CompSound.CHEST_OPEN.getSound(), 0.5F, 0.5F));
	}

	@Override
	protected void onButtonClick(final Player player, final int slot, final InventoryAction action, final ClickType click, final Button button) {
		super.onButtonClick(player, slot, action, click, button);
	}

	@Override
	protected String[] getInfo() {
		return new String[0];
	}
}
