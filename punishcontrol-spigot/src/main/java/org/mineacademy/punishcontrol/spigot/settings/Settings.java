package org.mineacademy.punishcontrol.spigot.settings;


import org.mineacademy.fo.settings.SimpleSettings;
import org.mineacademy.punishcontrol.core.storage.StorageType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Settings extends SimpleSettings {
	public static List<String> COMMAND_ALIASES = new ArrayList<>();
	public static Set<String> reportReasons;
	public static Boolean cacheResults;
	public static StorageType storageType;

	private static void init() {
		COMMAND_ALIASES = getStringList("Command_Aliases");
		pathPrefix("Reports");
		reportReasons = new HashSet<>(getStringList("Allowed_Reasons"));
		pathPrefix("Advanced");
		cacheResults = getBoolean("Cache_Results");
		pathPrefix(null);
		storageType = Enum.valueOf(StorageType.class, getString("Storage"));

	}

	public final static class Notifications {
		public final static class Punish {

			public static Boolean enabled;
			public static String permission;


			private static void init() {
				pathPrefix("Notifications.Punish");
				enabled = getBoolean("Enabled");
				permission = getString("Permission");
			}
		}

		public final static class SilentPunish {
			public static Boolean enabled;
			public static String permission;

			private static void init() {
				pathPrefix("Notifications.Silent-Punish");
				enabled = getBoolean("Enabled");
				permission = getString("Permission");
			}
		}
	}

	@Override
	protected int getConfigVersion() {
		return 1;
	}
}
