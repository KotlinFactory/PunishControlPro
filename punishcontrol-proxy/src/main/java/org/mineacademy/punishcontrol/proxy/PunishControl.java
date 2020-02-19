package org.mineacademy.punishcontrol.proxy;


import lombok.NonNull;
import org.mineacademy.bfo.Common;
import org.mineacademy.bfo.bungee.SimpleBungee;
import org.mineacademy.bfo.plugin.SimplePlugin;
import org.mineacademy.burst.Burst;
import org.mineacademy.punishcontrol.core.CoreModule;
import org.mineacademy.punishcontrol.core.DaggerCoreModule;
import org.mineacademy.punishcontrol.core.SimplePunishControlPlugin;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.storage.StorageType;
import org.mineacademy.punishcontrol.proxy.command.punishcontrol.*;
import org.mineacademy.punishcontrol.proxy.impl.ProxyPlayerProvider;
import org.mineacademy.punishcontrol.proxy.impl.ProxySettingsProvider;
import org.mineacademy.punishcontrol.proxy.impl.ProxyTextureProvider;
import org.mineacademy.punishcontrol.proxy.impl.ProxyWorkingDirectoryProvider;
import org.mineacademy.punishcontrol.proxy.listener.ProxyDataSetter;
import org.mineacademy.punishcontrol.proxy.settings.Settings;

public final class PunishControl extends SimplePlugin implements SimplePunishControlPlugin {
	private final ProxyModule proxyModule = DaggerProxyModule.create();
	private final CoreModule coreModule = DaggerCoreModule.create();

	@Override
	protected void onPluginStart() {
		Burst.setPlugin(this); //Set the plugin for our library
		setProviders();
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
		registerCommand(CommandMain.newInstance(Settings.MAIN_COMMAND_ALIASES));
	}

	@Override
	public void registerListener() {
		registerEvents(ProxyDataSetter.newInstance());
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


	private void setProviders() {
		//Working directory
		Providers.workingDirectoryProvider(ProxyWorkingDirectoryProvider.newInstance());
		//Player providers
		Providers.playerProvider(ProxyPlayerProvider.newInstance());
		org.mineacademy.burst.provider.Providers.setUuidNameProvider(ProxyPlayerProvider.newInstance());
		//Settings
		Providers.settingsProvider(ProxySettingsProvider.newInstance());
		//TextureProvider

		Providers.textureProvider(ProxyTextureProvider.newInstance());
	}
}