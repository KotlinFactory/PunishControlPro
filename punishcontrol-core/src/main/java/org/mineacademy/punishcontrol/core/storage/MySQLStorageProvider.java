package org.mineacademy.punishcontrol.core.storage;

import lombok.NonNull;
import org.mineacademy.punishcontrol.core.punish.Ban;
import org.mineacademy.punishcontrol.core.punish.Mute;
import org.mineacademy.punishcontrol.core.report.Report;
import org.mineacademy.punishcontrol.core.warn.Warn;

import java.util.List;
import java.util.UUID;

public class MySQLStorageProvider extends SimpleDatabase implements StorageProvider {

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
