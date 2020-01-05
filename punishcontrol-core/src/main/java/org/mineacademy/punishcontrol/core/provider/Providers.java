package org.mineacademy.punishcontrol.core.provider;

import de.leonhard.storage.util.Valid;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.UtilityClass;

@UtilityClass
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
