package org.mineacademy.punishcontrol.spigot.command;

public final class CommandUnMute extends AbstractPunishCommand {

	public static CommandUnMute newInstance() {
		return new CommandUnMute();
	}

	private CommandUnMute() {
		super("unmute");
	}
}
