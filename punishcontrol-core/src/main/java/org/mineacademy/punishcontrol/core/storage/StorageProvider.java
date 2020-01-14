package org.mineacademy.punishcontrol.core.storage;

import lombok.NonNull;
import org.mineacademy.punishcontrol.core.punish.Ban;
import org.mineacademy.punishcontrol.core.punish.Mute;
import org.mineacademy.punishcontrol.core.report.Report;
import org.mineacademy.punishcontrol.core.warn.Warn;

import java.util.List;
import java.util.UUID;

public interface StorageProvider {

	boolean isBanned(@NonNull UUID uuid);

	boolean isMuted(@NonNull UUID uuid);

	boolean isReported(@NonNull UUID uuid);

	boolean isWarned(@NonNull UUID uuid);

	// ----------------------------------------------------------------------------------------------------
	// Listing all current punishes/warns/reports
	// ----------------------------------------------------------------------------------------------------

	List<Ban> listAllBans();

	List<Mute> listAllMutes();

	List<Warn> listAllWarns();

	List<Report> listAllReports();

	// ----------------------------------------------------------------------------------------------------
	// Listing all punishes/warns/reports ever made
	// ----------------------------------------------------------------------------------------------------

	List<Ban> listBans();

	List<Mute> listMutes();

	List<Warn> listWarns();

	List<Report> listReports();

	// ----------------------------------------------------------------------------------------------------
	// Methods to handle the data of specific players
	// ----------------------------------------------------------------------------------------------------

	Ban currentBan(@NonNull UUID uuid);

	Mute currentMute(@NonNull UUID uuid);

	Warn currentWarn(@NonNull UUID uuid);

	Report currentReport(@NonNull UUID uuid);

	// ----------------------------------------------------------------------------------------------------
	// List all punishes/warns/reports the player ever had
	// ----------------------------------------------------------------------------------------------------

	List<Ban> listBans(@NonNull UUID uuid);

	List<Mute> listMutes(@NonNull UUID uuid);

	List<Warn> listWarns(@NonNull UUID uuid);

	List<Report> listReports(@NonNull UUID uuid);

	// ----------------------------------------------------------------------------------------------------
	// Saving our punishes
	// ----------------------------------------------------------------------------------------------------

	void saveBan(@NonNull Ban ban);

	void saveMute(@NonNull Mute mute);

	void saveWarn(@NonNull Warn warn);

	void saveReport(@NonNull Report report);
}
