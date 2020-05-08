package org.mineacademy.punishcontrol.core;

import dagger.Component;
import org.mineacademy.punishcontrol.core.listeners.BanIpListener;
import org.mineacademy.punishcontrol.core.listeners.BanListener;
import org.mineacademy.punishcontrol.core.listeners.MuteIpListener;
import org.mineacademy.punishcontrol.core.listeners.MuteListener;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.storage.JsonStorageProvider;
import org.mineacademy.punishcontrol.core.storage.MySQLConfig;
import org.mineacademy.punishcontrol.core.storage.MySQLStorageProvider;

/**
 * Interface implemented by dagger
 */
@Component(modules = Providers.class)
public interface CoreComponent {

  MySQLConfig buildConfig();

  MySQLStorageProvider mySQLStorageProvider();

  JsonStorageProvider jsonStorageProvider();

  // ----------------------------------------------------------------------------------------------------
  // Listeners
  // ----------------------------------------------------------------------------------------------------

  BanListener banListener();

  MuteListener muteListener();

  BanIpListener banIpListener();

  MuteIpListener muteIpListener();
}
