package org.mineacademy.punishcontrol.core.storage;

import org.mineacademy.punishcontrol.core.DaggerCoreModule;

public enum StorageType {
  MYSQL {
    @Override
    public StorageProvider getStorageProvider() {
      return DaggerCoreModule.create().mySQLStorageProvider();
    }
  },
  JSON {
    @Override
    public StorageProvider getStorageProvider() {
      return DaggerCoreModule.create().jsonStorageProvider();
    }
  };

  // ----------------------------------------------------------------------------------------------------
  // Static methods to get an instance of StorageType
  // ----------------------------------------------------------------------------------------------------

  public static StorageType find(final String name) {
    for (final StorageType value : values()) {
      if (value.name().equalsIgnoreCase(name)) {
        return value;
      }
    }

    return null;
  }

  // ----------------------------------------------------------------------------------------------------
  // Methods to override
  // ----------------------------------------------------------------------------------------------------

  public StorageProvider getStorageProvider() {
    throw new AbstractMethodError("Not implemented");
  }
}
