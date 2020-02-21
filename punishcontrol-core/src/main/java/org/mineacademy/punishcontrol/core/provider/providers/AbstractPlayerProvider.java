package org.mineacademy.punishcontrol.core.provider.providers;

import de.leonhard.storage.Json;
import de.leonhard.storage.internal.exception.LightningValidationException;
import lombok.NonNull;
import lombok.val;
import org.mineacademy.punishcontrol.core.PunishControlManager;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.util.UUIDFetcher;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractPlayerProvider extends Json implements PlayerProvider {

	public AbstractPlayerProvider() {
		super(PunishControlManager.FILES.UUID_STORAGE, Providers.workingDirectoryProvider().getDataFolder().getAbsolutePath() + "/data/");
	}

	@Override public final void saveData(@NonNull final UUID uuid, @NonNull final String name, final String ip) {
		fileData.insert(uuid.toString() + ".ip", ip); //Increasing performance -> Only writing to file 1x time.
		set(uuid.toString() + ".name", name);
	}

	@Override public final Optional<String> getIp(@NonNull final UUID uuid) {
		if (contains(uuid + ".ip")) {
			return Optional.of(uuid + ".ip");
		}

		return Optional.empty();
	}


	@Override public final String getName(@NonNull final UUID uuid) {
		if (contains(uuid.toString())) {
			return getString(uuid.toString());
		} else {
			final String name = UUIDFetcher.getName(uuid);
			if (name == null) {
				throw new LightningValidationException("No player with UUID '" + uuid + "' found");
			} else {
				set(uuid.toString() + ".name", name);
				return name;
			}
		}
	}

	@Override public UUID getUUID(@NonNull final String name) {
		for (final val entry : getFileData().toMap().entrySet()) {
			if (!(entry.getValue() instanceof Map)) {
				continue;
			}
			@SuppressWarnings("unchecked") final Map<String, Object> data = (Map<String, Object>) entry.getValue();
			if (data.get("name").toString().equalsIgnoreCase(name)) {
				return UUID.fromString(entry.getKey());
			}
		}

		//Not yet set.
		//Getting from Mojang & Setting it manually.

		final UUID uuid = UUIDFetcher.getUUID(name);

		if (uuid == null) {
			throw new LightningValidationException("No player named '" + name + "' found");
		}

		set(uuid.toString() + ".name", name);
		return uuid;
	}
}
