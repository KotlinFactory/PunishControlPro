package org.mineacademy.punishcontrol.spigot.command;

import org.mineacademy.fo.collection.StrictList;
import org.mineacademy.fo.command.SimpleCommand;

public final class CommandKick extends SimpleCommand {

	public CommandKick() {
		super(new StrictList<>("kick"));
		setUsage("[player] [reason]");
		setDescription("Kick a player using a sleek gui");
	}

	@Override
	protected void onCommand() {

	}
}
