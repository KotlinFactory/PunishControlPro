package org.mineacademy.punishcontrol.core.events;

import java.net.InetAddress;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.mineacademy.punishcontrol.core.event.AbstractEvent;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class JoinEvent extends AbstractEvent {

  private final UUID targetUUID;
  private final String name;
  private final InetAddress targetAddress;

  public static JoinEvent create(
      @NonNull final UUID targetUUID,
      @NonNull final String name,
      @NonNull final InetAddress targetInetAddress) {
    return new JoinEvent(targetUUID,name, targetInetAddress);
  }
}
