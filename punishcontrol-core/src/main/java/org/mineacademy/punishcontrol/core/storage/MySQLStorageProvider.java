package org.mineacademy.punishcontrol.core.storage;

import de.leonhard.storage.util.Valid;
import lombok.NonNull;
import org.mineacademy.punishcontrol.core.punish.Ban;
import org.mineacademy.punishcontrol.core.punish.Mute;
import org.mineacademy.punishcontrol.core.punish.Warn;

import java.util.List;
import java.util.UUID;

public final class MySQLStorageProvider extends SimpleDatabase implements StorageProvider {

	public static boolean connected() {
		return getInstance().isConnected();
	}

	private volatile static MySQLStorageProvider instance;

	private MySQLStorageProvider() {
		Valid.checkBoolean(instance == null, "We already have an instance of MySQLStorageProvider");
	}

	public static MySQLStorageProvider getInstance() {
		if (instance != null) {
			return instance;
		}
		return instance = new MySQLStorageProvider();
	}


	@Override public List<Ban> listBans() {
		return null;
	}

	@Override public List<Mute> listMutes() {
		return null;
	}

	@Override public List<Warn> listWarns() {
		return null;
	}

	@Override public List<Ban> listBans(@NonNull final UUID uuid) {
		return null;
	}

	@Override public List<Mute> listMutes(@NonNull final UUID uuid) {
		return null;
	}

	@Override public List<Warn> listWarns(@NonNull final UUID uuid) {
		return null;
	}

	@Override public void saveBan(@NonNull final Ban ban) {

	}

	@Override public void saveMute(@NonNull final Mute mute) {

	}

	@Override public void saveWarn(@NonNull final Warn warn) {

	}

	@Override public void removeBan(final @NonNull Ban ban) {

	}

	@Override public void removeMute(final @NonNull Mute mute) {

	}

	@Override public void removeWarn(final @NonNull Warn warn) {

	}
}
