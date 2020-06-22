package org.mineacademy.punishcontrol.core.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.mineacademy.punishcontrol.core.listener.Listener;
import org.mineacademy.punishcontrol.core.listener.Listeners;

/**
 * Central utility class for handling events
 */
@UtilityClass
@SuppressWarnings({"rawtypes", "unchecked"})
public class Events {

  private final List<Event> knownEvents = new ArrayList<>();

  public void add(final Event event) {
    knownEvents.add(event);
  }

  public List<Event> knownEvents() {
    return Collections.unmodifiableList(knownEvents);
  }

  /**
   * Called from Proxy/Spigot-ListenerImpl
   */
  public <T extends Event> T call(final T event) {
    for (final Listener listener : Listeners.registeredListeners()) {
      if (listener.getClazz() != event.getClass()) {
        continue;
      }

      listener.handleEvent(event);
      if (event.canceled()) {
        return event;
      }
    }

    return event;
  }
}
