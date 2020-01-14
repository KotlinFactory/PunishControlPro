package org.mineacademy.punishcontrol.spigot.command;

import org.mineacademy.fo.collection.StrictList;
import org.mineacademy.fo.command.SimpleCommand;

public final class CommandWarn extends SimpleCommand {
	public CommandWarn() {
		super(new StrictList<>("warn"));
		setDescription("Warn a player using a sleek gui.");
		setUsage("[player] [time] [reason]");
	}

	@Override
	protected void onCommand() {

	}
}
