package org.mineacademy.punishcontrol.core.providers;

import lombok.NonNull;
import org.mineacademy.punishcontrol.core.punish.Punish;

public interface PunishProvider {
  void broadCastPunishMessage(
      @NonNull final Punish punish, final boolean silent, final boolean superSilent);

  boolean handlePunishEvent(@NonNull final Punish punish);
}
