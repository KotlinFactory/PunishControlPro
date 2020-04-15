package org.mineacademy.punishcontrol.spigot.events;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.mineacademy.fo.event.SimpleEvent;
import org.mineacademy.punishcontrol.core.punish.Punish;
import org.mineacademy.punishcontrol.core.punish.PunishType;

@Data
@EqualsAndHashCode(callSuper = true)
public final class AsyncPunishCreateEvent extends SimpleEvent {

  private final Punish punish;
  private boolean cancelled;

  private AsyncPunishCreateEvent(@NonNull final Punish punish) {
    this.punish = punish;
  }

  public static AsyncPunishCreateEvent create(final Punish punish) {
    return new AsyncPunishCreateEvent(punish);
  }

  public PunishType punishType() {
    return punish.punishType();
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return new HandlerList();
  }
}
