package org.mineacademy.punishcontrol.spigot.command;

import lombok.NonNull;
import org.mineacademy.fo.collection.StrictList;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.punishcontrol.spigot.gui.MenuMain;

public final class CommandMain extends SimpleCommand {

	public static CommandMain newInstance(@NonNull final StrictList<String> labels) {
		return new CommandMain(labels);
	}

	private CommandMain(@NonNull final StrictList<String> labels) {
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
		MenuMain.showTo(getPlayer());
	}

	private void doHelp() {

	}
}
