package org.mineacademy.punishcontrol.core.punish;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum PunishType {
  BAN(true, false, false, "Ban"),
  MUTE(false, false, true, "Mute"),
  WARN(false, true, false, "Warn");

  // Should the punished player be kicked?
  private final boolean shouldKick;
  // Should the punished player be warned?
  private final boolean shouldWarn;
  // Should the punished player receive a message?
  private final boolean shouldMessage;
  private final String localized;
}
