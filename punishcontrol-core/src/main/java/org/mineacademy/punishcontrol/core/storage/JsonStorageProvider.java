package org.mineacademy.punishcontrol.core.storage;

import de.leonhard.storage.Json;
import lombok.NonNull;
import lombok.val;
import org.mineacademy.punishcontrol.core.PunishControlManager;
import org.mineacademy.punishcontrol.core.punish.Ban;
import org.mineacademy.punishcontrol.core.punish.Mute;
import org.mineacademy.punishcontrol.core.punish.Warn;
import org.mineacademy.punishcontrol.core.report.Report;
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
        "duration": 39393033093393033093039
      }
    }
  },
  "mutes": {
    "UUID": {
      "MS": {
        "creator": "UUID",
        "target-name": "NAME",
        "reason": "REASON",
        "duration": 39393033093393033093039
      }
    }
  },
  "warns": {
    "UUID": {
      "MS": {
        "creator": "UUID",
        "target-name": "NAME",
        "reason": "REASON",
        "duration": 39393033093393033093039
      }
    }
  },
  "reports": {
    "UUID": {
      "MS": {
        "creator": "UUID",
        "target-name": "NAME",
        "reason": "REASON",
        "duration": 39393033093393033093039
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
	public boolean isReported(@NonNull final UUID uuid) {
		return false;
	}

	@Override
	public boolean isWarned(@NonNull final UUID uuid) {
		return false;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Ban> listAllBans() {
		final Set<String> keys = singleLayerKeySet("bans");
		final List<Ban> result = new ArrayList<>();

		for (final String key : keys) { //UUIDs
			final Map<String, Object> bans = getMap(key);

			for (final val entry : bans.entrySet()) { //MS
				//Actual ban
				System.out.println(bans);

			}
		}

		return result;
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
