package org.mineacademy.punishcontrol.core.handler;

import de.leonhard.storage.util.Valid;
import lombok.NonNull;

import java.util.UUID;

public abstract class AbstractHandler<T> implements Handler<T> {
  // ----------------------------------------------------------------------------------------------------
  // Needs to be set
  // ----------------------------------------------------------------------------------------------------
  private T event;
  private UUID target;

  public abstract void onEvent(T event);

  protected final T event() {
    Valid.notNull(
        event,
        "Accessing the event before it was set",
        "Make sure to set it in the event-handler-method before accessing it");

    return event;
  }

  protected final void event(@NonNull final T event) {
    this.event = event;
  }

  protected final UUID target() {
    Valid.notNull(
        target,
        "Accessing the target before it was set",
        "Make sure to set it in the event-handler-method before accessing it");

    return target;
  }

  protected final void target(@NonNull final UUID target) {
    this.target = target;
  }
}
