package org.mineacademy.punishcontrol.core.provider;

import lombok.NonNull;

import java.util.List;
import java.util.UUID;

/**
 * Provide Data needed
 */
public interface PlayerProvider {

	/**
	 * @return Players which are on the server & players
	 * joined the server earlier.
	 */
	List<UUID> getOfflinePlayers();

	List<UUID> getOnlinePlayers();

	boolean isOnline(@NonNull UUID uuid);

	String getName(@NonNull UUID uuid);

	UUID getUUID(@NonNull String name);

	/**
	 * @return Warning: Nullable!
	 */
	String getIpIfOnline(@NonNull UUID uuid);

	void sendIfOnline(@NonNull UUID uuid, @NonNull String... messages);

}
