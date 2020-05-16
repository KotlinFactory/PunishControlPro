package org.mineacademy.punishcontrol.spigot.impl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.punishcontrol.core.notification.Notification;
import org.mineacademy.punishcontrol.core.notification.Notifications;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.providers.PunishProvider;
import org.mineacademy.punishcontrol.core.punish.Punish;
import org.mineacademy.punishcontrol.core.punish.Punishes;
import org.mineacademy.punishcontrol.core.settings.Localization;
import org.mineacademy.punishcontrol.core.settings.Settings;
import org.mineacademy.punishcontrol.core.settings.Settings.Punish.Warn;
import org.mineacademy.punishcontrol.spigot.Players;
import org.mineacademy.punishcontrol.spigot.events.AsyncPunishCreateEvent;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SpigotPunishProvider implements PunishProvider {

  public static SpigotPunishProvider newInstance() {
    return new SpigotPunishProvider();
  }

  @Override
  public void broadCastPunishMessage(
      @NonNull final Punish punish,
      final boolean silent,
      final boolean superSilent) {

    final val targetName = Providers.playerProvider().findName(punish.target())
        .orElse("");
    final val creatorName =
        Providers.playerProvider().findName(punish.creator()).orElse("");

    switch (punish.punishType()) {
      case BAN:
        Notifications.register(
            Notification
                .of("&6Banned " + targetName)
                .text(
                    "",
                    "&3" + creatorName + " banned " + targetName
                )
                .itemType(CompMaterial.ACACIA_DOOR)
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
                .itemType(CompMaterial.PAPER)
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
                .itemType(CompMaterial.YELLOW_DYE)
        );
        break;
    }

    Common.runLater(() -> {
      if (punish.punishType().shouldKick()) {
        Players.find(punish.target()).ifPresent((player -> player.kickPlayer(
            Punishes.formOnPunishMessage(punish))));
      }

      if (punish.punishType().shouldWarn()) {

        Players.find(punish.target()).ifPresent((player -> {
          Providers.playerProvider().sendIfOnline(
              player.getUniqueId(),
              Warn.messageType,
              Punishes.formPunishedMessage(punish).split("\n"));
        }));
      }
    });

    // No one will be notified
    if (superSilent) {
      return;
    }

    if (silent) {

      if (!Settings.Notifications.SilentPunish.ENABLED) {
        return;
      }

      for (final Player player : Bukkit.getOnlinePlayers()) {

        // The player {} has been banned by
        if (!player
            .hasPermission(Settings.Notifications.SilentPunish.PERMISSION)) {
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

    for (final Player player : Bukkit.getOnlinePlayers()) {

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

  @Override
  public boolean handlePunishEvent(final @NonNull Punish punish) {
    return Common.callEvent(AsyncPunishCreateEvent.create(punish));
  }
}
