package org.mineacademy.punishcontrol.spigot.gui;

import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.model.SimpleSound;
import org.mineacademy.fo.remain.CompSound;

public final class PunishControlGUI extends Menu {

	public static void showTo(@NonNull final Player player) {
		new PunishControlGUI().displayTo(player);
	}

	//You can't change anything here by clicking so we only need this gui once:)
	//And createNewInstance is already used:I
	public static PunishControlGUI create() {
		return new PunishControlGUI();
	}

	//TODO See boss for design.
	private PunishControlGUI() {
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
