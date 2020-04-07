package org.mineacademy.punishcontrol.core.punish;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum PunishType {
  BAN(true),
  MUTE(true),
  WARN(true);

  private final boolean shouldKick;
}
