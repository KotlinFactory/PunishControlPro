package org.mineacademy.punishcontrol.proxy.impl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Cancellable;
import org.mineacademy.bfo.Common;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.providers.PunishProvider;
import org.mineacademy.punishcontrol.core.punish.Punish;
import org.mineacademy.punishcontrol.core.settings.Localization;
import org.mineacademy.punishcontrol.core.settings.Settings;
import org.mineacademy.punishcontrol.proxy.events.PunishCreateEvent;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProxyPunishProvider implements PunishProvider {

  public static ProxyPunishProvider newInstance() {
    return new ProxyPunishProvider();
  }

  @Override
  public void broadCastPunishMessage(
      final @NonNull Punish punish, final boolean silent, final boolean superSilent) {

      // No one will be notified
      if (superSilent) {
        return;
      }

      if (silent) {

        if (!Settings.Notifications.SilentPunish.ENABLED) {
          return;
        }

        for (final ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {

          // The player {} has been banned by
          if (!player.hasPermission(Settings.Notifications.SilentPunish.PERMISSION)) {
            continue;
          }

          Common
              .tell(player, Localization.Punish.PUNISH_BROADCAST_MESSAGE.replace(
                  Common.chatLine(),
                  Providers.playerProvider().findNameUnsafe(punish.target()),
                  punish.punishType().localized(),
                  punish.reason(),
                  punish.ip().orElse("unknown")
              ).replacedMessage());

          // Sending message
        }

        return;
      }

      if (!Settings.Notifications.Punish.ENABLED) {
        return;
      }

      for (final ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {

        if (!player.hasPermission(Settings.Notifications.Punish.PERMISSION)) {
          continue;
        }

        Common
            .tell(player, Localization.Punish.PUNISH_BROADCAST_MESSAGE.replace(
                Common.chatLine(),
                Providers.playerProvider().findNameUnsafe(punish.target()),
                punish.punishType().localized(),
                punish.reason(),
                punish.ip().orElse("unknown")
            ).replacedMessage());
      }
  }

  @SneakyThrows
  @Override
  public boolean handlePunishEvent(final @NonNull Punish punish) {
    final Cancellable event =
        ProxyServer.getInstance()
            .getPluginManager()
            .callEvent(PunishCreateEvent.newInstance(punish));
    return event.isCancelled();
  }
}
