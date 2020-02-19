package org.mineacademy.punishcontrol.core.storage;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.mineacademy.punishcontrol.core.PunishControlManager;
import org.mineacademy.punishcontrol.core.punish.Ban;
import org.mineacademy.punishcontrol.core.punish.Mute;
import org.mineacademy.punishcontrol.core.punish.Punish;
import org.mineacademy.punishcontrol.core.punish.Warn;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class PlayerCache {

	public static PlayerCache getFor(@NonNull final UUID uuid) {
		return new PlayerCache(uuid);
	}

	//Dagger?
	private final StorageProvider provider = PunishControlManager.storageType().getStorageProvider();
	private final @NonNull UUID uuid;

	// ----------------------------------------------------------------------------------------------------
	// Is the player banned/warned/muted?
	// ----------------------------------------------------------------------------------------------------

	public boolean isBanned() {
		return provider.isBanned(uuid);
	}

	public boolean isMuted() {
		return provider.isMuted(uuid);
	}

	public boolean isWarned() {
		return provider.isWarned(uuid);
	}

	// Methods to get current Punishes/Warns/Reports. Will return null if there is no punish

	public Optional<Ban> currentBan() {
		return provider.currentBan(uuid);
	}

	public Optional<Mute> currentMute() {
		return provider.currentMute(uuid);
	}

	public Optional<Warn> currentWarn() {
		return provider.currentWarn(uuid);
	}

	//Methods to list all old punishes/warns/reports
	public List<Ban> listBans() {
		return provider.listBans(uuid);
	}

	public List<Mute> listMutes() {
		return provider.listMutes(uuid);
	}

	public List<Warn> listWarns() {
		return provider.listWarns(uuid);
	}


	public void ban(final Ban ban) {
		provider.saveBan(ban);
	}

	public void mute(final Mute mute) {
		provider.saveMute(mute);
	}

	public void warn(final Warn warn) {
		provider.saveWarn(warn);
	}

	public final List<Punish> listPunishes() {
		final List<Punish> result = new ArrayList<>();
		result.addAll(listBans());
		result.addAll(listMutes());
		return result;
	}
}
