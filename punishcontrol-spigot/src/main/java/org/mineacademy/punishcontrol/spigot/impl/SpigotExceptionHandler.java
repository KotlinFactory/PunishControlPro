package org.mineacademy.punishcontrol.spigot.impl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mineacademy.fo.Common;
import org.mineacademy.punishcontrol.core.providers.ExceptionHandler;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpigotExceptionHandler implements ExceptionHandler {

  public static SpigotExceptionHandler create() {
    return new SpigotExceptionHandler();
  }

  @Override
  public void saveError(final Throwable throwable, final String... messages) {
//    Notifications.register();
    Common.error(throwable, messages);
  }
}
