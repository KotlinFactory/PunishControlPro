package org.mineacademy.punishcontrol.core.storage;

import lombok.NonNull;
import org.mineacademy.punishcontrol.core.punish.Ban;
import org.mineacademy.punishcontrol.core.punish.Mute;
import org.mineacademy.punishcontrol.core.punish.Warn;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface StorageProvider {


	// ----------------------------------------------------------------------------------------------------
	// Listing all punishes/warns/reports ever made
	// ----------------------------------------------------------------------------------------------------

	List<Ban> listBans();

	List<Mute> listMutes();

	List<Warn> listWarns();


	// ----------------------------------------------------------------------------------------------------
	// Listing all current punishes/warns/reports
	// ----------------------------------------------------------------------------------------------------

	default List<Ban> listCurrentBans() {

		final List<Ban> bans = listBans();

		//Making our stream parallel on large collections
		final Stream<Ban> banStream = bans.size() > 40 ? bans.stream().parallel() : bans.stream();

		return banStream.filter((ban) -> !ban.isOld()).collect(Collectors.toList());
	}

	default List<Mute> listCurrentMutes() {

		final List<Mute> mutes = listMutes();

		//Making our stream parallel on large collections
		final Stream<Mute> warnStream = mutes.size() > 40 ? mutes.stream().parallel() : mutes.stream();

		return warnStream.filter((mute) -> !mute.isOld()).collect(Collectors.toList());
	}

	default List<Warn> listCurrentWarns() {
		final List<Warn> warns = listWarns();

		//Making our stream parallel on large collections
		final Stream<Warn> warnStream = warns.size() > 40 ? warns.stream().parallel() : warns.stream();

		return warnStream.filter((mute) -> !mute.isOld()).collect(Collectors.toList());
	}


	// ----------------------------------------------------------------------------------------------------
	//
	// Methods which needs UUID's/Punishes as argument
	//
	// ----------------------------------------------------------------------------------------------------


	// ----------------------------------------------------------------------------------------------------
	// List all punishes/warns/reports the player ever had
	// ----------------------------------------------------------------------------------------------------

	List<Ban> listBans(@NonNull UUID uuid);

	List<Mute> listMutes(@NonNull UUID uuid);

	List<Warn> listWarns(@NonNull UUID uuid);


	// ----------------------------------------------------------------------------------------------------
	// Methods to handle the data of specific players
	// ----------------------------------------------------------------------------------------------------

	default Optional<Ban> currentBan(@NonNull final UUID uuid) {

		for (final Ban ban : listBans(uuid)) {
			if (!ban.isOld())
				return Optional.of(ban);
		}
		return Optional.empty();
	}

	default Optional<Mute> currentMute(@NonNull final UUID uuid) {
		for (final Mute mute : listMutes(uuid)) {
			if (!mute.isOld())
				return Optional.of(mute);
		}

		return Optional.empty();
	}

	default Optional<Warn> currentWarn(@NonNull final UUID uuid) {
		for (final Warn warn : listWarns(uuid)) {
			if (!warn.isOld())
				return Optional.of(warn);
		}

		return Optional.empty();
	}


	// ----------------------------------------------------------------------------------------------------
	// Methods to check whether a player is banned
	// ----------------------------------------------------------------------------------------------------

	default boolean isBanned(@NonNull final UUID uuid) {
		return currentBan(uuid).isPresent();
	}

	default boolean isMuted(@NonNull final UUID uuid) {
		return currentMute(uuid).isPresent();
	}

	default boolean isWarned(@NonNull final UUID uuid) {
		return currentWarn(uuid).isPresent();
	}


	// ----------------------------------------------------------------------------------------------------
	// Saving & Removing our punishes
	// ----------------------------------------------------------------------------------------------------

	void saveBan(@NonNull Ban ban);

	void saveMute(@NonNull Mute mute);

	void saveWarn(@NonNull Warn warn);


	//Returns false if not punished

	void removeBan(@NonNull final Ban ban);

	void removeMute(@NonNull final Mute mute);

	void removeWarn(@NonNull final Warn warn);

	default boolean removeBanFor(@NonNull final UUID target) {
		final Optional<Ban> optionalBan = currentBan(target);

		if (!optionalBan.isPresent()) {
			return false;
		}

		final Ban ban = optionalBan.get();

		removeBan(ban);

		return true;
	}

	default boolean removeMuteFor(@NonNull final UUID target) {
		final Optional<Mute> optionalMute = currentMute(target);

		if (!optionalMute.isPresent()) {
			return false;
		}

		final Mute mute = optionalMute.get();

		removeMute(mute);

		return true;
	}

	default boolean removeWarnFor(@NonNull final UUID target) {
		final Optional<Warn> optionalWarn = currentWarn(target);

		if (!optionalWarn.isPresent()) {
			return false;
		}

		final Warn warn = optionalWarn.get();

		removeWarn(warn);

		return true;
	}
}
