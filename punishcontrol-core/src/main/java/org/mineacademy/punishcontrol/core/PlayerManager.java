package org.mineacademy.punishcontrol.core;

import lombok.experimental.UtilityClass;
import org.mineacademy.punishcontrol.core.provider.Providers;

import java.util.List;
import java.util.UUID;

@UtilityClass
public class PlayerManager {

	//TODO: Useful?
	public List<UUID> listOfflinePlayers() {
		return Providers.playerProvider().getOfflinePlayers();
	}
}
