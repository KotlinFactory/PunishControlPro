package org.mineacademy.punishcontrol.core.punish;

import de.leonhard.storage.internal.exceptions.LightningValidationException;
import de.leonhard.storage.util.Valid;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.mineacademy.punishcontrol.core.punishes.Ban;
import org.mineacademy.punishcontrol.core.punishes.Mute;
import org.mineacademy.punishcontrol.core.punishes.Warn;
import org.mineacademy.punishcontrol.core.settings.Localization;
import org.mineacademy.punishcontrol.core.settings.Settings;

/**
 * Utility class to interact with Punishes
 */
@UtilityClass
@SuppressWarnings("unchecked")
public class Punishes {

  public <T extends Punish> T convert(final Punish punish,
      @NonNull final Class<T> target) {
    if (target == Ban.class) {
      return (T)
          Ban.of(punish.target(), punish.creator(), punish.punishDuration())
              .reason(punish.reason())
              .creation(punish.creation())
              .ip(punish.ip().orElse("unknown"))
              .removed(punish.removed())
              .isSilent(punish.isSilent())
              .isSuperSilent(punish.isSuperSilent());
    } else if (target == Mute.class) {
      return (T)
          Mute.of(punish.target(), punish.creator(), punish.punishDuration())
              .reason(punish.reason())
              .creation(punish.creation())
              .ip(punish.ip().orElse("unknown"))
              .removed(punish.removed())
              .isSilent(punish.isSilent())
              .isSuperSilent(punish.isSuperSilent());

    } else if (target == Warn.class) {
      return (T)
          Warn.of(punish.target(), punish.creator(), punish.punishDuration())
              .creation(punish.creation())
              .reason(punish.reason())
              .ip(punish.ip().orElse("unknown"))
              .removed(punish.removed())
              .isSilent(punish.isSilent())
              .isSuperSilent(punish.isSuperSilent());
    }
    Valid.error(
        "Invalid class provided: " + target.getSimpleName(),
        "Package: " + target.getPackage());
    return null;
  }

  public String formKickedMessage(final String reason) {
    return reason;
  }

  /**
   * Form message to send to a player when the player was punished
   */
  public String formOnPunishMessage(final Punish punish) {

    switch (punish.punishType()) {

      case BAN:
        return "\n"+ Localization.Punish.BAN_MESSAGE.replace(
            punish.reason(),
            Settings.Advanced.formatDate(punish.getEndTime()))
            .replacedMessageJoined()
            .replace("&", "§");
      case MUTE:
        return "\n"+ Localization.Punish.MUTE_MESSAGE.replace(
            punish.reason() == null ? "unknown" : punish.reason(),
            Settings.Advanced.formatDate(punish.getEndTime()))
            .replacedMessageJoined()
            .replace("&", "§");
      case WARN:
        return "\n"+ Localization.Punish.WARN_MESSAGE.replace(
            punish.reason(),
            Settings.Advanced.formatDate(punish.getEndTime()))
            .replacedMessageJoined()
            .replace("&", "§");
    }


    throw new LightningValidationException(
        "Exception while fetching the ban message",
        "Have you altered the data?");
  }

  public String formPunishedMessage(final Punish punish) {
    return formOnPunishMessage(punish);
  }
}
