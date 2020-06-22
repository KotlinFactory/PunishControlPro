package org.mineacademy.punishcontrol.core.providers;

import de.leonhard.storage.util.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.NonNull;
import lombok.val;
import org.mineacademy.punishcontrol.core.MessageType;
import org.mineacademy.punishcontrol.core.Searcher.SearchResult;

/**
 * Provide data needed
 */
public interface PlayerProvider {

  void saveData(
      @NonNull UUID uuid, @NonNull String name,
      @NonNull final String ip);

  /**
   * @return Players which are on the server & players joined the server earlier.
   */
  List<UUID> offlinePlayers();

  List<UUID> getOnlinePlayers();

  boolean isOnline(@NonNull UUID uuid);

  boolean hasPermission(
      @NonNull final UUID uuid,
      @NonNull final String permission);

  Optional<String> findName(@NonNull UUID uuid);

  @Deprecated
  default String findNameUnsafe(@NonNull final UUID uuid) {
    final val result = findName(uuid).orElse(null);
    Valid.notNull(
        result,
        "Couldn't find '" + uuid + "' on Mojang side",
        "Maybe we were banned?",
        "Try restarting our server & join again");
    return result;
  }

  Optional<UUID> findUUID(@NonNull String name);

  @Deprecated
  default UUID findUUIDUnsafe(@NonNull final String name) {
    final val result = findUUID(name).orElse(null);
    Valid.notNull(
        result,
        "Couldn't find '" + name + "' on Mojang side",
        "Maybe we were banned?",
        "Try restarting our server & join again");
    return result;
  }

  Optional<String> ip(@NonNull UUID uuid);

  void sendIfOnline(@NonNull UUID uuid, @NonNull String... messages);

  void sendIfOnline(
      @NonNull UUID uuid,
      @NonNull MessageType messageType,
      @NonNull String... messages);

  void kickIfOnline(@NonNull UUID uuid, @NonNull String... reason);

  void punishable(
      @NonNull UUID target,
      @NonNull boolean punishable);

  boolean punishable(@NonNull UUID target);

  List<UUID> searchForUUIDsOfIp(@NonNull String hostAddress);

  /**
   * Search for a player by partial name
   *
   * @param partial
   * @return
   */
  List<SearchResult> search(String partial);
}
