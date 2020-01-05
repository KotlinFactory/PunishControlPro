package org.mineacademy.punishcontrol.core.warn;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public final class Warn {
	@NonNull
	private final UUID target, creator;

}
