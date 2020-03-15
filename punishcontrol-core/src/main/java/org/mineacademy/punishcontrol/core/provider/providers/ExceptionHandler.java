package org.mineacademy.punishcontrol.core.provider.providers;

public interface ExceptionHandler {
  void saveError(final Throwable throwable, final String... messages);
}
