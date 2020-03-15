package org.mineacademy.punishcontrol.core.provider.providers;

import lombok.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Provide Data needed */
public interface PlayerProvider {

  void saveData(@NonNull UUID uuid, @NonNull String name, @NonNull final String ip);

  /** @return Players which are on the server & players joined the server earlier. */
  List<UUID> getOfflinePlayers();

  List<UUID> getOnlinePlayers();

  boolean isOnline(@NonNull UUID uuid);

  boolean hasPermission(@NonNull final UUID uuid, @NonNull final String permission);

  String getName(@NonNull UUID uuid);

  UUID getUUID(@NonNull String name);

  Optional<String> getIp(@NonNull UUID uuid);

  void sendIfOnline(@NonNull UUID uuid, @NonNull String... messages);
}
