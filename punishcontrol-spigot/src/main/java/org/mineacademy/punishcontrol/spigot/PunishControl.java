package org.mineacademy.punishcontrol.spigot;


import lombok.NonNull;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.settings.YamlStaticConfig;
import org.mineacademy.punishcontrol.core.SimplePunishControlPlugin;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.provider.SettingsProvider;
import org.mineacademy.punishcontrol.core.storage.StorageType;
import org.mineacademy.punishcontrol.spigot.command.punishcontrol.PunishControlCommandGroup;
import org.mineacademy.punishcontrol.spigot.impl.SpigotPlayerProvider;
import org.mineacademy.punishcontrol.spigot.settings.Settings;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public final class PunishControl extends SimplePlugin implements SimplePunishControlPlugin {

	public static PunishControl getInstance() {
		return (PunishControl) SimplePlugin.getInstance();
	}

	@Override
	protected void onPluginStart() {
		Common.ADD_LOG_PREFIX = false;

		setProviders();

		onPunishControlPluginStart();
	}

	//Setting the implementations of our providers
	private void setProviders() {
		Providers.setPlayerProvider(new SpigotPlayerProvider());

		Providers.setSettingsProvider(new SettingsProvider() {
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

		});
	}

	// ----------------------------------------------------------------------------------------------------
	// Methods to start our plugin.
	// ----------------------------------------------------------------------------------------------------

	@Override
	public void registerCommands() {

	}

	@Override
	public void registerListener() {

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
		return Collections.<Class<? extends YamlStaticConfig>>singletonList(Settings.class);
	}

	@Override
	public SimpleCommandGroup getMainCommand() {
		return new PunishControlCommandGroup();
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
