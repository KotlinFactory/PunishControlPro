package org.mineacademy.punishcontrol.core.storage;

import lombok.experimental.UtilityClass;
import org.mineacademy.punishcontrol.core.provider.Providers;

@UtilityClass
public class StorageTypes {

  public final MySQLStorageProvider mySQLStorageProvider =
      Providers.storageProvider() instanceof MySQLStorageProvider
          ? (MySQLStorageProvider) Providers.storageProvider()
          : new MySQLStorageProvider(Providers.exceptionHandler());
}
