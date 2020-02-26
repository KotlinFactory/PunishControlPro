package org.mineacademy.punishcontrol.core;

import de.leonhard.storage.util.Valid;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import org.mineacademy.punishcontrol.core.storage.StorageType;

/**
 * Central manager & utility class
 * of PunishControl
 * <p>
 * Saves constants.
 */
@UtilityClass
@Accessors(chain = true)
public class PunishControlManager {
	@Setter
	@NonNull
	private StorageType storageType;
	@Setter
	@NonNull
	private String language;

	/*
	TODO:
	 - AutoBackuper /phc backup


*/
	public StorageType storageType() {
		Valid.notNull(storageType, "StorageType not yet set");

		return storageType;
	}

	public String language() {
		Valid.notNull(language, "Languages not yet set");

		return language;
	}

	// ----------------------------------------------------------------------------------------------------
	// Constants
	// ----------------------------------------------------------------------------------------------------

	@UtilityClass
	@FieldDefaults(makeFinal = true)
	public static class FILES {
		public String JSON_DATA_FILE_NAME = "Punishes";
		public String SKIN_STORAGE = "Textures";
		public String UUID_STORAGE = "UUIDs";
	}
}
