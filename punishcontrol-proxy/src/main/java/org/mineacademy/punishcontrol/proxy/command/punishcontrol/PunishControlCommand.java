package org.mineacademy.punishcontrol.proxy.command.punishcontrol;

import lombok.NonNull;
import org.mineacademy.bfo.collection.StrictList;
import org.mineacademy.bfo.command.SimpleCommand;

public final class PunishControlCommand extends SimpleCommand {

	public static PunishControlCommand newInstance(@NonNull final String... labels) {
		return new PunishControlCommand(labels);
	}

	private PunishControlCommand(@NonNull final String... labels) {
		super(new StrictList<>(labels));
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
//		new PunishControlGUI().displayTo(getPlayer());
	}

	private void doHelp() {

	}
}
