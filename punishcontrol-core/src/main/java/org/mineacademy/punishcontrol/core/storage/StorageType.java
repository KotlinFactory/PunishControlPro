package org.mineacademy.punishcontrol.core.storage;


import org.mineacademy.punishcontrol.core.DaggerCoreComponent;

public enum StorageType {
  MYSQL {
    @Override
    public StorageProvider getStorageProvider() {
      if (storageProvider != null) {
        return storageProvider;
      }
      return storageProvider = DaggerCoreComponent.create().mySQLStorageProvider();
    }
  },
  JSON {
    @Override
    public StorageProvider getStorageProvider() {
      if (storageProvider != null) {
        return storageProvider;
      }
      return storageProvider = DaggerCoreComponent.create().jsonStorageProvider();
    }
  };

  private static StorageProvider storageProvider;

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
