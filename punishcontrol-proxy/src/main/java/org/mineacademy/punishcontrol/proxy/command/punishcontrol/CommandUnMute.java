package org.mineacademy.punishcontrol.proxy.command.punishcontrol;

public final class CommandUnMute extends AbstractPunishCommand {

	public static CommandUnMute newInstance() {
		return new CommandUnMute();
	}

	private CommandUnMute() {
		super("unmute");
	}
}
