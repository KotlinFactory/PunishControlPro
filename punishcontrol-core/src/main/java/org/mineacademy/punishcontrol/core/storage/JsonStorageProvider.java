package org.mineacademy.punishcontrol.core.storage;

import de.leonhard.storage.Json;
import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.punishcontrol.core.PunishControlManager;
import org.mineacademy.punishcontrol.core.punish.Ban;
import org.mineacademy.punishcontrol.core.punish.Mute;
import org.mineacademy.punishcontrol.core.punish.Warn;
import org.mineacademy.punishcontrol.core.storage.cache.JsonPlayerCache;
import org.mineacademy.punishcontrol.core.storage.cache.PlayerCache;

import java.util.*;

/**
 * Class to save our
 * data in a JSON
 * for an example how the file looks like, just scrool to
 * the end of this file
 */
@SuppressWarnings("unchecked")
public final class JsonStorageProvider extends Json implements StorageProvider {

	private static final String BANS_PATH_PREFIX = "bans";
	private static final String MUTES_PATH_PREFIX = "mutes";
	private static final String WARN_PATH_PREFIX = "warns";

	//

	private static final String PATH_TO_BAN = BANS_PATH_PREFIX + ".{uuid}.{creation}";
	private static final String PATH_TO_MUTE = MUTES_PATH_PREFIX + ".{uuid}.{creation}";
	private static final String PATH_TO_WARN = WARN_PATH_PREFIX + ".{uuid}.{creation}";


	public JsonStorageProvider() {
		super(PunishControlManager.FILES.JSON_DATA_FILE_NAME, PunishControlManager.FILES.PLUGIN_FOLDER);
	}

	@Override public PlayerCache getFor(final @NonNull UUID uuid) {
		return new JsonPlayerCache(uuid);
	}

	@Override
	public boolean isBanned(@NonNull final UUID uuid) {
		return currentBan(uuid) != null;
	}

	@Override
	public boolean isMuted(@NonNull final UUID uuid) {
		return currentMute(uuid) != null;
	}

	@Override
	public boolean isWarned(@NonNull final UUID uuid) {
		return currentWarn(uuid) != null;
	}

	// ----------------------------------------------------------------------------------------------------
	// Listing current punishes
	// ----------------------------------------------------------------------------------------------------

	@Override
	@SuppressWarnings("unchecked")
	public List<Ban> listCurrentBans() {
		final Set<String> keys = singleLayerKeySet(BANS_PATH_PREFIX);
		final List<Ban> result = new ArrayList<>();

		for (final String key : keys) { //UUIDs
			final Map<String, Object> bans = getMap(key);

			for (final val entry : bans.entrySet()) { //MS
				//Actual ban

				final long creation = Long.parseLong(entry.getKey());
				final Map<String, Object> banRawData = (Map<String, Object>) entry.getValue();
				final Ban ban = Ban.ofRawData(creation, banRawData);

				if (!ban.isOld()) {
					result.add(ban);
				}
			}
		}

		return result;
	}

	@Override
	public List<Mute> listCurrentMutes() {
		final Set<String> keys = singleLayerKeySet(MUTES_PATH_PREFIX);
		final List<Mute> result = new ArrayList<>();

		for (final String key : keys) { //UUIDs
			final Map<String, Object> bans = getMap(key);

			for (final val entry : bans.entrySet()) { //MS
				//Actual ban

				final long creation = Long.parseLong(entry.getKey());
				final Map<String, Object> muteWarnData = (Map<String, Object>) entry.getValue();

				final Mute mute = Mute.ofRawData(creation, muteWarnData);

				if (!mute.isOld()) {
					result.add(mute);
				}
			}
		}

		return result;
	}

