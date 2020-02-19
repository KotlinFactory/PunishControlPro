package org.mineacademy.punishcontrol.core.punish;


import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.mineacademy.punishcontrol.core.util.TimeUtil;

import java.util.OptionalInt;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class PunishDuration {
	private final long ms;

	public static PunishDuration of(@NonNull String humanReadableTime) {
		if (!humanReadableTime.contains(" ")) {
			humanReadableTime = splitHumanToHumanReadable(humanReadableTime);
		}

		return new PunishDuration(TimeUtil.toTicks(humanReadableTime) * 50); //Converting to ms (1tick = 50ms)
	}

	//10days becomes 10 days
	private static String splitHumanToHumanReadable(@NonNull final String humanReadable) {
		// returns an OptionalInt with the value of the index of the first Letter
		final OptionalInt firstLetterIndex = IntStream.range(0, humanReadable.length())
			.filter(i -> Character.isLetter(humanReadable.charAt(i)))
			.findFirst();

		// Default if there is no letter, only numbers
		String numbers = humanReadable;
		String letters = "";
		// if there are letters, split the string at the first letter
		if (firstLetterIndex.isPresent()) {
			numbers = humanReadable.substring(0, firstLetterIndex.getAsInt());
			letters = humanReadable.substring(firstLetterIndex.getAsInt());
		}

		return numbers + " " + letters;
	}

	public static PunishDuration of(final long ms) {
		return new PunishDuration(ms);
	}

	public static PunishDuration of(final long time, final TimeUnit unit) {
		return new PunishDuration(unit.toMillis(time));
	}

	public static PunishDuration permanent() {
		return new PunishDuration(-1);
	}


	// ----------------------------------------------------------------------------------------------------
	// Convenience methods here
	// ----------------------------------------------------------------------------------------------------

	public boolean isPermanent() {
		return ms == -1L;
	}

	public long createBanTime() {
		return System.currentTimeMillis() + ms;
	}

	@Override public String toString() {
		return TimeUtil.formatTimeGeneric(TimeUnit.MILLISECONDS.toSeconds(ms));
	}

	public long toMs() {
		return ms;
	}
}
