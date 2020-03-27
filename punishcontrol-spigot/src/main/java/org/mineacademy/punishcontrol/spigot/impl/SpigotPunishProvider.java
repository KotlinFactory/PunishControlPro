package org.mineacademy.punishcontrol.spigot.impl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.punishcontrol.core.punish.Punish;
import org.mineacademy.punishcontrol.core.punish.PunishProvider;
import org.mineacademy.punishcontrol.spigot.events.AsyncPunishCreateEvent;
import org.mineacademy.punishcontrol.spigot.settings.Settings;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SpigotPunishProvider implements PunishProvider {

  public static SpigotPunishProvider newInstance() {
    return new SpigotPunishProvider();
  }

  @Override
  public void broadCastPunishMessage(
      @NonNull final Punish punish, final boolean silent, final boolean superSilent) {

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
        if (!player.hasPermission(Settings.Notifications.SilentPunish.PERMISSION)) {
          continue;
        }

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

      // Sending message
    }
  }

  @Override
  public boolean handlePunishEvent(final @NonNull Punish punish) {
    return Common.callEvent(AsyncPunishCreateEvent.newInstance(punish));
  }
}
