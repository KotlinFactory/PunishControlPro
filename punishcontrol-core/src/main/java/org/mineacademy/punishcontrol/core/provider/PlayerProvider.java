package org.mineacademy.punishcontrol.core.provider;

import lombok.NonNull;

import java.util.List;
import java.util.UUID;

/**
 * Provide Data needed
 */
public interface PlayerProvider {

	List<UUID> getOfflinePlayers();

	boolean isOnline(@NonNull UUID uuid);

	String getName(@NonNull UUID uuid);

	UUID getUUID(@NonNull String name);

	void sendIfOnline(@NonNull UUID uuid, @NonNull String... messages);

}
