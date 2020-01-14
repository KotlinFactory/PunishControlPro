package org.mineacademy.punishcontrol.spigot.command;

import org.mineacademy.fo.collection.StrictList;
import org.mineacademy.fo.command.SimpleCommand;

public final class CommandReport extends SimpleCommand {
	public CommandReport() {
		super(new StrictList<>("report"));
		setDescription("Report a player using a sleek gui.");
		setUsage("[player] [reason]");
	}

	@Override
	protected void onCommand() {

	}
}
