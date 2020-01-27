package org.mineacademy.punishcontrol.spigot.command;

import org.mineacademy.fo.command.SimpleCommand;

public final class CommandKick extends SimpleCommand {

	public static CommandKick newInstance() {
		return new CommandKick();
	}

	private CommandKick() {
		super("kick");
	}

	@Override protected void onCommand() {

	}
}
