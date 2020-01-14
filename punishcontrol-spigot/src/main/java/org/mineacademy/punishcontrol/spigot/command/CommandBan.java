package org.mineacademy.punishcontrol.spigot.command;

import org.mineacademy.fo.collection.StrictList;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.punishcontrol.spigot.gui.PlayerBrowser;

public final class CommandBan extends SimpleCommand {

	public CommandBan() {
		super(new StrictList<>("ban"));
		setUsage("[player] [time] [reason]");
		setDescription("Ban a player using a sleek gui");
	}

	@Override
	protected void onCommand() {
		switch (args.length) {
			case 0:
				if (!isPlayer()) {
					returnTell("Console needs to provide more information", "To run this command", "Please provide at least 2 arguments.");
				}
				PlayerBrowser.showTo(getPlayer(), true);
				//SEND GUI
				break;
			case 1:
				checkConsole();
				System.out.println("k3l");
				break;
			case 2:
				break;
			case 3:
				System.out.println("adkle");
				break;
		}

	}


}
