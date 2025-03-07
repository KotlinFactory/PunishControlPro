package org.mineacademy.punishcontrol.spigot.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.util.UUIDTypeAdapter;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.collection.expiringmap.ExpiringMap;

@UtilityClass
@SuppressWarnings("unchecked")
@FieldDefaults(makeFinal = true)
public class MojangUtils {

  private final String STEVE_TEXTURE = "ewogICJ0aW1lc3RhbXAiIDogMTU5MTU3NDcyMzc4MywKICAicHJvZmlsZUlkIiA6ICI4NjY3YmE3MWI4NWE0MDA0YWY1NDQ1N2E5NzM0ZWVkNyIsCiAgInByb2ZpbGVOYW1lIiA6ICJTdGV2ZSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS82ZDNiMDZjMzg1MDRmZmMwMjI5Yjk0OTIxNDdjNjlmY2Y1OWZkMmVkNzg4NWY3ODUwMjE1MmY3N2I0ZDUwZGUxIgogICAgfSwKICAgICJDQVBFIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS85NTNjYWM4Yjc3OWZlNDEzODNlNjc1ZWUyYjg2MDcxYTcxNjU4ZjIxODBmNTZmYmNlOGFhMzE1ZWE3MGUyZWQ2IgogICAgfQogIH0KfQ==";

  public final String SERVICE_URL = "https://sessionserver.mojang.com/session/minecraft/profile/";
  private final ExpiringMap<UUID, String> cache = ExpiringMap.builder()
      .expiration(3L, TimeUnit.DAYS).build();

  public String getTextureHash(final UUID uuid) {
    if (MojangUtils.cache.containsKey(uuid)) {
      return MojangUtils.cache.get(uuid);
    }

    final String hash = fetch(uuid);
    // Mojang failed?
    if (hash != null && !hash.isEmpty()) {
      cache.put(uuid, hash);
    }
    return hash;
  }

  public String fetch(final UUID uuid) {
    try {
      final String out = fetch0(uuid);
      if (isValid(out)) {
        cache.put(uuid, out);
        return out;
      }
    } catch (final Throwable throwable) {
      System.err.println(
          "If you aren't in online mode, disable it in your settings.yml as well!"
      );
      System.err.println("Using Steve-Texture as default");
      throwable.printStackTrace();
    }
    return STEVE_TEXTURE;
  }

  private String fetch0(final UUID uuid) throws Exception {
    final URL url_1 = new URL(
        SERVICE_URL + UUIDTypeAdapter
            .fromUUID(uuid)
        + "?unsigned=false");
    final InputStreamReader reader_1 = new InputStreamReader(url_1.openStream());
    final JsonObject textureProperty = new JsonParser().parse(reader_1).getAsJsonObject()
        .get("properties").getAsJsonArray().get(0).getAsJsonObject();

    return textureProperty.get("value").getAsString();
  }

  private boolean isValid(@Nullable final Object hash) {
    if (!(hash instanceof String)) {
      return false;
    }

    return !((String) hash).isEmpty();
  }
}

