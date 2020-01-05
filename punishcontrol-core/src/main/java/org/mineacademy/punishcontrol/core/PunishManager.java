package org.mineacademy.punishcontrol.core;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@UtilityClass
public class PunishManager {

	//Caching banned & muted players for best Performance
	public final Set<UUID> bannedPlayers = new HashSet<>();
	public final Set<UUID> mutedPlayers = new HashSet<>();

	public boolean isBanned(@NonNull final UUID uuid) {
		return bannedPlayers.contains(uuid);
	}

	public boolean isMuted(@NonNull final UUID uuid) {
		return mutedPlayers.contains(uuid);
	}


}
