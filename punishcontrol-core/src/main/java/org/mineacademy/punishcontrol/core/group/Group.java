package org.mineacademy.punishcontrol.core.group;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.mineacademy.punishcontrol.core.punish.PunishDuration;

@Getter
@Accessors(fluent = true)
@Builder
public final class Group {

  private final String name;
  private final String permission;
  private final String item = "STONE";
  private final int priority;

  private final boolean overridePunishes;

  private final PunishDuration banLimit;
  private final PunishDuration muteLimit;
  private final PunishDuration warnLimit;

  private final List<String> templateByPasses;
//
//  public String name() {
//    return name;
//  }
//
//  public String permission() {
//    return permission;
//  }
//
//  public String item() {
//    return item;
//  }
//
//  public int priority() {
//    return priority;
//  }
}
