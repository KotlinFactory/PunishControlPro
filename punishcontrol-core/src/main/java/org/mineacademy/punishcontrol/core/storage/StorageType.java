package org.mineacademy.punishcontrol.core.storage;

import lombok.NonNull;
import org.mineacademy.punishcontrol.core.storage.cache.JsonPlayerCache;
import org.mineacademy.punishcontrol.core.storage.cache.MySQLPlayerCache;
import org.mineacademy.punishcontrol.core.storage.cache.PlayerCache;

import java.util.UUID;

public enum StorageType {
	MYSQL {
		//TODO Cache results

		@Override

		public PlayerCache getCacheFor(final @NonNull UUID uuid) {
			return new MySQLPlayerCache(uuid);
		}

		@Override
		public StorageProvider getStorageProvider() {
			return MySQLStorageProvider.getInstance();
		}
	},
	JSON {
		@Override
		public PlayerCache getCacheFor(final @NonNull UUID uuid) {
			return new JsonPlayerCache(uuid);
		}

		@Override
		public StorageProvider getStorageProvider() {
			return new JsonStorageProvider();
		}
	};

	// ----------------------------------------------------------------------------------------------------
	// Static methods to get an instance of StorageType
	// ----------------------------------------------------------------------------------------------------

	public static StorageType find(@NonNull final String name) {
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


	public PlayerCache getCacheFor(@NonNull final UUID uuid) {
		throw new AbstractMethodError("Not implemented");
	}

	public StorageProvider getStorageProvider() {
		throw new AbstractMethodError("Not implemented");
	}


}
