package org.mineacademy.punishcontrol.core.uuid;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * Internally, do NOT use!
 */
class UUIDFetcher {

  /**
   * Date when name changes were introduced
   *
   * @see UUIDFetcher#getUUIDAt(String, long)
   */
  public static final long FEBRUARY_2015 = 1422748800000L;
  private static final String UUID_URL = "https://api.mojang.com/users/profiles/minecraft/%s?at=%d";
  private static final String NAME_URL = "https://api.mojang.com/user/profiles/%s/names";
  private static final Gson gson = new GsonBuilder()
      .registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).create();
  private static final ExecutorService pool = Executors.newCachedThreadPool();

  private String name;
  private UUID id;

  /**
   * Fetches the uuid asynchronously and passes it to the consumer
   *
   * @param name   The name
   * @param action Do what you want to do with the uuid her
   */
  public static void getUUID(final String name, final Consumer<UUID> action) {
    pool.execute(() -> action.accept(getUUID(name)));
  }

  /**
   * Fetches the uuid synchronously and returns it
   *
   * @param name The name
   * @return The uuid
   */
  public static UUID getUUID(final String name) {
    return getUUIDAt(name, System.currentTimeMillis());
  }

  /**
   * Fetches the uuid synchronously for a specified name and time and passes the result to
   * the consumer
   *
   * @param name      The name
   * @param timestamp Time when the player had this name in milliseconds
   * @param action    Do what you want to do with the uuid her
   */
  public static void getUUIDAt(
      final String name, final long timestamp,
      final Consumer<UUID> action) {
    pool.execute(() -> action.accept(getUUIDAt(name, timestamp)));
  }

  /**
   * Fetches the uuid synchronously for a specified name and time
   *
   * @param name      The name
   * @param timestamp Time when the player had this name in milliseconds
   * @see UUIDFetcher#FEBRUARY_2015
   */
  public static UUID getUUIDAt(String name, final long timestamp) {
    name = name.toLowerCase();
    try {
      final HttpURLConnection connection = (HttpURLConnection) new URL(
          String.format(UUID_URL, name, timestamp / 1000)).openConnection();
      connection.setReadTimeout(5000);
      final UUIDFetcher data = gson.fromJson(
          new BufferedReader(
              new InputStreamReader(connection.getInputStream())),
          UUIDFetcher.class);

      return data.id;
    } catch (final Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  /**
   * Fetches the name asynchronously and passes it to the consumer
   *
   * @param uuid   The uuid
   * @param action Do what you want to do with the name her
   */
  public static void getName(final UUID uuid, final Consumer<String> action) {
    pool.execute(() -> action.accept(getName(uuid)));
  }

  /**
   * Fetches the name synchronously and returns it
   *
   * @param uuid The uuid
   * @return The name
   */
  public static String getName(final UUID uuid) {
    try {
      final HttpURLConnection connection = (HttpURLConnection) new URL(
          String.format(NAME_URL, UUIDTypeAdapter.fromUUID(uuid)))
          .openConnection();
      connection.setReadTimeout(5000);
      final UUIDFetcher[] nameHistory = gson.fromJson(
          new BufferedReader(
              new InputStreamReader(connection.getInputStream())),
          UUIDFetcher[].class);
      final UUIDFetcher currentNameData = nameHistory[nameHistory.length - 1];
      return currentNameData.name;
    } catch (final Throwable throwable) {
      System.err.println("Exception while getting name for: " + uuid);
      throwable.printStackTrace();
    }
    return null;
  }
}