package org.mineacademy.punishcontrol.spigot.impl;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mineacademy.fo.Valid;
import org.mineacademy.punishcontrol.core.providers.SettingsProvider;
import org.mineacademy.punishcontrol.core.punishes.Ban;
import org.mineacademy.punishcontrol.spigot.settings.Settings;
import org.mineacademy.punishcontrol.spigot.settings.Settings.Punish.Mute;

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

  @Override
  public boolean isAPIEnabled() {
    return Settings.Advanced.API.ENABLED;
  }

  @Override
  public List<String> getJoinMessageForBannedPlayer(final Ban ban) {
    // TODO
    return new ArrayList<>();
  }

  @Override
  public List<String> allowedCommands() {
    return Mute.BLOCKED_COMMANDS;
  }
}
