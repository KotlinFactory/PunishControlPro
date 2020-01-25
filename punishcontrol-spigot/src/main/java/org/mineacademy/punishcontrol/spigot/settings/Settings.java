package org.mineacademy.punishcontrol.spigot.settings;


import org.mineacademy.fo.settings.SimpleSettings;
import org.mineacademy.punishcontrol.core.storage.StorageType;

import java.util.HashSet;
import java.util.Set;

public final class Settings extends SimpleSettings {
	public static Set<String> reportReasons;
	public static Boolean cacheResults;
	public static StorageType storageType;

	private static void init() {
		pathPrefix("Reports");
		reportReasons = new HashSet<>(getStringList("Allowed_Reasons"));
		pathPrefix("Advanced");
		cacheResults = getBoolean("Cache_Results");
		pathPrefix(null);
		storageType = Enum.valueOf(StorageType.class, getString("Storage"));
	}

	@Override
	protected int getConfigVersion() {
		return 1;
	}


}
