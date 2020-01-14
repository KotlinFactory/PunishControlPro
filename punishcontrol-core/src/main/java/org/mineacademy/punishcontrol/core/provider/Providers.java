package org.mineacademy.punishcontrol.core.provider;

import de.leonhard.storage.util.Valid;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.UtilityClass;
import org.mineacademy.punishcontrol.core.PunishControlManager;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;

@UtilityClass
@Accessors(fluent = true)
public class Providers {

	@Setter
	@NonNull
	private PlayerProvider playerProvider;

	@Setter
	@NonNull
	private TextureProvider textureProvider;

	@Setter
	@NonNull
	private SettingsProvider settingsProvider;

	//StorageProvider can't be set.
	public StorageProvider storageProvider() {
		return PunishControlManager.storageType().getStorageProvider();
	}

	public PlayerProvider playerProvider() {
		Valid.notNull(playerProvider, "PlayerProvider not yet set.");

		return playerProvider;
	}

	public TextureProvider textureProvider() {
		Valid.notNull(textureProvider, "TextureProvider not yet set.");

		return textureProvider;
	}

	public SettingsProvider settingsProvider() {
		Valid.notNull(settingsProvider, "SettingsProvider not yet set");

		return settingsProvider;
	}
}
