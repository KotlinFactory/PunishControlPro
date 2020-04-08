package org.mineacademy.punishcontrol.core.providers;

import java.util.List;
import org.mineacademy.punishcontrol.core.punishes.Ban;

/** Provides all settings needed by the PunishControl core */
public interface SettingsProvider {

  // Should results be cached?
  boolean cacheResults();

  boolean isAPIEnabled();

  List<String> getJoinMessageForBannedPlayer(Ban ban);

  List<String> allowedCommands();
}
