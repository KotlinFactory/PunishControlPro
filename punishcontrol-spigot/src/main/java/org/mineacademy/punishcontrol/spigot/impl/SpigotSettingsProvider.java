package org.mineacademy.punishcontrol.spigot.impl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mineacademy.fo.Valid;
import org.mineacademy.punishcontrol.core.provider.providers.SettingsProvider;
import org.mineacademy.punishcontrol.core.punishes.Ban;
import org.mineacademy.punishcontrol.spigot.settings.Settings;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SpigotSettingsProvider implements SettingsProvider {
	public static SpigotSettingsProvider newInstance() {
		return new SpigotSettingsProvider();
	}

	@Override
	public boolean cacheResults() {
		Valid.checkNotNull(Settings.Advanced.CACHE_RESULTS, "CacheResults not yet set");

		return Settings.Advanced.CACHE_RESULTS;
	}

	@Override public boolean isAPIEnabled() {
		return Settings.Advanced.API.ENABLED;
	}

	@Override
	public List<String> getJoinMessageForBannedPlayer(final Ban ban) {
		//TODO
		return null;
	}
}
