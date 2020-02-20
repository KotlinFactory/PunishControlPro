package org.mineacademy.punishcontrol.core.punish;

import lombok.NonNull;

public interface PunishMessageBroadcaster {
	void broadcastMessage(@NonNull final Punish punish, final boolean silent, final boolean superSilent);
}
