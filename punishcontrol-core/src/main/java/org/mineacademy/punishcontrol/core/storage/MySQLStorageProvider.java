package org.mineacademy.punishcontrol.core.storage;

import de.leonhard.storage.util.Valid;
import lombok.NonNull;
import org.mineacademy.punishcontrol.core.punish.Ban;
import org.mineacademy.punishcontrol.core.punish.Mute;
import org.mineacademy.punishcontrol.core.report.Report;
import org.mineacademy.punishcontrol.core.warn.Warn;

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

	@Override
	public boolean isBanned(@NonNull final UUID uuid) {
		return false;
	}

	@Override
	public boolean isMuted(@NonNull final UUID uuid) {
		return false;
	}

	@Override
	public boolean isReported(@NonNull final UUID uuid) {
		return false;
	}

	@Override
	public boolean isWarned(@NonNull final UUID uuid) {
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

	@Override
	public void saveBan(@NonNull final Ban ban) {

	}

	@Override
	public void saveMute(@NonNull final Mute mute) {

	}

	@Override
	public void saveWarn(@NonNull final Warn warn) {

	}

	@Override
	public void saveReport(@NonNull final Report report) {

	}
}
