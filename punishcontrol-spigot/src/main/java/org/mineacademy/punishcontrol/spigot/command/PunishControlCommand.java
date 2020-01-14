package org.mineacademy.punishcontrol.spigot.command;

import org.mineacademy.fo.collection.StrictList;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.punishcontrol.spigot.gui.PunishControlGUI;

public final class PunishControlCommand extends SimpleCommand {

	public PunishControlCommand(final StrictList<String> labels) {
		super(labels);
	}

	@Override
	protected void onCommand() {
		checkConsole();

		if (args.length != 0) {
			doHelp();
			return;
		}

		checkConsole();
		//Open GUI
		new PunishControlGUI().displayTo(getPlayer());
	}

	private void doHelp() {

	}
}
