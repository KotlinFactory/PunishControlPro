package org.mineacademy.punishcontrol.core.storage;

import lombok.NonNull;
import org.mineacademy.punishcontrol.core.punish.Ban;
import org.mineacademy.punishcontrol.core.punish.Mute;
import org.mineacademy.punishcontrol.core.punish.Warn;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StorageProvider {

	boolean isBanned(@NonNull UUID uuid);

	boolean isMuted(@NonNull UUID uuid);

	boolean isWarned(@NonNull UUID uuid);

	// ----------------------------------------------------------------------------------------------------
	// Listing all current punishes/warns/reports
	// ----------------------------------------------------------------------------------------------------

	List<Ban> listCurrentBans();

	List<Mute> listCurrentMutes();

	List<Warn> listCurrentWarns();

	// ----------------------------------------------------------------------------------------------------
	// Listing all punishes/warns/reports ever made
	// ----------------------------------------------------------------------------------------------------

	List<Ban> listBans();

	List<Mute> listMutes();

	List<Warn> listWarns();

	// ----------------------------------------------------------------------------------------------------
	// Methods to handle the data of specific players
	// ----------------------------------------------------------------------------------------------------

	Optional<Ban> currentBan(@NonNull UUID uuid);

	Optional<Mute> currentMute(@NonNull UUID uuid);

	Optional<Warn> currentWarn(@NonNull UUID uuid);

	// ----------------------------------------------------------------------------------------------------
	// List all punishes/warns/reports the player ever had
	// ----------------------------------------------------------------------------------------------------

	List<Ban> listBans(@NonNull UUID uuid);

	List<Mute> listMutes(@NonNull UUID uuid);

	List<Warn> listWarns(@NonNull UUID uuid);

	// ----------------------------------------------------------------------------------------------------
	// Saving our punishes
	// ----------------------------------------------------------------------------------------------------

	void saveBan(@NonNull Ban ban);

	void saveMute(@NonNull Mute mute);

	void saveWarn(@NonNull Warn warn);

	// ----------------------------------------------------------------------------------------------------
	// Removing punishes
	// ----------------------------------------------------------------------------------------------------

	//Returns false if not punished

	boolean removeCurrentBan(@NonNull final UUID target);

	boolean removeCurrentMute(@NonNull final UUID target);

	boolean removeCurrentWarn(@NonNull final UUID target);
}
