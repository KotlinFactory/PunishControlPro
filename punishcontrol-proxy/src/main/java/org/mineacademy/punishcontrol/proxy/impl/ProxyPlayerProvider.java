package org.mineacademy.punishcontrol.proxy.impl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.val;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.mineacademy.bfo.Common;
import org.mineacademy.bfo.debug.Debugger;
import org.mineacademy.burst.provider.UUIDNameProvider;
import org.mineacademy.punishcontrol.core.provider.providers.AbstractPlayerProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProxyPlayerProvider extends AbstractPlayerProvider
    implements UUIDNameProvider { // Compatibility
  private static final ProxyServer proxyServer = ProxyServer.getInstance();

  public static ProxyPlayerProvider newInstance() {
    return new ProxyPlayerProvider();
  }

  @Override
  public List<UUID> getOfflinePlayers() {
    final List<UUID> result = new ArrayList<>();
    for (final String str : singleLayerKeySet()) {
      try {
        result.add(UUID.fromString(str));
      } catch (final Throwable throwable) {
        Debugger.saveError(throwable);
      }
    }

    return result;
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
  public boolean hasPermission(final @NonNull UUID uuid, final @NonNull String permission) {
    final ProxiedPlayer player = proxyServer.getPlayer(uuid);
    if (player == null) {
      return false;
    }

    return player.hasPermission(permission);
  }

  @Override
  public void sendIfOnline(@NonNull final UUID uuid, final @NonNull String... messages) {
    final ProxiedPlayer player = proxyServer.getPlayer(uuid);

    if (player == null) {
      return;
    }

    Common.tell(player, messages);
  }
}
