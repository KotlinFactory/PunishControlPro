package org.mineacademy.punishcontrol.core.storage.cache;

import lombok.NonNull;
import org.mineacademy.punishcontrol.core.punish.Ban;
import org.mineacademy.punishcontrol.core.punish.Mute;
import org.mineacademy.punishcontrol.core.punish.Warn;
import org.mineacademy.punishcontrol.core.report.Report;

import java.util.List;
import java.util.UUID;

public final class MySQLPlayerCache extends PlayerCache {

	public MySQLPlayerCache(@NonNull final UUID uuid) {
		super(uuid);
	}

	@Override
	public boolean isBanned() {
		return false;
	}

	@Override
	public boolean isMuted() {
		return false;
	}

	@Override
	public boolean isWarned() {
		return false;
	}

	@Override
	public boolean isReported() {
		return false;
	}

	@Override
	public Ban currentBan() {
		return null;
	}

	@Override
	public Mute currentMute() {
		return null;
	}

	@Override
	public Warn currentWarn() {
		return null;
	}

	@Override
	public Report currentReport() {
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
	public void ban(final Ban ban) {

	}

	@Override
	public void mute(final Mute mute) {

	}
}
