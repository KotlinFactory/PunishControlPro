package org.mineacademy.punishcontrol.core.listener;

import lombok.NonNull;
import org.mineacademy.punishcontrol.core.PunishManager;

import java.util.UUID;

public interface JoinHandler {

	void setCancelReason(@NonNull String cancelReason);

	void setCancelled(boolean cancelled, Object event);

	default void handlePlayerJoin(final UUID uuid, final Object event) {
		if (!PunishManager.isBanned(uuid)) {
			return;
		}


		setCancelled(true, event);
		//TODO


		/**
		 * You have been banned
		 */
		setCancelReason("");
	}
}
