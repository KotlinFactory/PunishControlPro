package org.mineacademy.punishcontrol.spigot.menu;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.menu.Menu;

import java.util.UUID;

public abstract class AbstractPlayerBrowserMenu extends AbstractAsyncMenuPaged<UUID> {

  protected AbstractPlayerBrowserMenu(
      @NonNull final Menu parent, @NonNull final boolean offlinePlayers) {
    this(
        parent,
        offlinePlayers
            ? Common.convert(Bukkit.getServer().getOfflinePlayers(), OfflinePlayer::getUniqueId)
            : Common.convert(Bukkit.getServer().getOnlinePlayers(), Player::getUniqueId));
  }

  private AbstractPlayerBrowserMenu(
      @NonNull final Menu parent, @NonNull final Iterable<UUID> players) {
    super(parent, players);
  }
}