	@Override
	public List<Warn> listCurrentWarns() {
		final Set<String> keys = singleLayerKeySet(WARN_PATH_PREFIX);
		final List<Warn> result = new ArrayList<>();

		for (final String key : keys) { //UUIDs
			final Map<String, Object> bans = getMap(key);

			for (final val entry : bans.entrySet()) { //MS
				//Actual ban

				final long creation = Long.parseLong(entry.getKey());
				final Map<String, Object> warnRawData = (Map<String, Object>) entry.getValue();

				final Warn warn = Warn.ofRawData(creation, warnRawData);

				if (!warn.isOld()) {
					result.add(warn);
				}
			}
		}
		return result;
	}

	@Override
	public List<Ban> listBans() {
		final Set<String> keys = singleLayerKeySet(BANS_PATH_PREFIX);
		final List<Ban> result = new ArrayList<>();

		for (final String key : keys) { //UUIDs
			final Map<String, Object> bans = getMap(key);

			for (final val entry : bans.entrySet()) { //MS
				//Actual ban

				final long creation = Long.parseLong(entry.getKey());
				final Map<String, Object> banRawData = (Map<String, Object>) entry.getValue();

				final Ban ban = Ban.ofRawData(creation, banRawData);

				result.add(ban);
			}
		}

		return result;
	}

	@Override
	public List<Mute> listMutes() {
		final Set<String> keys = singleLayerKeySet(MUTES_PATH_PREFIX);
		final List<Mute> result = new ArrayList<>();

		for (final String key : keys) { //UUIDs
			final Map<String, Object> bans = getMap(key);

			for (final val entry : bans.entrySet()) { //MS
				//Actual ban

				final long creation = Long.parseLong(entry.getKey());
				final Map<String, Object> muteRawData = (Map<String, Object>) entry.getValue();

				final Mute mute = Mute.ofRawData(creation, muteRawData);

				result.add(mute);
			}
		}

		return result;
	}

	@Override
	public List<Warn> listWarns() {
		final Set<String> keys = singleLayerKeySet(WARN_PATH_PREFIX);
		final List<Warn> result = new ArrayList<>();

		for (final String key : keys) { //UUIDs
			final Map<String, Object> bans = getMap(key);

			for (final val entry : bans.entrySet()) { //MS
				//Actual ban

				final long creation = Long.parseLong(entry.getKey());
				final Map<String, Object> warnRawData = (Map<String, Object>) entry.getValue();


				final Warn warn = Warn.ofRawData(creation, warnRawData);

				result.add(warn);
			}
		}

		return result;
	}

	// ----------------------------------------------------------------------------------------------------
	// Listing all punishes of players
	// ----------------------------------------------------------------------------------------------------

	@Override
	public List<Ban> listBans(@NonNull final UUID uuid) {
		final List<Ban> result = new ArrayList<>();

		final Map<String, Object> bans = getMap(BANS_PATH_PREFIX + "." + uuid);

		for (final val entry : bans.entrySet()) { //MS
			//Actual ban

			final long creation = Long.parseLong(entry.getKey());
			final Map<String, Object> banRawData = (Map<String, Object>) entry.getValue();

			final Ban ban = Ban.ofRawData(creation, banRawData);

			result.add(ban);
		}

		return result;
	}

	@Override
	public List<Mute> listMutes(@NonNull final UUID uuid) {
		final List<Mute> result = new ArrayList<>();

		final Map<String, Object> bans = getMap(MUTES_PATH_PREFIX + "." + uuid);

		for (final val entry : bans.entrySet()) { //MS
			//Actual ban

			final long creation = Long.parseLong(entry.getKey());
			final Map<String, Object> muteRawData = (Map<String, Object>) entry.getValue();

			final Mute mute = Mute.ofRawData(creation, muteRawData);

			result.add(mute);
		}

		return result;
	}

	@Override
	public List<Warn> listWarns(@NonNull final UUID uuid) {
		final List<Warn> result = new ArrayList<>();

		final Map<String, Object> bans = getMap(WARN_PATH_PREFIX + "." + uuid);

		for (final val entry : bans.entrySet()) { //MS
			//Actual ban

			final long creation = Long.parseLong(entry.getKey());
			final Map<String, Object> warnRawData = (Map<String, Object>) entry.getValue();

			final Warn warn = Warn.ofRawData(creation, warnRawData);

			result.add(warn);
		}

		return result;
	}

