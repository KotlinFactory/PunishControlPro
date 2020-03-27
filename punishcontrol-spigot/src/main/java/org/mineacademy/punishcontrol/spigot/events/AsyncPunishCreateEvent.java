package org.mineacademy.punishcontrol.spigot.events;

import lombok.Data;
import lombok.NonNull;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.mineacademy.punishcontrol.core.punish.Punish;
import org.mineacademy.punishcontrol.core.punish.PunishType;

@Data
public class AsyncPunishCreateEvent extends Event implements Cancellable {
  private final Punish punish;
  private boolean cancelled;

  private AsyncPunishCreateEvent(@NonNull final Punish punish) {
    super(true);
    this.punish = punish;
  }

  public static AsyncPunishCreateEvent newInstance(final Punish punish) {
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
