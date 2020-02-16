package org.mineacademy.punishcontrol.core.storage;

public enum StorageType {
	MYSQL {
		//TODO Cache results


		@Override
		public StorageProvider getStorageProvider() {
			return MySQLStorageProvider.getInstance();
		}
	},
	JSON {
		@Override
		public StorageProvider getStorageProvider() {
			return new JsonStorageProvider();
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
