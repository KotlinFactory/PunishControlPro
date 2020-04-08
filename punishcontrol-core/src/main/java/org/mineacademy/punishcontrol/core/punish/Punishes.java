package org.mineacademy.punishcontrol.core.punish;

import de.leonhard.storage.util.Valid;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.mineacademy.punishcontrol.core.punishes.Ban;
import org.mineacademy.punishcontrol.core.punishes.Mute;
import org.mineacademy.punishcontrol.core.punishes.Warn;

/** Utility class to interact with Punishes */
@UtilityClass
@SuppressWarnings("unchecked")
public class Punishes {
  public <T extends Punish> T convert(final Punish punish, @NonNull final Class<T> target) {
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
        "Invalid class provided: " + target.getSimpleName(), "Package: " + target.getPackage());
    return null;
  }

  /**
   * Form message to send to a player when the player was punished
   */
  public String formOnPunishMessage(final Punish punish){
    return punish.reason();
  }

  public String formPunishedMessage(final Punish punish){
    return punish.reason();
  }

}
