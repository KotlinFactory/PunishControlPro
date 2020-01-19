package org.mineacademy.punishcontrol.core.provider;

import de.leonhard.storage.Json;
import de.leonhard.storage.internal.exception.LightningValidationException;
import lombok.NonNull;
import lombok.val;
import org.mineacademy.punishcontrol.core.PunishControlManager;
import org.mineacademy.punishcontrol.core.util.UUIDFetcher;

import java.util.UUID;

public abstract class AbstractPlayerProvider extends Json implements PlayerProvider {

	public AbstractPlayerProvider() {
		super(PunishControlManager.FILES.UUID_STORAGE, Providers.workingDirectoryProvider().getDataFolder().getAbsolutePath() + "/data/");
	}

	@Override
	public final void saveUUIDAndName(@NonNull final UUID uuid, @NonNull final String name) {
		set(uuid.toString(), name);
	}

	@Override
	public final String getName(@NonNull final UUID uuid) {
		if (contains(uuid.toString())) {
			return getString(uuid.toString());
		} else {
			final String name = UUIDFetcher.getName(uuid);
			if (name == null) {
				throw new LightningValidationException("No player with UUID '" + uuid + "' found");
			} else {
				set(uuid.toString(), name);
				return name;
			}
		}
	}

	@Override
	public final UUID getUUID(@NonNull final String name) {
		for (final val entry : getFileData().toMap().entrySet()) {
			if (entry.getValue().equals(name)) {
				return UUID.fromString(entry.getKey());
			}

		}

		//Not yet set.
		//Getting from Mojang & Setting it manually.
		final UUID uuid = UUIDFetcher.getUUID(name);

		if (uuid == null) {
			throw new LightningValidationException("No player named '" + name + "' found");
		}


		set(uuid.toString(), name);
		return uuid;
	}
}
