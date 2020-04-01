package org.mineacademy.punishcontrol.core.listener;

import org.mineacademy.punishcontrol.core.event.Event;

public interface Listener<T extends Event> {
  Class<T> getClazz();

  void handleEvent(T event);
}
