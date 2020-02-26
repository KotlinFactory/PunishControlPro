package org.mineacademy.punishcontrol.proxy.settings;

import org.mineacademy.bfo.settings.SimpleSettings;
import org.mineacademy.punishcontrol.core.storage.StorageType;

import java.util.ArrayList;
import java.util.List;

public final class Settings extends SimpleSettings {

	public static List<String> COMMAND_ALIASES = new ArrayList<>();
	public static StorageType STORAGE_TYPE;

	private static void init() {
		STORAGE_TYPE = Enum.valueOf(StorageType.class, getString("Storage"));
		COMMAND_ALIASES = getStringList("Command_Aliases");
		pathPrefix("Reports");

	}

	public final static class Notifications {
		public final static class Punish {

			public static Boolean ENABLED;
			public static String PERMISSION;


			private static void init() {
				pathPrefix("Notifications.Punish");
				ENABLED = getBoolean("Enabled");
				PERMISSION = getString("Permission");
			}
		}

		public final static class SilentPunish {
			public static Boolean ENABLED;
			public static String PERMISSION;

			private static void init() {
				pathPrefix("Notifications.Silent-Punish");
				ENABLED = getBoolean("Enabled");
				PERMISSION = getString("Permission");
			}
		}
	}

	public static final class Advanced {
		public static Boolean CACHE_RESULTS;

		private static void init() {
			pathPrefix("Advanced");
			CACHE_RESULTS = getBoolean("Cache_Results");
		}

		public static final class API {
			public static boolean ENABLED;

			private static void init() {

				pathPrefix("Advanced.API");
				ENABLED = getBoolean("Enabled");
			}
		}
	}

	@Override
	protected int getConfigVersion() {
		return 1;
	}
}
