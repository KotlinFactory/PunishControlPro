package org.mineacademy.punishcontrol.core.punish;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum PunishType {
  BAN(true, false, "Ban"),
  MUTE(false, false, "Mute"),
  WARN(false, true, "Warn");

  private final boolean shouldKick;
  private final boolean shouldWarn;
  private final String localized;
}
