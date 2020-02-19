package org.mineacademy.punishcontrol.proxy.settings;

import de.leonhard.storage.Yaml;
import org.mineacademy.bfo.plugin.SimplePlugin;
import org.mineacademy.bfo.settings.SimpleSettings;
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

	@Override
	protected int getConfigVersion() {
		return 0;
	}

	@Override
	public Yaml getConfigInstance() {
		return new Yaml("settings", SimplePlugin.getData().getAbsolutePath());
	}
}
