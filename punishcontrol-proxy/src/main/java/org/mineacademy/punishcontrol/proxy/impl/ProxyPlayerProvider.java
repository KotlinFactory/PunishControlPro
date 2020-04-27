package org.mineacademy.punishcontrol.proxy.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.mineacademy.bfo.Common;
import org.mineacademy.bfo.PlayerUtil;
import org.mineacademy.burst.provider.UUIDNameProvider;
import org.mineacademy.punishcontrol.core.MessageType;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.providers.AbstractPlayerProvider;
import org.mineacademy.punishcontrol.core.providers.ExceptionHandler;
import org.mineacademy.punishcontrol.proxy.Players;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProxyPlayerProvider
    extends AbstractPlayerProvider
    implements UUIDNameProvider { // Compatibility

  private static final ProxyServer proxyServer = ProxyServer.getInstance();

  public static ProxyPlayerProvider newInstance() {
    return new ProxyPlayerProvider();
  }

  @Override
  public List<UUID> getOnlinePlayers() {
    final List<UUID> result = new ArrayList<>();

    for (final val player : proxyServer.getPlayers()) {
      result.add(player.getUniqueId());
    }

    return result;
  }

  @Override
  public boolean isOnline(@NonNull final UUID uuid) {
    return ProxyServer.getInstance().getPlayer(uuid) != null;
  }

  @Override
  public boolean hasPermission(
      final @NonNull UUID uuid,
      final @NonNull String permission) {
    final ProxiedPlayer player = proxyServer.getPlayer(uuid);
    if (player == null) {
      return false;
    }

    return player.hasPermission(permission);
  }

  @Override
  public void sendIfOnline(@NonNull final UUID uuid,
      final @NonNull String... messages) {
    final ProxiedPlayer player = proxyServer.getPlayer(uuid);

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
    final ProxiedPlayer player = Players.find(uuid).orElse(null);

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
        PlayerUtil.sendTitle(player, title, subTitle);
        return;
      case ACTION_BAR:
        PlayerUtil.sendActionBar(player, String.join(" ", messages));
    }
  }

  @Override
  public void kickIfOnline(@NonNull final UUID uuid, @NonNull final String... reason) {
    final val player = ProxyServer.getInstance().getPlayer(uuid);
    if (player == null) {
      return;
    }

    player.disconnect(String.join("\n", reason));
  }

  @Override
  public ExceptionHandler exceptionHandler() {
    return Providers.exceptionHandler();
  }
}
