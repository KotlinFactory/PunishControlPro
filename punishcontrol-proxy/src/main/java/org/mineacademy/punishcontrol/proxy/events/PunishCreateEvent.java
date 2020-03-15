package org.mineacademy.punishcontrol.proxy.events;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;
import org.mineacademy.punishcontrol.core.punish.Punish;

@Getter
@Setter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PunishCreateEvent extends Event implements Cancellable {

  private final Punish punish;
  private boolean cancelled;

  public static PunishCreateEvent newInstance(final Punish punish) {
    return new PunishCreateEvent(punish);
  }
}
