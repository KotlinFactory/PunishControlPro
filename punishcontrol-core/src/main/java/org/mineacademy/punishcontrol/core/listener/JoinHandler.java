package org.mineacademy.punishcontrol.core.listener;

import lombok.NonNull;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.punishes.Ban;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class JoinHandler {
	protected final StorageProvider storageProvider;

	protected JoinHandler(@NonNull final StorageProvider storageProvider) {
		this.storageProvider = storageProvider;
	}


	protected abstract void setCancelReason(@NonNull final List<String> cancelReason, @NonNull final Object event);

	protected abstract void setCancelled(final boolean cancelled, @NonNull final Object event);

	protected final void handlePlayerJoin(@NonNull final UUID uuid, @NonNull final Object event) {
		if (!storageProvider.isBanned(uuid)) {
			return;
		}

		setCancelled(true, event);
		//TODO

		final Optional<Ban> optionalBan = storageProvider.currentBan(uuid);
		if (!optionalBan.isPresent()) {
			return;
		}

		final Ban ban = optionalBan.get();

		/*
		 * You have been banned
		 */
		setCancelReason(Providers.settingsProvider().getJoinMessageForBannedPlayer(ban), event);
	}
}
