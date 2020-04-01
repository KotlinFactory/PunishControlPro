package org.mineacademy.punishcontrol.core.events;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.mineacademy.punishcontrol.core.event.AbstractEvent;

@Getter
@Setter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ChatEvent extends AbstractEvent {

  private final UUID targetUUID;
  @NonNull
  private String message;

  public static ChatEvent create(final UUID targetUUID, final String message) {
    return new ChatEvent(targetUUID, message);
  }
}
