package org.mineacademy.punishcontrol.core.storage.cache;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.mineacademy.punishcontrol.core.punish.Ban;
import org.mineacademy.punishcontrol.core.punish.Mute;
import org.mineacademy.punishcontrol.core.punish.Punish;
import org.mineacademy.punishcontrol.core.report.Report;
import org.mineacademy.punishcontrol.core.warn.Warn;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class PlayerCache {
	private final @NonNull UUID uuid;

	// ----------------------------------------------------------------------------------------------------
	// Is the player banned/warned/muted?
	// ----------------------------------------------------------------------------------------------------

	public abstract boolean isBanned();

	public abstract boolean isMuted();

	public abstract boolean isWarned();

	public abstract boolean isReported();

	// Methods to get current Punishes/Warns/Reports. Will return null if there is no punish

	public abstract Ban currentBan();

	public abstract Mute currentMute();

	public abstract Warn currentWarn();

	public abstract Report currentReport();


	//Methods to list all old punishes/warns/reports
	public abstract List<Ban> listBans();

	public abstract List<Mute> listMutes();

	public abstract List<Warn> listWarns();

	public abstract List<Report> listReports();

	public abstract void ban(@NonNull Ban ban);

	public abstract void mute(@NonNull Mute mute);

	public final List<Punish> listPunishes() {
		final List<Punish> result = new ArrayList<>();
		result.addAll(listBans());
		result.addAll(listMutes());
		return result;
	}
}
