package org.mineacademy.punishcontrol.core;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.punish.Ban;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;

import java.util.UUID;

/**
 * Abstraction layer for the
 * most used methods from StorageProvider.
 */

@UtilityClass
public class PunishManager {
	private final StorageProvider provider = Providers.storageProvider();

	public boolean isBanned(@NonNull final UUID uuid) {
		return provider.isBanned(uuid);
	}

	public boolean isMuted(@NonNull final UUID uuid) {
		return provider.isMuted(uuid);
	}

	public Ban currentBan(@NonNull final UUID uuid) {
		return provider.currentBan(uuid);
	}

}
