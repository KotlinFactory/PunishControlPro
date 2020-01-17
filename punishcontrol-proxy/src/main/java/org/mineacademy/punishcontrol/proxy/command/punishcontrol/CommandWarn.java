package org.mineacademy.punishcontrol.proxy.command.punishcontrol;

public final class CommandWarn extends AbstractPunishCommand {

	public static CommandWarn newInstance() {
		return new CommandWarn();
	}

	private CommandWarn() {
		super("warn");
		setDescription("Warn a player using a sleek gui.");
	}
}
