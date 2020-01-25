package org.mineacademy.punishcontrol.core.listener;

import lombok.NonNull;
import org.mineacademy.punishcontrol.core.PunishManager;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.punish.Ban;

import java.util.List;
import java.util.UUID;

public abstract class JoinHandler {

	protected abstract void setCancelReason(@NonNull final List<String> cancelReason, @NonNull final Object event);

	protected abstract void setCancelled(final boolean cancelled, @NonNull final Object event);

	protected final void handlePlayerJoin(@NonNull final UUID uuid, @NonNull final Object event) {
		if (!PunishManager.isBanned(uuid)) {
			return;
		}

		setCancelled(true, event);
		//TODO

		final Ban ban = PunishManager.currentBan(uuid);

		/*
		 * You have been banned
		 */
		setCancelReason(Providers.settingsProvider().getJoinMessageForBannedPlayer(ban), event);
	}
}
