package org.mineacademy.punishcontrol.proxy.impl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mineacademy.bfo.Common;
import org.mineacademy.punishcontrol.core.providers.ExceptionHandler;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProxyExceptionHandler implements ExceptionHandler {

  public static ProxyExceptionHandler newInstance() {
    return new ProxyExceptionHandler();
  }

  @Override
  public void saveError(final Throwable throwable, final String... messages) {
    Common.error(throwable, messages);
  }
}
