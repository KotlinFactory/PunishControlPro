package org.mineacademy.punishcontrol.proxy.impl;

import lombok.*;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.mineacademy.bfo.Common;
import org.mineacademy.bfo.debug.Debugger;
import org.mineacademy.punishcontrol.core.notification.Notification;
import org.mineacademy.punishcontrol.core.notification.Notifications;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.providers.PunishProvider;
import org.mineacademy.punishcontrol.core.punish.Punish;
import org.mineacademy.punishcontrol.core.punish.Punishes;
import org.mineacademy.punishcontrol.core.settings.Localization;
import org.mineacademy.punishcontrol.core.settings.Settings;
import org.mineacademy.punishcontrol.core.settings.Settings.Punish.Warn;
import org.mineacademy.punishcontrol.proxy.ItemUtil;
import org.mineacademy.punishcontrol.proxy.Players;
import org.mineacademy.punishcontrol.proxy.events.PunishCreateEvent;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProxyPunishProvider implements PunishProvider {

  private static PlayerProvider playerProvider;

  public static ProxyPunishProvider create() {
    return new ProxyPunishProvider();
  }

  @Override
  public void broadCastPunishMessage(
      @NonNull final Punish punish,
      final boolean silent,
      final boolean superSilent) {

    if (punish.punishType().shouldKick())
      Players.find(punish.target()).ifPresent((
          player -> player.disconnect(
              Punishes.formOnPunishMessage(punish))));

    if (punish.punishType().shouldWarn())
      Players.find(punish.target())
          .ifPresent((
              player -> Providers.playerProvider().sendIfOnline(
                  player.getUniqueId(),
                  Warn.messageType,
                  Punishes.formPunishedMessage(punish).split("\n"))));

    if (punish.punishType().shouldMessage())
      Players.find(punish.target())
          .ifPresent((
              player -> Providers.playerProvider().sendIfOnline(
                  player.getUniqueId(),
                  Punishes.formPunishedMessage(punish).split("\n"))));

    final val targetName = Providers.playerProvider().findName(punish.target())
        .orElse("");
    final val creatorName = Providers.playerProvider().findName(punish.creator())
        .orElse("");

    switch (punish.punishType()) {
      case BAN:
        Notifications.register(
            Notification
                .of("&6Banned " + targetName)
                .text(
                    "",
                    "&3" + creatorName + " banned " + targetName
                )
                .itemType(ItemUtil.forPunishType(punish.punishType()))
        );
        break;
      case MUTE:
        Notifications.register(
            Notification
                .of("&6Muted " + targetName)
                .text(
                    "",
                    "&3" + creatorName + " muted " + targetName
                )
                .itemType(ItemUtil.forPunishType(punish.punishType()))
        );
        break;
      case WARN:
        Notifications.register(
            Notification
                .of("&6Warned " + targetName)
                .text(
                    "",
                    "&3" + creatorName + " warned " + targetName
                )
                .itemType(ItemUtil.forPunishType(punish.punishType()))
        );
        break;
    }

    // No one will be notified
    if (superSilent)
      return;

    if (silent) {

      if (!Settings.Notifications.SilentPunish.ENABLED)
        return;

      for (final ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {

        // The player {} has been banned by
        if (!player.hasPermission(Settings.Notifications.SilentPunish.PERMISSION))
          continue;

        org.mineacademy.bfo.Common
            .tell(player, Localization.Punish.PUNISH_BROADCAST_MESSAGE.replace(
                org.mineacademy.bfo.Common.chatLineSmooth(),
                Providers.playerProvider().findName(punish.target()).orElse("unknown"),
                punish.punishType().localized(),
                punish.reason(),
                punish.ip().orElse("unknown")
            ).replacedMessage());

        // Sending message
      }

      return;
    }

    if (!Settings.Notifications.Punish.ENABLED) {
      Debugger.debug("Punish", "Punish notification disabled - skipping it");
      return;
    }

    for (final ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {

      if (!player.hasPermission(Settings.Notifications.Punish.PERMISSION))
        continue;

      Common
          .tell(player, Localization.Punish.PUNISH_BROADCAST_MESSAGE.replace(
              Common.chatLineSmooth(),
              Providers.playerProvider().findName(punish.target()).orElse("unknown"),
              punish.punishType().localized(),
              punish.reason(),
              punish.ip().orElse("unknown")
          ).replacedMessage());
    }
  }

  @SneakyThrows
  @Override
  public boolean handlePunishEvent(final @NonNull Punish punish) {
    final PunishCreateEvent event =
        ProxyServer.getInstance()
            .getPluginManager()
            .callEvent(PunishCreateEvent.create(punish));
    return !event.isCancelled();
  }
}
