package org.mineacademy.punishcontrol.proxy.impl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mineacademy.punishcontrol.core.provider.providers.SettingsProvider;
import org.mineacademy.punishcontrol.core.punishes.Ban;
import org.mineacademy.punishcontrol.proxy.settings.Settings;

import java.util.List;

// TODO

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProxySettingsProvider implements SettingsProvider {

  public static ProxySettingsProvider newInstance() {
    return new ProxySettingsProvider();
  }

  @Override
  public boolean cacheResults() {
    return Settings.Advanced.CACHE_RESULTS;
  }

  @Override
  public boolean isAPIEnabled() {
    return Settings.Advanced.API.ENABLED;
  }

  @Override
  public List<String> getJoinMessageForBannedPlayer(final Ban ban) {
    return null;
  }
}
