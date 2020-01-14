package org.mineacademy.punishcontrol.core.listener;

import lombok.NonNull;
import org.mineacademy.punishcontrol.core.PunishManager;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.punish.Ban;

import java.util.List;
import java.util.UUID;

public interface JoinHandler {

	void setCancelReason(List<String> cancelReason, @NonNull final Object event);

	void setCancelled(boolean cancelled, Object event);

	default void handlePlayerJoin(final UUID uuid, final Object event) {
		if (!PunishManager.isBanned(uuid)) {
			return;
		}

		setCancelled(true, event);
		//TODO


		final Ban ban = PunishManager.currentBan(uuid);


		/**
		 * You have been banned
		 */
		setCancelReason(Providers.settingsProvider().getJoinMessageForBannedPlayer(ban), event);
	}
}
