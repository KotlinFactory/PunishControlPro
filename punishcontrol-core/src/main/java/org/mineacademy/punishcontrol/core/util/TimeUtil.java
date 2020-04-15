package org.mineacademy.punishcontrol.core.util;

import de.leonhard.storage.util.Valid;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;
import lombok.experimental.UtilityClass;

/**
 * Utility class for calculating time from ticks and back.
 */
@UtilityClass
public class TimeUtil {

  /**
   * The date format in dd.MM.yyy HH:mm:ss
   */
  private final DateFormat DATE_FORMAT = new SimpleDateFormat(
      "dd.MM.yyyy HH:mm:ss");
  /**
   * The date format in dd.MM.yyy HH:mm
   */
  private final DateFormat DATE_FORMAT_SHORT = new SimpleDateFormat(
      "dd.MM.yyyy HH:mm");

  // ------------------------------------------------------------------------------------------------------------
  // Current time
  // ------------------------------------------------------------------------------------------------------------

  /**
   * Seconds elapsed since January the 1st, 1970
   *
   * @return System.currentTimeMillis / 1000
   */
  public long currentTimeSeconds() {
    return System.currentTimeMillis() / 1000;
  }

  /**
   * Ticks elapsed since January the 1st, 1970
   *
   * @return System.currentTimeMillis / 50
   */
  public long currentTimeTicks() {
    return System.currentTimeMillis() / 50;
  }

  // ------------------------------------------------------------------------------------------------------------
  // Formatting
  // ------------------------------------------------------------------------------------------------------------

  /**
   * Return the current date formatted as DAY.MONTH.YEAR HOUR:MINUTES:SECONDS
   *
   * @return
   */
  public String getFormattedDate() {
    return getFormattedDate(System.currentTimeMillis());
  }

  /**
   * Return the given date in millis formatted as DAY.MONTH.YEAR
   * HOUR:MINUTES:SECONDS
   *
   * @param time
   * @return
   */
  public String getFormattedDate(final long time) {
    return DATE_FORMAT.format(time);
  }

  /**
   * Return the current date formatted as DAY.MONTH.YEAR HOUR:MINUTES
   *
   * @return
   */
  public String getFormattedDateShort() {
    return DATE_FORMAT_SHORT.format(System.currentTimeMillis());
  }

  /**
   * Return the given date in millis formatted as DAY.MONTH.YEAR HOUR:MINUTES
   *
   * @param time
   * @return
   */
  public String getFormattedDateShort(final long time) {
    return DATE_FORMAT_SHORT.format(time);
  }

  // ------------------------------------------------------------------------------------------------------------
  // Converting
  // ------------------------------------------------------------------------------------------------------------

  /**
   * Converts the time from a human readable format like "10 minutes" to
   * seconds.
   *
   * @param humanReadableTime the human readable time format: {time} {period}
   *                          example: 5 seconds, 10 ticks, 7 minutes, 12 hours
   *                          etc..
   * @return the converted human time to seconds
   */
  public long toTicks(final String humanReadableTime) {
    Valid.notNull(humanReadableTime, "Time is null");

    long seconds = 0L;

    final String[] split = humanReadableTime.split(" ");

    for (int i = 1; i < split.length; i++) {
      final String sub = split[i].toLowerCase();
      int multiplier = 0; // e.g 2 hours = 2
      long unit = 0; // e.g hours = 3600
      boolean isTicks = false;

      try {
        multiplier = Integer.parseInt(split[i - 1]);
      } catch (final NumberFormatException e) {
        continue;
      }

      // attempt to match the unit time
      if (sub.startsWith("tick")) {
        isTicks = true;
      } else if (sub.startsWith("second")) {
        unit = 1;
      } else if (sub.startsWith("minute")) {
        unit = 60;
      } else if (sub.startsWith("hour")) {
        unit = 3600;
      } else if (sub.startsWith("day")) {
        unit = 86400;
      } else if (sub.startsWith("week")) {
        unit = 604800;
      } else if (sub.startsWith("month")) {
        unit = 2629743;
      } else if (sub.startsWith("year")) {
        unit = 31556926;
      } else {
        //Invalid-format
        return Long.MIN_VALUE;
      }

      seconds += multiplier * (isTicks ? 1 : unit * 20);
    }

    return seconds;
  }

