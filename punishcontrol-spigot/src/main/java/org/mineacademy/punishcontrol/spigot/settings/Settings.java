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


		//Loading our Groups

		System.out.println("KEYS:");
		System.out.println(getValuesAndKeys("Groups").toString());

		/*
		Groups:
  Admin:
    Priority: 0
    Permission: punishcontrol.group.admin
    #Set to -1 to disable
    Limits:
      Ban: -1
      Mute: -1
      Warn: -1
  Moderator:
    Priority: 1
    Permission: punishcontrol.group.moderator
    Limits:
      Ban: 1 year
      Mute: 1 year
      Warn: 2 year
    Supporter:
      Priority: 2
      Permission: punishcontrol.group.supporter
      Limits:
        Ban: 1 month
        Mute: 2 month
        Warn: 3 month
		 */
	}

	@Override
	protected int getConfigVersion() {
		return 1;
	}


}
