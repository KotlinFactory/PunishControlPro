package org.mineacademy.punishcontrol.core.handler;

import lombok.NonNull;

import java.util.Arrays;
import java.util.List;

/**
 * A handler handles a specific event on the core-side (e.g Proxy&Spigot) but needs specific methods
 * to be implemented by spigot/proxy for this
 *
 * <p>The implementations have to listen to the event themselves
 *
 * @param <T> Event to handle
 */
public interface Handler<T> {
  void cancel();

  boolean canceled();

  void cancelReason(@NonNull final List<String> cancelReason);

  default void cancelReason(@NonNull final String... cancelReason) {
    cancelReason(Arrays.asList(cancelReason));
  }

  String cancelReason();
}