	// ----------------------------------------------------------------------------------------------------
	// Listing current punishes of players
	// ----------------------------------------------------------------------------------------------------

	@Override
	@Nullable
	public Ban currentBan(@NonNull final UUID uuid) {
		for (final Ban ban : listBans(uuid)) {
			if (!ban.isOld()) {
				return ban;
			}
		}

		return null;
	}

	@Override
	@Nullable
	public Mute currentMute(@NonNull final UUID uuid) {
		for (final Mute mute : listMutes(uuid)) {
			if (!mute.isOld()) {
				return mute;
			}
		}
		return null;
	}

	@Override
	@Nullable
	public Warn currentWarn(@NonNull final UUID uuid) {
		for (final Warn warn : listWarns(uuid)) {
			if (!warn.isOld()) {
				return warn;
			}
		}

		return null;
	}


	// ----------------------------------------------------------------------------------------------------
	// Saving punishes
	// ----------------------------------------------------------------------------------------------------

	@Override
	public void saveBan(@NonNull final Ban ban) {
		final String path = PATH_TO_BAN
			.replace("{uuid}", ban.target().toString())
			.replace("{creation}", ban.creation() + "");

		set(path, ban.toMap());
	}

	@Override
	public void saveMute(@NonNull final Mute mute) {
		final String path = PATH_TO_MUTE
			.replace("{uuid}", mute.target().toString())
			.replace("{creation}", mute.creation() + "");

		set(path, mute.toMap());
	}

	@Override public void saveWarn(@NonNull final Warn warn) {
		final String path = PATH_TO_WARN
			.replace("{uuid}", warn.target().toString())
			.replace("{creation}", warn.creation() + "");

		set(path, warn.toMap());
	}

	// ----------------------------------------------------------------------------------------------------
	// Removing punishes
	// ----------------------------------------------------------------------------------------------------

	@Override public boolean removeCurrentBan(final @NonNull UUID target) {
		final Ban ban = currentBan(target);
		if (ban == null) {
			return false;
		}

		final String path = PATH_TO_BAN
			.replace("{uuid}", ban.target().toString())
			.replace("{creation}", ban.creation() + "");

		set(path + ".removed", true);
		return true;
	}

	@Override public boolean removeCurrentMute(final @NonNull UUID target) {
		final Mute mute = currentMute(target);
		if (mute == null) {
			return false;
		}

		final String path = PATH_TO_MUTE
			.replace("{uuid}", mute.target().toString())
			.replace("{creation}", mute.creation() + "");

		set(path + ".removed", true);
		return true;
	}

	@Override public boolean removeCurrentWarn(final @NonNull UUID target) {
		final Warn warn = currentWarn(target);
		if (warn == null) {
			return false;
		}


		final String path = PATH_TO_WARN
			.replace("{uuid}", warn.target().toString())
			.replace("{creation}", warn.creation() + "");

		set(path + ".removed", true);
		return true;
	}

	// ----------------------------------------------------------------------------------------------------
	// Example-File
	// ----------------------------------------------------------------------------------------------------

	/*
		{
		  "bans": {
		    "UUID": {
		      "MS": {
		        "creator": "UUID",
		        "target-name": "NAME",
		        "reason": "REASON",
		        "duration": 39393033093393033093039,
		        "removed": "false"
		      }
		    }
		  },
		  "mutes": {
		    "UUID": {
		      "MS": {
		        "creator": "UUID",
		        "target-name": "NAME",
		        "reason": "REASON",
		        "duration": 39393033093393033093039,
		        "removed": "false"
		      }
		    }
		  },
		  "warns": {
		    "UUID": {
		      "MS": {
		        "creator": "UUID",
		        "target-name": "NAME",
		        "reason": "REASON",
		        "duration": 39393033093393033093039,
		        "removed": "false"
		      }
		    }
		  }
		}
    */
}
