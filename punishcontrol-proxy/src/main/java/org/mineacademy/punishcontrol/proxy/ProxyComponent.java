package org.mineacademy.punishcontrol.proxy;

import dagger.Component;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.proxy.listeners.ProxyDataSetter;

/** Interface implemented by dagger */
@Component(modules = {Providers.class, ProxyModule.class})
public interface ProxyComponent {

  // ----------------------------------------------------------------------------------------------------
  // Listener
  // ----------------------------------------------------------------------------------------------------
  ProxyDataSetter proxyDataSetter();
}
