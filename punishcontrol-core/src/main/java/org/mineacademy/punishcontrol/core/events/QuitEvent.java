package org.mineacademy.punishcontrol.core.events;

import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.mineacademy.punishcontrol.core.event.AbstractEvent;

@RequiredArgsConstructor
public class QuitEvent extends AbstractEvent {

  private final UUID target;

  public static QuitEvent create(@NonNull final UUID target) {
    return new QuitEvent(target);
  }
}