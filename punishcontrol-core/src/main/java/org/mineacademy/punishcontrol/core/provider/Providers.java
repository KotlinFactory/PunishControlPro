package org.mineacademy.punishcontrol.core.provider;

import dagger.Module;
import dagger.Provides;
import de.leonhard.storage.util.Valid;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.mineacademy.punishcontrol.core.PunishControlManager;
import org.mineacademy.punishcontrol.core.provider.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.provider.providers.SettingsProvider;
import org.mineacademy.punishcontrol.core.provider.providers.TextureProvider;
import org.mineacademy.punishcontrol.core.provider.providers.WorkingDirectoryProvider;
import org.mineacademy.punishcontrol.core.punish.PunishProvider;
import org.mineacademy.punishcontrol.core.storage.MySQLConfig;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;

import javax.inject.Singleton;

/**
 * -- Heart of PunishControl --
 * <p>
 * Central provider for dependencies
 * used in PunishControlPro.
 * <p>
 * It is also used by Dagger
 */

@Module
@Accessors(fluent = true)
public final class Providers {

	@Setter
	@NonNull
	private static org.mineacademy.punishcontrol.core.provider.providers.PlayerProvider playerProvider;

	@Setter
	@NonNull
	private static org.mineacademy.punishcontrol.core.provider.providers.TextureProvider textureProvider;

	@Setter
	@NonNull
	private static org.mineacademy.punishcontrol.core.provider.providers.SettingsProvider settingsProvider;

	@Setter
	@NonNull
	private static org.mineacademy.punishcontrol.core.provider.providers.WorkingDirectoryProvider workingDirectoryProvider;

	@Setter
	@NonNull
	private static PunishProvider punishProvider;

	//StorageProvider can't be set.
	public static StorageProvider storageProvider() {
		return PunishControlManager.storageType().getStorageProvider();
	}

	@Singleton
	@Provides
	public static PlayerProvider playerProvider() {
		Valid.notNull(playerProvider, "PlayerProvider not yet set.");

		return playerProvider;
	}

	@Singleton
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
	public static org.mineacademy.punishcontrol.core.provider.providers.WorkingDirectoryProvider workingDirectoryProvider() {
		Valid.notNull(workingDirectoryProvider, "WorkingDirectoryProvider not yet set");

		return workingDirectoryProvider;
	}

	@Singleton
	@Provides
	public static PunishProvider punishProvider() {
		Valid.notNull(punishProvider, "PunishMessage-BroadCaster not yet set");

		return punishProvider;
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
	public static StorageProvider provider() {
		return PunishControlManager.storageType().getStorageProvider();
	}

//	@Provides
//	public static MySQLStorageProvider mySQLStorageProvider() {
//		return (MySQLStorageProvider) StorageType.MYSQL.getStorageProvider();
//	}
//
//	@Provides
//	public static JsonStorageProvider jsonStorageProvider() {
//		return (JsonStorageProvider) StorageType.JSON.getStorageProvider();
//	}
}
