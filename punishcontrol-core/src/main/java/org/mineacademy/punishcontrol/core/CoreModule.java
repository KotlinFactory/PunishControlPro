package org.mineacademy.punishcontrol.core;

import dagger.Component;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.storage.MySQLConfig;

/**
 * Interface implemented by dagger
 */
@Component(modules = Providers.class)
public interface CoreModule {

	MySQLConfig buildConfig();
}
