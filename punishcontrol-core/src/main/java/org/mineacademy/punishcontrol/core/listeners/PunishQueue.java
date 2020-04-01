package org.mineacademy.punishcontrol.core.listeners;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.mineacademy.punishcontrol.core.events.ChatEvent;
import org.mineacademy.punishcontrol.core.listener.Listener;
import org.mineacademy.punishcontrol.core.punish.Punish;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class PunishQueue implements Listener<ChatEvent> {
  private static final Map<UUID, Punish> punishMap = new HashMap<>();

  public static Map<UUID, Punish> punishMap(){
    return Collections.unmodifiableMap(punishMap);
  }

  public static PunishQueue create() {
    return new PunishQueue();
  }

  @Override
  public Class<ChatEvent> getClazz() {
    return ChatEvent.class;
  }

  @Override
  public void handleEvent(final ChatEvent event) {

  }
}
