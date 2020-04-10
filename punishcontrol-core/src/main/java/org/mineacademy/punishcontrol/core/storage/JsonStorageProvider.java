package org.mineacademy.punishcontrol.core.storage;

import de.leonhard.storage.util.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.inject.Inject;
import lombok.NonNull;
import lombok.val;
import org.mineacademy.punishcontrol.core.PunishControlManager;
import org.mineacademy.punishcontrol.core.flatfiles.SecureJson;
import org.mineacademy.punishcontrol.core.providers.ExceptionHandler;
import org.mineacademy.punishcontrol.core.providers.PluginDataProvider;
import org.mineacademy.punishcontrol.core.punishes.Ban;
import org.mineacademy.punishcontrol.core.punishes.Mute;
import org.mineacademy.punishcontrol.core.punishes.Warn;

/**
 * Class to save our data in a JSON for an example how the file looks like, just
 * scrool to the end of this file
 */

public final class JsonStorageProvider extends SecureJson implements StorageProvider {

  private static final String BANS_PATH_PREFIX = "bans";
  private static final String MUTES_PATH_PREFIX = "mutes";
  private static final String WARN_PATH_PREFIX = "warns";

  //

  private static final String PATH_TO_BAN =
      BANS_PATH_PREFIX + ".{uuid}.{creation}";
  private static final String PATH_TO_MUTE =
      MUTES_PATH_PREFIX + ".{uuid}.{creation}";
  private static final String PATH_TO_WARN =
      WARN_PATH_PREFIX + ".{uuid}.{creation}";
  private final ExceptionHandler exceptionHandler;

  //

  @Inject
  public JsonStorageProvider(final ExceptionHandler exceptionHandler,
      final PluginDataProvider workingDirectoryProvider) {
    super(
        PunishControlManager.FILES.JSON_DATA_FILE_NAME,
        workingDirectoryProvider.getDataFolder().getAbsolutePath() + "/data");
    this.exceptionHandler = exceptionHandler;
  }

  @Override
  public ExceptionHandler exceptionHandler() {
    return exceptionHandler;
  }

  private long parseMSFromKey(final String path, final String key) {
    try {
      return Long.parseLong(key);
    } catch (final NumberFormatException ex) {
      Valid.error(
          ex,
          "",
          "----",
          "Invalid configuration in your JSON-Punish-Storage",
          "String to parse is not numeric!",
          "Dir: " + getFilePath(),
          "Path in File: " + path + "." + key,
          "This is not an PunishControlPro error but an configuration mistake!",
          "----");
    }

    return 0;
  }

  @SuppressWarnings("unchecked")
  private Map<String, Object> parseMap(final String path,
      @NonNull final Object rawData) {
    try {
      return (Map<String, Object>) rawData;
    } catch (final ClassCastException ex) {
      Valid.error(
          ex,
          "",
          "----",
          "Invalid configuration in your JSON-Punish-Storage",
          "Expected type: Map<String, Object> found: " + rawData.getClass()
              .getSimpleName(),
          "Dir: " + getFilePath(),
          "Path in File: " + path,
          "This is not an PunishControlPro error but an configuration mistake!",
          "----");
    }

    return null;
  }

  // ----------------------------------------------------------------------------------------------------
  // Listing all punishes
  // ----------------------------------------------------------------------------------------------------

  @Override
  public List<Ban> listBans() {
    reloadIfNeeded();
    final Set<String> keys = singleLayerKeySet(BANS_PATH_PREFIX);
    final List<Ban> result = new ArrayList<>();

    for (final String key : keys) { // UUIDs
      final Map<String, Object> bans = getMap(BANS_PATH_PREFIX + "." + key);

      for (final val entry : bans.entrySet()) { // MS
        // Actual ban

        final long creation = parseMSFromKey(BANS_PATH_PREFIX + "." + key,
            entry.getKey());
        final Map<String, Object> banRawData = parseMap(key, entry.getValue());

        final Ban ban = Ban.ofRawData(creation, banRawData);

        result.add(ban);
      }
    }
    return result;
  }

  @Override
  public List<Mute> listMutes() {
    reloadIfNeeded();

    final Set<String> keys = singleLayerKeySet(MUTES_PATH_PREFIX);
    final List<Mute> result = new ArrayList<>();

    for (final String key : keys) { // UUIDs
      final Map<String, Object> bans = getMap(MUTES_PATH_PREFIX + "." + key);

      for (final val entry : bans.entrySet()) { // MS
        // Actual ban

        final long creation = parseMSFromKey(MUTES_PATH_PREFIX + "." + key,
            entry.getKey());
        final Map<String, Object> muteRawData = parseMap(key, entry.getValue());

        final Mute mute = Mute.ofRawData(creation, muteRawData);

        result.add(mute);
      }
    }

    return result;
  }