  public long toMS(final String humanReadableTime) {
    return toTicks(humanReadableTime) * 50;
  }

  /**
   * Formats the given time from seconds into the following format:
   *
   * <p>"1 hour 50 minutes 10 seconds" or similar, or less
   *
   * @param seconds
   * @return
   */
  public String formatTimeGeneric(final long seconds) {
    final long second = seconds % 60;
    long minute = seconds / 60;
    String hourMsg = "";

    if (minute >= 60) {
      final long hour = seconds / 60;
      minute %= 60;

      hourMsg = (hour == 1 ? "hour" : "hours") + " ";
    }

    return hourMsg
        + minute
        + (minute > 0 ? (minute == 1 ? " minute" : " minutes") + " " : "")
        + second
        + (second == 1 ? " second" : " seconds");
  }

  /**
   * Format time in "X days Y hours Z minutes Å seconds"
   *
   * @param seconds
   * @return
   */
  public String formatTimeDays(final long seconds) {
    final long minutes = seconds / 60;
    final long hours = minutes / 60;
    final long days = hours / 24;

    return days
        + " days "
        + hours % 24
        + " hours "
        + minutes % 60
        + " minutes "
        + seconds % 60
        + " seconds";
  }

  /**
   * Format the time in seconds, for example: 10d 5h 10m 20s or just 5m 10s If
   * the seconds are zero, an output 0s is given
   *
   * @param seconds
   * @return
   */
  public String formatTimeShort(long seconds) {
    long minutes = seconds / 60;
    long hours = minutes / 60;
    final long days = hours / 24;

    hours = hours % 24;
    minutes = minutes % 60;
    seconds = seconds % 60;

    return (days > 0 ? days + "d " : "")
        + (hours > 0 ? hours + "h " : "")
        + (minutes > 0 ? minutes + "m " : "")
        + seconds
        + "s";
  }

  public String formatMenuDate(final long duration) {
    return formatMenuDate(duration, true);
  }

  /**
   * Formats milliseconds so that they are easily to read in a menu-title
   *
   * @param duration Duration in milliseconds
   * @param isShort  Determines whether the title should be long or short
   * @return
   */
  public String formatMenuDate(final long duration, final boolean isShort) {
    if (duration == -1) {
      return "&cPermanent";
    }

    final long years = (duration / ((long) 1000 * 60 * 60 * 24 * 365));
    final long month = (duration / ((long) 1000 * 60 * 60 * 24 * 30) % 12);
    final long days = (duration / (1000 * 60 * 60 * 24) % 30);
    final long hours = (duration / (1000 * 60 * 60) % 24);
    String preString = years + " years, " +
        month + " month, " +
        days + " days, " +
        hours + " hours";

    System.out.println("Duration '" + duration + "'");
    System.out.println("PreString: " + preString);
    preString = preString.replace("0 years, ", "");
    preString = preString.replace("0 month, ", "");
    preString = preString.replace("0 days, ", "");
    preString = preString.replace("0 hours, ", "");
    if (preString.contains("years") && preString
        .contains("hours")) { //TODO Years, Month & days
      preString = "{years} years {month} month"
          .replace("{years}", years + "")
          .replace("{month}", month + "");
    }

    if (preString.contains("hours") && preString.contains("month")) {
      preString = "{month} month {days} days"
          .replace("{month}", month + "")
          .replace("{days}", days + "");
    }

    if (isShort && preString.contains("month") && preString.contains("days")
        && preString.contains("hours")) {
      preString = preString.replace(", 1 hours", "");
    }

    return preString;
  }

  public String format(long duration) {
    final long days = TimeUnit.MILLISECONDS.toDays(duration);
    duration -= TimeUnit.DAYS.toMillis(days);
    final long hours = TimeUnit.MILLISECONDS.toHours(duration);
    duration -= TimeUnit.HOURS.toMillis(hours);

    final StringBuilder sb = new StringBuilder(64);
    sb.append(days);
    sb.append(" Days ");
    sb.append(hours);
    sb.append(" Hours ");


    return (sb.toString());
  }
}
