package org.mineacademy.punishcontrol.core.storage;

import de.leonhard.storage.util.Valid;
import lombok.NonNull;
import org.mineacademy.punishcontrol.core.punish.Ban;
import org.mineacademy.punishcontrol.core.punish.Mute;
import org.mineacademy.punishcontrol.core.punish.Warn;
import org.mineacademy.punishcontrol.core.storage.cache.MySQLPlayerCache;
import org.mineacademy.punishcontrol.core.storage.cache.PlayerCache;

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

	@Override public PlayerCache getFor(final @NonNull UUID uuid) {
		return new MySQLPlayerCache(uuid);
	}

	@Override
	public boolean isBanned(@NonNull final UUID uuid) {
		return false;
	}

	@Override
	public boolean isMuted(@NonNull final UUID uuid) {
		return false;
	}

	@Override
	public boolean isWarned(@NonNull final UUID uuid) {
		return false;
	}

	@Override
	public List<Ban> listCurrentBans() {
		return null;
	}

	@Override
	public List<Mute> listCurrentMutes() {
		return null;
	}

	@Override
	public List<Warn> listCurrentWarns() {
		return null;
	}

	@Override
	public List<Ban> listBans() {
		return null;
	}

	@Override
	public List<Mute> listMutes() {
		return null;
	}

	@Override
	public List<Warn> listWarns() {
		return null;
	}

	@Override
	public Ban currentBan(@NonNull final UUID uuid) {
		return null;
	}

	@Override
	public Mute currentMute(@NonNull final UUID uuid) {
		return null;
	}

	@Override
	public Warn currentWarn(@NonNull final UUID uuid) {
		return null;
	}

	@Override
	public List<Ban> listBans(@NonNull final UUID uuid) {
		return null;
	}

	@Override
	public List<Mute> listMutes(@NonNull final UUID uuid) {
		return null;
	}

	@Override
	public List<Warn> listWarns(@NonNull final UUID uuid) {
		return null;
	}

	@Override
	public void saveBan(@NonNull final Ban ban) {

	}

	@Override
	public void saveMute(@NonNull final Mute mute) {

	}

	@Override
	public void saveWarn(@NonNull final Warn warn) {

	}

	@Override public boolean removeCurrentBan(final @NonNull UUID target) {
		return false;
	}

	@Override public boolean removeCurrentMute(final @NonNull UUID target) {
		return false;
	}

	@Override public boolean removeCurrentWarn(final @NonNull UUID target) {
		return false;
	}
}
