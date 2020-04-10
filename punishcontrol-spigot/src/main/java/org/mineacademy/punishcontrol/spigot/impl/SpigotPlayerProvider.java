package org.mineacademy.punishcontrol.spigot.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Players;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.providers.AbstractPlayerProvider;
import org.mineacademy.punishcontrol.core.providers.ExceptionHandler;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class SpigotPlayerProvider extends AbstractPlayerProvider {

  public static SpigotPlayerProvider newInstance() {
    return new SpigotPlayerProvider();
  }

  @Override
  public List<UUID> getOfflinePlayers() {
    final List<UUID> result = new ArrayList<>();

    for (final OfflinePlayer player : Bukkit.getOfflinePlayers()) {
      result.add(player.getUniqueId());
    }

    return result;
  }

  @Override
  public List<UUID> getOnlinePlayers() {
    final List<UUID> result = new ArrayList<>();
    for (final Player player : Bukkit.getOnlinePlayers()) {
      result.add(player.getUniqueId());
    }
    return result;
  }

  @Override
  public boolean isOnline(@NonNull final UUID uuid) {
    return Bukkit.getPlayer(uuid) != null;
  }

  @Override
  public boolean hasPermission(final @NonNull UUID uuid,
      final @NonNull String permission) {
    final Player player = Bukkit.getPlayer(uuid);
    if (player == null) {
      return false;
    }

    return player.hasPermission(permission);
  }

  @Override
  public void sendIfOnline(@NonNull final UUID uuid,
      final @NonNull String... messages) {
    final Player player = Bukkit.getPlayer(uuid);

    if (player == null) {
      return;
    }

    Common.tell(player, messages);
  }

  @Override
  public void kickIfOnline(@NonNull final UUID uuid, @NonNull final String... reason) {
    Players.find(uuid).ifPresent((player -> {
      player.kickPlayer(String.join("\n", reason));
    }));
  }

  @Override
  public ExceptionHandler exceptionHandler() {
    return Providers.exceptionHandler();
  }
}
