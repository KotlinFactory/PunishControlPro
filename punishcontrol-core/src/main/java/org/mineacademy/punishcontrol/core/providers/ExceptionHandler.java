package org.mineacademy.punishcontrol.core.providers;

public interface ExceptionHandler {
  void saveError(final Throwable throwable, final String... messages);
}
