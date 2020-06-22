package org.mineacademy.punishcontrol.core.uuid;

import com.google.common.base.Charsets;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.mineacademy.punishcontrol.core.settings.Settings.Advanced;
import org.mineacademy.punishcontrol.core.util.LoadingCache;

@UtilityClass
public class UUIDs {

  private final LoadingCache<UUID, String> STRING_UUID_CACHE = new LoadingCache<UUID, String>(
      30, TimeUnit.MINUTES) {
    @Override
    public String load(final UUID uuid) {
      return UUIDFetcher.getName(uuid);
    }
  };

  private final LoadingCache<String, UUID> UUID_STRING_CACHE = new LoadingCache<String, UUID>(
      30, TimeUnit.MINUTES) {
    @Override
    public UUID load(final String s) {
      return UUIDFetcher.getUUID(s);
    }
  };

  public Optional<UUID> find(@NonNull final String name) {
    if (!Advanced.ONLINE_MODE) {
      return Optional
          .of(UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(Charsets.UTF_8)));
    }
    return Optional.ofNullable(UUID_STRING_CACHE.get(name));
  }

  public Optional<String> toName(@NonNull final UUID uuid) {
    if (!Advanced.ONLINE_MODE) {
      throw new AbstractMethodError("toName() can't be used in offline-mode");
    }
    return Optional.ofNullable(STRING_UUID_CACHE.get(uuid));
  }
}
