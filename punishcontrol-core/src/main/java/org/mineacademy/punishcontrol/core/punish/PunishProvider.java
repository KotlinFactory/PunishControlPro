package org.mineacademy.punishcontrol.core.punish;

import lombok.NonNull;

public interface PunishProvider {
  void broadCastPunishMessage(
      @NonNull final Punish punish, final boolean silent, final boolean superSilent);

  boolean handlePunishEvent(@NonNull final Punish punish);
}
