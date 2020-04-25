package org.mineacademy.punishcontrol.spigot.util;

import com.mojang.util.UUIDTypeAdapter;
import de.leonhard.storage.internal.FileData;
import de.leonhard.storage.shaded.json.JSONException;
import de.leonhard.storage.shaded.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.collection.expiringmap.ExpiringMap;
import org.mineacademy.fo.debug.Debugger;

@UtilityClass
@SuppressWarnings("unchecked")
@FieldDefaults(makeFinal = true)
public class MojangUtils {

  private final String SERVICE_URL =
      "https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false";
  private final ExpiringMap<UUID, String> cache =
      ExpiringMap.builder().expiration(3L, TimeUnit.HOURS).build();

  @SneakyThrows
  public String getTextureHash(final UUID uuid) {
    if (MojangUtils.cache.containsKey(uuid)) {
      return MojangUtils.cache.get(uuid);
    }

    final String hash = fetch(uuid);

    if (isValid(hash)) {
      final ExpiringMap<UUID, String> cache = MojangUtils.cache;
      cache.put(uuid, hash);
    }
    return hash;
  }

  private String fetch(final UUID uuid) throws IOException {
    try {
      final HttpURLConnection con =
          (HttpURLConnection)
              new URL(String.format(SERVICE_URL, UUIDTypeAdapter.fromUUID(uuid)))
                  .openConnection();
      con.setReadTimeout(5000);

      if (con.getResponseCode() != HttpURLConnection.HTTP_OK) {
        System.out.println(con.getResponseMessage());
        return null;
      }

      final String jsonString =
          new BufferedReader(new InputStreamReader(con.getInputStream()))
              .readLine();

      final JSONObject jsonObject = new JSONObject(jsonString);
      final FileData fileData = new FileData(jsonObject);
      final List<?> list = (List<?>) fileData.get("properties");

      if (list.size() != 1) {
        return null;
      }

      final Map<String, Object> result = (Map<String, Object>) list.get(0);

      final Object hash = result.get("value");

      if (hash != null) {
        cache.put(uuid, hash.toString());
        return (String) hash;
      }
    } catch (final JSONException ex) {
      Debugger.debug("Hash","Mojang-API is not working probarly");
    } catch (final Throwable throwable) {
      Debugger.saveError(throwable, "Exception while saving data");
    }
    return null;
  }

  private boolean isValid(@Nullable final Object hash) {

    if (!(hash instanceof String)) {
      return false;
    }

    return !((String) hash).isEmpty();
  }
}
