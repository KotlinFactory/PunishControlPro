package org.mineacademy.punishcontrol.core.listener;

import lombok.NonNull;

import java.util.UUID;

public interface JoinHandler {

	void setCancelReason(@NonNull String cancelReason);

	void setCancelled(boolean cancelled, Object event);

	default void handlePlayerJoin(final UUID uuid, final Object event) {

	}
}