  @Override
  public List<Warn> listWarns() {
    reloadIfNeeded();

    final Set<String> keys = singleLayerKeySet(WARN_PATH_PREFIX);
    final List<Warn> result = new ArrayList<>();

    for (final String key : keys) { // UUIDs
      final Map<String, Object> bans = getMap(WARN_PATH_PREFIX + "." + key);

      for (final val entry : bans.entrySet()) { // MS
        // Actual ban

        final long creation = parseMSFromKey(WARN_PATH_PREFIX + "." + key,
            entry.getKey());
        final Map<String, Object> warnRawData = parseMap(key, entry.getValue());

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
    reloadIfNeeded();

    final List<Ban> result = new ArrayList<>();

    final Map<String, Object> bans = getMap(BANS_PATH_PREFIX + "." + uuid);

    for (final val entry : bans.entrySet()) { // MS
      // Actual ban

      final long creation = parseMSFromKey(BANS_PATH_PREFIX + "." + uuid,
          entry.getKey());
      final Map<String, Object> banRawData =
          parseMap(BANS_PATH_PREFIX + "." + uuid, entry.getValue());

      final Ban ban = Ban.ofRawData(creation, banRawData);

      result.add(ban);
    }

    return result;
  }

  @Override
  public List<Mute> listMutes(@NonNull final UUID uuid) {
    reloadIfNeeded();

    final List<Mute> result = new ArrayList<>();

    final Map<String, Object> bans = getMap(MUTES_PATH_PREFIX + "." + uuid);

    for (final val entry : bans.entrySet()) { // MS
      // Actual ban

      final long creation = parseMSFromKey(MUTES_PATH_PREFIX + "." + uuid,
          entry.getKey());
      final Map<String, Object> muteRawData =
          parseMap(MUTES_PATH_PREFIX + "." + uuid, entry.getValue());

      final Mute mute = Mute.ofRawData(creation, muteRawData);

      result.add(mute);
    }

    return result;
  }

  @Override
  public List<Warn> listWarns(@NonNull final UUID uuid) {
    reloadIfNeeded();

    final List<Warn> result = new ArrayList<>();

    final Map<String, Object> bans = getMap(WARN_PATH_PREFIX + "." + uuid);

    for (final val entry : bans.entrySet()) { // MS
      // Actual ban

      final long creation = parseMSFromKey(WARN_PATH_PREFIX + "." + uuid,
          entry.getKey());
      final Map<String, Object> warnRawData =
          parseMap(WARN_PATH_PREFIX + "." + uuid, entry.getValue());

      final Warn warn = Warn.ofRawData(creation, warnRawData);

      result.add(warn);
    }

    return result;
  }

  // ----------------------------------------------------------------------------------------------------
  // Saving punishes
  // ----------------------------------------------------------------------------------------------------

  @Override
  public void saveBan(@NonNull final Ban ban) {
    final String path =
        PATH_TO_BAN
            .replace("{uuid}", ban.target().toString())
            .replace("{creation}", ban.creation() + "");
    set(path, ban.toMap());
  }

  @Override
  public void saveMute(@NonNull final Mute mute) {
    final String path =
        PATH_TO_MUTE
            .replace("{uuid}", mute.target().toString())
            .replace("{creation}", mute.creation() + "");
    set(path, mute.toMap());
  }

  @Override
  public void saveWarn(@NonNull final Warn warn) {
    final String path =
        PATH_TO_WARN
            .replace("{uuid}", warn.target().toString())
            .replace("{creation}", warn.creation() + "");

    set(path, warn.toMap());
  }

  // ----------------------------------------------------------------------------------------------------
  // Removing punishes
  // ----------------------------------------------------------------------------------------------------

  @Override
  public void removeBan(final @NonNull Ban ban) {
    final String path =
        PATH_TO_BAN
            .replace("{uuid}", ban.target().toString())
            .replace("{creation}", ban.creation() + "");

    set(path + ".removed", true);
  }

  @Override
  public void removeMute(final @NonNull Mute mute) {
    final String path =
        PATH_TO_MUTE
            .replace("{uuid}", mute.target().toString())
            .replace("{creation}", mute.creation() + "");

    set(path + ".removed", true);
  }

  @Override
  public void removeWarn(final @NonNull Warn warn) {
    final String path =
        PATH_TO_WARN
            .replace("{uuid}", warn.target().toString())
            .replace("{creation}", warn.creation() + "");

    set(path + ".removed", true);
  }
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

