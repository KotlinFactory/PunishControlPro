package org.mineacademy.punishcontrol.core;

import dagger.Component;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.storage.JsonStorageProvider;
import org.mineacademy.punishcontrol.core.storage.MySQLConfig;
import org.mineacademy.punishcontrol.core.storage.MySQLStorageProvider;

import javax.inject.Singleton;

/** Interface implemented by dagger */
@Component(modules = Providers.class)
public interface CoreComponent {

  MySQLConfig buildConfig();

  @Singleton
  MySQLStorageProvider mySQLStorageProvider();

  @Singleton
  JsonStorageProvider jsonStorageProvider();
}
