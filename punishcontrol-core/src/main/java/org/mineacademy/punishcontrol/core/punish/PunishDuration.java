package org.mineacademy.punishcontrol.core.punish;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.mineacademy.punishcontrol.core.util.TimeUtil;

import java.util.concurrent.TimeUnit;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class PunishDuration {
	private final long ms;

	public static PunishDuration of(@NonNull String humanReadableTime) {
		return new PunishDuration(TimeUtil.toTicks(humanReadableTime) * 50); //Converting to ms (1tick = 50ms)
	}

	public static PunishDuration of(long ms) {
		return new PunishDuration(ms);
	}

	public static PunishDuration of(long time, TimeUnit unit) {
		return new PunishDuration(unit.toMillis(time));
	}

	public static PunishDuration permanent() {
		return new PunishDuration(-1);
	}

	public boolean isPermanent() {
		return ms == -1L;
	}

	public long createBanTime() {
		return System.currentTimeMillis() + ms;
	}

	public String format() {
		return TimeUtil.formatTimeGeneric(TimeUnit.MILLISECONDS.toSeconds(ms));
	}
}
