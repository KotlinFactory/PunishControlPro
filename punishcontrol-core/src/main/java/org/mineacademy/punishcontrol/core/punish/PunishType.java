package org.mineacademy.punishcontrol.core.punish;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum PunishType {
  BAN(true, "Ban"),
  MUTE(false , "Mute"),
  WARN(true, "Warn");


  private final boolean shouldKick;
  private final String localized;
}
