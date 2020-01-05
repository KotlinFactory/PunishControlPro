package org.mineacademy.punishcontrol.core.provider;

import java.util.Set;

/**
 * Provides all settings needed
 * by the PunishControl core
 */
public interface SettingsProvider {
	Set<String> getReportReasons();

	//Should results be cached?
	boolean cacheResults();
}
