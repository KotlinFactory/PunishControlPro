package org.mineacademy.punishcontrol.proxy.command.punishcontrol;

import org.mineacademy.bfo.collection.StrictList;
import org.mineacademy.bfo.command.SimpleCommand;

public class PunishControlCommandGroup extends SimpleCommand {
	protected PunishControlCommandGroup() {
		super(new StrictList<>("punishcontrol", "punish", "phc", "pc", "pun"));
	}

	@Override
	protected void onCommand() {
		checkConsole();
	}

}
