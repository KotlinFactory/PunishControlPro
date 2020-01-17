package org.mineacademy.punishcontrol.proxy;


import lombok.NonNull;
import org.mineacademy.bfo.Common;
import org.mineacademy.bfo.bungee.SimpleBungee;
import org.mineacademy.bfo.collection.StrictList;
import org.mineacademy.bfo.plugin.SimplePlugin;
import org.mineacademy.burst.Burst;
import org.mineacademy.punishcontrol.core.SimplePunishControlPlugin;
import org.mineacademy.punishcontrol.core.storage.StorageType;
import org.mineacademy.punishcontrol.proxy.command.punishcontrol.*;

public final class PunishControl extends SimplePlugin implements SimplePunishControlPlugin {

	@Override
	protected void onPluginStart() {
		Burst.setPlugin(this); //Set the plugin for our library
		onPunishControlPluginStart();

	}

	@Override
	public SimpleBungee getBungeeCord() {
		return null;
	}

	@Override
	public void log(final @NonNull String... message) {
		Common.log(message);
	}

	@Override
	public void registerCommands() {
		registerCommand(CommandBan.newInstance());
		registerCommand(CommandKick.newInstance());
		registerCommand(CommandMute.newInstance());
		registerCommand(CommandReport.newInstance());
		registerCommand(CommandUnBan.newInstance());
		registerCommand(CommandUnMute.newInstance());
		registerCommand(CommandWarn.newInstance());
		registerCommand(PunishControlCommand.newInstance(new StrictList<>("punishcontrol", "phc", "pun", "pc")));
	}

	@Override
	public void registerListener() {

	}

	@Override
	public String chooseLanguage() {
		return "";
	}

	@Override
	public StorageType chooseStorageProvider() {
		return null;
	}

	@Override
	public void saveError(@NonNull final Throwable t) {
		Common.error(t);
	}
}