package org.mineacademy.punishcontrol.core.providers;

import de.leonhard.storage.util.Valid;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import lombok.val;
import org.mineacademy.punishcontrol.core.PunishControlManager;
import org.mineacademy.punishcontrol.core.flatfiles.SecureJson;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.uuid.UUIDs;

public abstract class AbstractPlayerProvider extends SecureJson implements
    PlayerProvider {

  public AbstractPlayerProvider() {
    super(
        PunishControlManager.FILES.UUID_STORAGE,
        Providers.pluginDataProvider().getDataFolder().getAbsolutePath()
            + "/data/");
  }

  @Override
  public final void saveData(
      @NonNull final UUID uuid, @NonNull final String name, final String ip) {
    fileData.insert(
        uuid.toString() + ".ip",
        ip); // Increasing performance -> Only writing to file 1x time.
    set(uuid.toString() + ".name", name);
  }

  @Override
  public final Optional<String> getIp(@NonNull final UUID uuid) {
    if (contains(uuid + ".ip")) {
      return Optional.of(getString(uuid + ".ip"));
    }

    return Optional.empty();
  }

  @Override
  public final String getName(@NonNull final UUID uuid) {
    if (contains(uuid.toString())) {
      return getString(uuid + ".name");
    } else {
      final String name = UUIDs.toName(uuid).orElse(null);
      Valid.notNull(name, "No player with UUID '" + uuid + "' found on Mojang-Side");
      set(uuid.toString() + ".name", name);
      return name;
    }
  }

  @Override
  public final UUID getUUID(@NonNull final String name) {
    for (final val entry : getFileData().toMap().entrySet()) {
      if (!(entry.getValue() instanceof Map)) {
        continue;
      }
      @SuppressWarnings("unchecked") final Map<String, Object> data = (Map<String, Object>) entry
          .getValue();
      if (data.get("name").toString().equalsIgnoreCase(name)) {
        return UUID.fromString(entry.getKey());
      }
    }

    // Not yet set.
    // Getting from Mojang & Setting it manually.

    final UUID uuid = UUIDs.find(name).orElse(null);

    Valid.notNull(uuid, "No player named '" + name + "' found on the mojang side");

    set(uuid.toString() + ".name", name);
    return uuid;
  }

  // ----------------------------------------------------------------------------------------------------
  // Overridden methods for usability & security
  // ----------------------------------------------------------------------------------------------------
}
