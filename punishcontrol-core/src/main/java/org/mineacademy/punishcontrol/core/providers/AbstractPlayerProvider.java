package org.mineacademy.punishcontrol.core.providers;

import java.util.*;
import lombok.NonNull;
import lombok.val;
import org.mineacademy.punishcontrol.core.PunishControlManager;
import org.mineacademy.punishcontrol.core.Searcher;
import org.mineacademy.punishcontrol.core.Searcher.SearchResult;
import org.mineacademy.punishcontrol.core.flatfiles.SecureJson;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.uuid.UUIDs;

public abstract class AbstractPlayerProvider
    extends SecureJson
    implements PlayerProvider {

  public AbstractPlayerProvider() {
    super(
        PunishControlManager.FILES.UUID_STORAGE,
        Providers.pluginDataProvider().getDataFolder().getAbsolutePath()
        + "/data/");
  }

  @Override
  public final List<UUID> offlinePlayers() {
    final List<UUID> result = new ArrayList<>();

    for (final String key : fileData.singleLayerKeySet())
      try {
        result.add(UUID.fromString(key));
      } catch (final Throwable throwable) {
        System.err.println("Exception while parsing: '" + key + "' to UUID");
        System.err.println("Have you altered the data?");
        System.err.println("Skipping it...");
        throwable.printStackTrace();
      }

    return result;
  }

  public final List<String> playerNames() {
    final List<String> result = new ArrayList<>();

    for (final String key : fileData.singleLayerKeySet())
      try {
        result.add(getString(key + ".name"));
      } catch (final Throwable throwable) {
        System.err.println("Exception while parsing: '" + key + "' to UUID");
        System.err.println("Have you altered the data?");
        System.err.println("Skipping it...");
        throwable.printStackTrace();
      }

    return result;
  }

  @Override
  public final void saveData(
      @NonNull final UUID uuid,
      @NonNull final String name,
      @NonNull final String ip) {
    // Increasing performance -> Only writing to file 1x time.
    fileData.insert(uuid.toString() + ".ip", ip);
    set(uuid.toString() + ".name", name);
  }

  @Override
  public final Optional<String> ip(@NonNull final UUID uuid) {
    if (contains(uuid + ".ip"))
      return Optional.of(getString(uuid + ".ip"));

    return Optional.empty();
  }

  @Override
  public Optional<String> findName(@NonNull final UUID uuid) {
    if (contains(uuid.toString()))
      return Optional.ofNullable(getString(uuid + ".name"));
    else {
      if (!PunishControlManager.isOnlineMode())
        return Optional.empty();

      //Not yet in file
      final String name = UUIDs.toName(uuid).orElse(null);
      if (name != null)
        set(uuid.toString() + ".name", name);
      return Optional.ofNullable(name);
    }
  }

  @Override
  public final Optional<UUID> findUUID(@NonNull final String name) {
    for (final val entry : getFileData().toMap().entrySet()) {
      if (!(entry.getValue() instanceof Map))
        continue;
      final Map<String, Object> data = (Map<String, Object>) entry
          .getValue();
      if (data.get("name").toString().equalsIgnoreCase(name))
        return Optional.of(UUID.fromString(entry.getKey()));
    }

    //Not yet in file
    if (!PunishControlManager.isOnlineMode())
      return Optional.empty();
    final UUID uuid = UUIDs.find(name).orElse(null);
    if (uuid != null)
      set(uuid.toString() + ".name", name);
    return Optional.ofNullable(uuid);
  }

  @Override
  public void punishable(
      @NonNull final UUID target,
      @NonNull final boolean punishable) {
    set(target + ".punishable", punishable);
  }

  @Override
  public boolean punishable(@NonNull final UUID target) {
    return getOrSetDefault(target + ".punishable", true);
  }

  @Override
  public List<UUID> searchForUUIDsOfIp(@NonNull final String hostAddress) {
    final List<UUID> result = new ArrayList<>();

    for (final UUID uuid : offlinePlayers()) {
      final String ip = ip(uuid).orElse("unknown");

      if (hostAddress.equalsIgnoreCase(ip))
        result.add(uuid);
    }

    return result;
  }

  @Override
  public List<SearchResult> search(final String partial) {
    return Searcher.search(partial, playerNames());
  }
}
