package org.mineacademy.punishcontrol.spigot;

import lombok.NonNull;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.collection.StrictList;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.settings.YamlStaticConfig;
import org.mineacademy.punishcontrol.core.CoreModule;
import org.mineacademy.punishcontrol.core.DaggerCoreModule;
import org.mineacademy.punishcontrol.core.SimplePunishControlPlugin;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.provider.SettingsProvider;
import org.mineacademy.punishcontrol.core.punish.Ban;
import org.mineacademy.punishcontrol.core.storage.StorageType;
import org.mineacademy.punishcontrol.spigot.command.*;
import org.mineacademy.punishcontrol.spigot.impl.SpigotPlayerProvider;
import org.mineacademy.punishcontrol.spigot.impl.SpigotTextureProvider;
import org.mineacademy.punishcontrol.spigot.impl.SpigotWorkingDirectoryProvider;
import org.mineacademy.punishcontrol.spigot.settings.Localization;
import org.mineacademy.punishcontrol.spigot.settings.Settings;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public final class PunishControl extends SimplePlugin implements SimplePunishControlPlugin {
	private final SpigotModule spigotModule = DaggerSpigotModule.builder().build();
	private final CoreModule coreModule = DaggerCoreModule.builder().build();

	@Override
	protected void onPluginStart() {
		Common.ADD_LOG_PREFIX = false;

		setProviders();

		onPunishControlPluginStart();

		//Bypass UltraPunishments
		//Common.runLater(40, this::registerCommands);
	}

	//Setting the implementations of our providers
	private void setProviders() {
		Providers.workingDirectoryProvider(SpigotWorkingDirectoryProvider.newInstance());
		Providers.playerProvider(SpigotPlayerProvider.newInstance());
		Providers.textureProvider(SpigotTextureProvider.newInstance());

		Providers.settingsProvider(new SettingsProvider() {
			@Override
			public Set<String> getReportReasons() {
				Valid.checkNotNull(Settings.reportReasons, "Report reasons not yet set");
				return Settings.reportReasons;
			}

			@Override
			public boolean cacheResults() {
				Valid.checkNotNull(Settings.cacheResults, "CacheResults not yet set");

				return Settings.cacheResults;
			}

			@Override
			public List<String> getJoinMessageForBannedPlayer(final Ban ban) {
				return null;
			}

		});
	}

	// ----------------------------------------------------------------------------------------------------
	// Methods to start our plugin.
	// ----------------------------------------------------------------------------------------------------

	@Override
	public void registerCommands() {
		registerCommand(CommandMain.newInstance(new StrictList<>("punishcontrol", "phc", "pun", "pc")));
		registerCommand(CommandBan.newInstance());
		registerCommand(CommandKick.newInstance());
		registerCommand(CommandWarn.newInstance());
		registerCommand(CommandMute.newInstance());
	}

	@Override
	public void registerListener() {
		System.out.println("Registered");
		registerEvents(spigotModule.spigotDataSetter());
		registerEvents(spigotModule.spigotJoinHandler());
	}

	@Override
	public String chooseLanguage() {
		return "ENG";
	}

	@Override
	public StorageType chooseStorageProvider() {
		return Settings.storageType;
	}

	// ----------------------------------------------------------------------------------------------------
	// Overridden methods needed by our superclasses
	// ----------------------------------------------------------------------------------------------------

	@Override
	public List<Class<? extends YamlStaticConfig>> getSettings() {
		return Arrays.asList(Settings.class, Localization.class);
	}

	@Override
	public void log(final @NonNull String... message) {
		Common.log(message);
	}

	@Override
	public void saveError(@NonNull final Throwable t) {
		Common.error(t);
	}
}
