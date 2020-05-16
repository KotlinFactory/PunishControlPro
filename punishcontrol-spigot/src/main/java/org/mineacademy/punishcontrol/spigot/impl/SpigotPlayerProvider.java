package org.mineacademy.punishcontrol.spigot.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.debug.Debugger;
import org.mineacademy.fo.remain.Remain;
import org.mineacademy.punishcontrol.core.MessageType;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.providers.AbstractPlayerProvider;
import org.mineacademy.punishcontrol.core.providers.ExceptionHandler;
import org.mineacademy.punishcontrol.spigot.Players;
import org.mineacademy.punishcontrol.spigot.Scheduler;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class SpigotPlayerProvider extends AbstractPlayerProvider {

  public static SpigotPlayerProvider newInstance() {
    return new SpigotPlayerProvider();
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
  public void sendIfOnline(
      @NonNull final UUID uuid,
      @NonNull final MessageType messageType,
      @NonNull final String... messages) {

    final Player player = Players.find(uuid).orElse(null);

    if (player == null) {
      return;
    }

    switch (messageType) {
      case CHAT:
        Common.tell(player, messages);
        return;
      case TITLE:
        final String title = messages.length >= 1 ? messages[0] : "";
        final String subTitle = messages.length >= 2 ? messages[1] : "";
        Debugger.debug("Warn", "Title: '" + title + "'", "SubTitle: '" + subTitle + "'");
        Remain.sendTitle(player, title, subTitle);
        return;
      case ACTION_BAR:
        Remain.sendActionBar(player, String.join(" ", messages));
    }
  }

  @Override
  public void kickIfOnline(@NonNull final UUID uuid, @NonNull final String... reason) {
    //Might be executed async.
    Scheduler.runSync(() -> {
      Players.find(uuid).ifPresent((player -> {
        player.kickPlayer(String.join("\n", reason));
      }));
    });
  }

  @Override
  public ExceptionHandler exceptionHandler() {
    return Providers.exceptionHandler();
  }
}
