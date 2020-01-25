package org.mineacademy.punishcontrol.core.provider;

import dagger.Module;
import dagger.Provides;
import de.leonhard.storage.util.Valid;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.mineacademy.punishcontrol.core.PunishControlManager;
import org.mineacademy.punishcontrol.core.storage.*;

/**
 * Central provider for dependencies
 * used in PunishControlPro.
 * <p>
 * It is also used by Dagger
 */

@Accessors(fluent = true)
@Module
public final class Providers {

	@Setter
	@NonNull
	private static PlayerProvider playerProvider;

	@Setter
	@NonNull
	private static TextureProvider textureProvider;

	@Setter
	@NonNull
	private static SettingsProvider settingsProvider;

	@Setter
	@NonNull
	private static WorkingDirectoryProvider workingDirectoryProvider;

	//StorageProvider can't be set.
	public static StorageProvider storageProvider() {
		return PunishControlManager.storageType().getStorageProvider();
	}

	@Provides
	public static PlayerProvider playerProvider() {
		Valid.notNull(playerProvider, "PlayerProvider not yet set.");

		return playerProvider;
	}

	@Provides
	public static TextureProvider textureProvider() {
		Valid.notNull(textureProvider, "TextureProvider not yet set.");

		return textureProvider;
	}

	@Provides
	public static SettingsProvider settingsProvider() {
		Valid.notNull(settingsProvider, "SettingsProvider not yet set");

		return settingsProvider;
	}

	@Provides
	public static WorkingDirectoryProvider workingDirectoryProvider() {
		Valid.notNull(workingDirectoryProvider, "WorkingDirectoryProvider not yet set");

		return workingDirectoryProvider;
	}

	// ----------------------------------------------------------------------------------------------------
	// Dagger only
	// ----------------------------------------------------------------------------------------------------

	@Provides
	public static MySQLConfig config(@NonNull final WorkingDirectoryProvider workingDirectoryProvider) {
		Valid.notNull(workingDirectoryProvider, "Working directoryProvider is null");

		return MySQLConfig.newInstance(workingDirectoryProvider);
	}

	@Provides
	public static MySQLStorageProvider mySQLStorageProvider() {
		return (MySQLStorageProvider) StorageType.MYSQL.getStorageProvider();
	}

	@Provides
	public static JsonStorageProvider jsonStorageProvider() {
		return (JsonStorageProvider) StorageType.JSON.getStorageProvider();
	}
}
