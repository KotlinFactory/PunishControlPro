package org.mineacademy.punishcontrol.core.warn;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.mineacademy.punishcontrol.core.punish.PunishDuration;

import java.util.UUID;

@RequiredArgsConstructor
public final class Warn {
	@NonNull
	private final UUID target, creator;
	@NonNull
	private final PunishDuration duration;

}
