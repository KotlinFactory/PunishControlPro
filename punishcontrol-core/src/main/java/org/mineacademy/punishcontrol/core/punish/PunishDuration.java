package org.mineacademy.punishcontrol.core.punish;

import java.util.OptionalInt;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.mineacademy.punishcontrol.core.util.TimeUtil;

/**
 * Class to be able
 * <p>
 * TODO: do not obfuscate!
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class PunishDuration {

  private final long ms;

  /**
   * Returns an empty punish-duration.
   * Normally indicates that the String that should be parsed had the
   * wrong format.
   */
  public static PunishDuration empty() {
    return new PunishDuration(Long.MIN_VALUE);
  }

  public static PunishDuration of(@NonNull String humanReadableTime) {
    if (humanReadableTime.equalsIgnoreCase("-1")) {
      return permanent();
    }

    if (!humanReadableTime.contains(" ")) {
      // Input: 10days Output: 10 days
      humanReadableTime = splitHumanToHumanReadable(humanReadableTime);
    }

    // Converting to ms (1tick = 50ms)

    final long ticks = TimeUtil.toTicks(humanReadableTime);

    //Invalid format
    if (ticks == Long.MIN_VALUE) {
      return empty();
    }
    return new PunishDuration(TimeUtil.toTicks(humanReadableTime) * 50);
  }

  // 10days becomes 10 days
  private static String splitHumanToHumanReadable(
      @NonNull final String humanReadable) {
    // returns an OptionalInt with the value of the index of the first Letter
    final OptionalInt firstLetterIndex =
        IntStream.range(0, humanReadable.length())
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
    if (ms == -1) {
      return permanent();
    }
    return new PunishDuration(ms);
  }

  public static PunishDuration of(final long time, final TimeUnit unit) {
    if (time == -1) {
      return permanent();
    }
    return new PunishDuration(unit.toMillis(time));
  }

  public static PunishDuration permanent() {
    return new PunishDuration(-1);
  }

  // ----------------------------------------------------------------------------------------------------
  // Convenience methods here
  // ----------------------------------------------------------------------------------------------------

  public boolean moreThan(final PunishDuration punishDuration) {
    if (isPermanent()) {
      return true;
    }

    if (punishDuration.isPermanent()) {
      return false;
    }

    return toMs() > punishDuration.toMs();
  }

  public boolean lessThan(final PunishDuration punishDuration) {
    return !moreThan(punishDuration);
  }

  public boolean isEmpty() {
    return toMs() == Long.MIN_VALUE;
  }

  public boolean isPermanent() {
    return ms == -1L;
  }

  public long createBanTime() {
    return System.currentTimeMillis() + ms;
  }

  @Override
  public String toString() {
    if (isPermanent()) {
      return "&cPermanent";
    }
    return TimeUtil.formatMenuDate(ms);
  }

  public long toMs() {
    return ms;
  }
}
