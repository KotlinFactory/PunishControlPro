package org.mineacademy.punishcontrol.core.storage;

import de.leonhard.storage.Json;
import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.punishcontrol.core.PunishControlManager;
import org.mineacademy.punishcontrol.core.punish.Ban;
import org.mineacademy.punishcontrol.core.punish.Mute;
import org.mineacademy.punishcontrol.core.punish.Punish;
import org.mineacademy.punishcontrol.core.punish.Warn;
import org.mineacademy.punishcontrol.core.storage.cache.JsonPlayerCache;
import org.mineacademy.punishcontrol.core.storage.cache.PlayerCache;

import java.util.*;

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

//We don't yet know any methods -> Will be added -> we don't want to yet implement them -> make class temporary abstract
public final class JsonStorageProvider extends Json implements StorageProvider {

	public JsonStorageProvider() {
		super(PunishControlManager.FILES.JSON_DATA_FILE_NAME, PunishControlManager.FILES.PLUGIN_FOLDER);
	}

	@Override public PlayerCache getFor(final @NonNull UUID uuid) {
		return new JsonPlayerCache(uuid);
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
	public boolean isWarned(@NonNull final UUID uuid) {
		return false;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Ban> listCurrentBans() {
		final Set<String> keys = singleLayerKeySet("bans");
		final List<Ban> result = new ArrayList<>();

		for (final String key : keys) { //UUIDs
			final Map<String, Object> bans = getMap(key);

			for (final val entry : bans.entrySet()) { //MS
				//Actual ban

				final Map<String, Object> banRawData = (Map<String, Object>) entry.getValue();

				final UUID target = UUID.fromString((String) banRawData.get("target"));
				final UUID creator = UUID.fromString((String) banRawData.get("creator"));
				final String reason = (String) banRawData.get("reason");
				final long duration = (long) banRawData.get("duration");

				final String ip = (String) banRawData.getOrDefault("ip", "unknown");

				final boolean removed = (boolean) banRawData.get("removed");

				final Ban ban = Ban
						.of(target, creator, duration)
						.removed(removed)
						.reason(reason)
						.ip(ip);

				if (!ban.isOld()) {
					result.add(ban);
				}
			}
		}

		return result;
	}

	@Override
	public List<Mute> listCurrentMutes() {
		final Set<String> keys = singleLayerKeySet("mutes");
		final List<Mute> result = new ArrayList<>();

		for (final String key : keys) { //UUIDs
			final Map<String, Object> bans = getMap(key);

			for (final val entry : bans.entrySet()) { //MS
				//Actual ban

				final Map<String, Object> banRawData = (Map<String, Object>) entry.getValue();

				final UUID target = UUID.fromString((String) banRawData.get("target"));
				final UUID creator = UUID.fromString((String) banRawData.get("creator"));
				final String reason = (String) banRawData.get("reason");
				final long duration = (long) banRawData.get("duration");

				final String ip = (String) banRawData.getOrDefault("ip", "unknown");

				final boolean removed = (boolean) banRawData.get("removed");

				final Mute mute = Mute
						.of(target, creator, duration)
						.removed(removed)
						.reason(reason);

				if (!mute.isOld()) {
					result.add(mute);
				}
			}
		}

		return result;
	}

	@Override
	public List<Warn> listCurrentWarns() {
		final Set<String> keys = singleLayerKeySet("warns");
		final List<Warn> result = new ArrayList<>();

		for (final String key : keys) { //UUIDs
			final Map<String, Object> bans = getMap(key);

			for (final val entry : bans.entrySet()) { //MS
				//Actual ban

				final Map<String, Object> banRawData = (Map<String, Object>) entry.getValue();

				final UUID target = UUID.fromString((String) banRawData.get("target"));
				final UUID creator = UUID.fromString((String) banRawData.get("creator"));
				final String reason = (String) banRawData.get("reason");
				final long duration = (long) banRawData.get("duration");

				final boolean removed = (boolean) banRawData.get("removed");


				final Warn warn = Warn
						.of(target, creator, duration)
						.removed(removed)
						.reason(reason);

				if (!warn.isOld()) {
					result.add(warn);
				}
			}
		}
		return result;
	}

	@Override
	public List<Ban> listBans() {
		final Set<String> keys = singleLayerKeySet("bans");
		final List<Ban> result = new ArrayList<>();

		for (final String key : keys) { //UUIDs
			final Map<String, Object> bans = getMap(key);

			for (final val entry : bans.entrySet()) { //MS
				//Actual ban

				final Map<String, Object> banRawData = (Map<String, Object>) entry.getValue();

				final UUID target = UUID.fromString((String) banRawData.get("target"));
				final UUID creator = UUID.fromString((String) banRawData.get("creator"));
				final String reason = (String) banRawData.get("reason");
				final long duration = (long) banRawData.get("duration");

				final boolean removed = (boolean) banRawData.get("removed");

				final Ban warn = Ban
						.of(target, creator, duration)
						.removed(removed)
						.reason(reason);

				result.add(warn);
			}
		}

		return result;
	}

	@Override
	public List<Mute> listMutes() {
		final Set<String> keys = singleLayerKeySet("bans");
		final List<Mute> result = new ArrayList<>();

		for (final String key : keys) { //UUIDs
			final Map<String, Object> bans = getMap(key);

			for (final val entry : bans.entrySet()) { //MS
				//Actual ban

				final Map<String, Object> banRawData = (Map<String, Object>) entry.getValue();

				final UUID target = UUID.fromString((String) banRawData.get("target"));
				final UUID creator = UUID.fromString((String) banRawData.get("creator"));
				final String reason = (String) banRawData.get("reason");
				final long duration = (long) banRawData.get("duration");
				final long creation = Long.parseLong(entry.getKey());

				final boolean removed = (boolean) banRawData.get("removed");

				final Mute warn = Mute
						.of(target, creator, duration)
						.removed(removed)
						.reason(reason);

				result.add(warn);
			}
		}

		return result;
	}

	@Override
	public List<Warn> listWarns() {
		final Set<String> keys = singleLayerKeySet("bans");
		final List<Warn> result = new ArrayList<>();

		for (final String key : keys) { //UUIDs
			final Map<String, Object> bans = getMap(key);

			for (final val entry : bans.entrySet()) { //MS
				//Actual ban

				final Map<String, Object> banRawData = (Map<String, Object>) entry.getValue();

				final UUID target = UUID.fromString((String) banRawData.get("target"));
				final UUID creator = UUID.fromString((String) banRawData.get("creator"));
				final String reason = (String) banRawData.get("reason");
				final long duration = (long) banRawData.get("duration");
				final long creation = Long.parseLong(entry.getKey());

				final boolean removed = (boolean) banRawData.get("removed");

				final Warn warn = Warn
						.of(target, creator, duration)
						.removed(removed)
						.reason(reason)
						.creation(creation);

				result.add(warn);
			}
		}

		return result;
	}

	@Override
	public Ban currentBan(@NonNull final UUID uuid) {
		final Map<String, Object> data = getMap("bans." + uuid);

		for (final val entry : data.entrySet()) {
			final long creation = Long.parseLong(entry.getKey());
			final Map<String, Object> banRawData = (Map<String, Object>) entry.getValue();


		}

		return null;

	}

	@Override
	@Nullable
	public Mute currentMute(@NonNull final UUID uuid) {
		return null;
	}

	@Override
	@Nullable
	public Warn currentWarn(@NonNull final UUID uuid) {
		return null;
	}

	@Override
	@Nullable
	public List<Ban> listBans(@NonNull final UUID uuid) {
		return null;
	}

	@Override
	@Nullable
	public List<Mute> listMutes(@NonNull final UUID uuid) {
		return null;
	}

	@Override
	@Nullable
	public List<Warn> listWarns(@NonNull final UUID uuid) {
		return null;
	}

	private void insertDefaultPunishData(@NonNull final String pathPrefix, @NonNull final Punish punish) {
		fileData.insert(pathPrefix + ".duration", punish.punishDuration().toMs());
		fileData.insert(pathPrefix + ".creator", punish.creator());
		fileData.insert(pathPrefix + ".target", punish.target());
		fileData.insert(pathPrefix + ".removed", punish.removed());
		fileData.insert(pathPrefix + ".target-name", "unknown");
	}

	@Override
	public void saveBan(@NonNull final Ban ban) {
		final String pathPrefix = "bans." + ban.target() + "." + ban.creation() + ".";

		insertDefaultPunishData(pathPrefix, ban);
		fileData.insert(pathPrefix + ".ip", ban.ip());

		write();
	}

	@Override
	public void saveMute(@NonNull final Mute mute) {
		final String pathPrefix = "mutes." + mute.target() + "." + mute.creation() + ".";

		insertDefaultPunishData(pathPrefix, mute);

		write();
	}

	@Override
	public void saveWarn(@NonNull final Warn warn) {
		final String pathPrefix = "warns." + warn.target() + "." + warn.creation() + ".";

		insertDefaultPunishData(pathPrefix, warn);

		write();
	}

	@Override public boolean removeCurrentBan(final @NonNull UUID target) {
		final Ban ban = currentBan(target);
		if (ban == null) {
			return false;
		}

		set("bans." + target + "." + ban.creation() + ".removed", true);
		return true;
	}

	@Override public boolean removeCurrentMute(final @NonNull UUID target) {
		final Mute mute = currentMute(target);
		if (mute == null) {
			return false;
		}

		set("mutes." + target + "." + mute.creation() + ".removed", true);
		return false;
	}

	@Override public boolean removeCurrentWarn(final @NonNull UUID target) {
		final Warn warn = currentWarn(target);
		if (warn == null) {
			return false;
		}

		set("warns." + target + "." + warn.creation() + ".removed", true);
		return true;
	}
}
