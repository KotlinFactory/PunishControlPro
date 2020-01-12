package org.mineacademy.punishcontrol.core.storage;

import de.leonhard.storage.Json;
import lombok.NonNull;
import org.mineacademy.punishcontrol.core.PunishControlManager;
import org.mineacademy.punishcontrol.core.punish.Ban;
import org.mineacademy.punishcontrol.core.punish.Mute;
import org.mineacademy.punishcontrol.core.report.Report;
import org.mineacademy.punishcontrol.core.storage.cache.PlayerCache;
import org.mineacademy.punishcontrol.core.warn.Warn;

import java.util.List;
import java.util.UUID;

//We don't yet know any methods -> Will be added -> we don't want to yet implement them -> make class temporary abstract
public final class JsonStorageProvider extends Json implements StorageProvider {

	public JsonStorageProvider() {
		super(PunishControlManager.FILES.JSON_DATA_FILE_NAME, PunishControlManager.FILES.PLUGIN_FOLDER);
	}

	public static PlayerCache getFor(@NonNull final UUID uuid) {
		return PunishControlManager.storageType().getCacheFor(uuid);
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
	public List<Ban> listAllBans() {
		return null;
	}

	@Override
	public List<Mute> listAllMutes() {
		return null;
	}

	@Override
	public List<Warn> listAllWarns() {
		return null;
	}

	@Override
	public List<Report> listAllReports() {
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
	public List<Report> listReports() {
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
	public Report currentReport(@NonNull final UUID uuid) {
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
	public List<Report> listReports(@NonNull final UUID uuid) {
		return null;
	}
}
