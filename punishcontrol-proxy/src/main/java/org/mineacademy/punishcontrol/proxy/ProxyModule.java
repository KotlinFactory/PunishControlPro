package org.mineacademy.punishcontrol.proxy;

import dagger.Component;
import org.mineacademy.punishcontrol.core.provider.Providers;

/** Interface implemented by dagger */
@Component(modules = Providers.class)
public interface ProxyModule {}
