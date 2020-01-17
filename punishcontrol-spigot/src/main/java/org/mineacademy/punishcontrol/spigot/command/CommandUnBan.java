package org.mineacademy.punishcontrol.spigot.command;

public final class CommandUnBan extends AbstractPunishCommand {

	public static CommandUnBan newInstance() {
		return new CommandUnBan();
	}

	private CommandUnBan() {
		super("unban");
		setUsage("[player]");
		setDescription("Unban a player using a sleek gui");
	}
}
