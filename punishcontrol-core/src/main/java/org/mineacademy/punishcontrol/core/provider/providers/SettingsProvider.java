package org.mineacademy.punishcontrol.core.provider.providers;

import org.mineacademy.punishcontrol.core.punishes.Ban;

import java.util.List;

/**
 * Provides all settings needed
 * by the PunishControl core
 */
public interface SettingsProvider {

	//Should results be cached?
	boolean cacheResults();

	List<String> getJoinMessageForBannedPlayer(Ban ban);

}
