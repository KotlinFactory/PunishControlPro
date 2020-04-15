package org.mineacademy.punishcontrol.spigot.menus.browsers;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import lombok.val;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.debug.LagCatcher;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.punish.Punish;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;
import org.mineacademy.punishcontrol.spigot.Scheduler;
import org.mineacademy.punishcontrol.spigot.menu.AbstractPunishBrowser;
import org.mineacademy.punishcontrol.spigot.menus.ChooseActionMenu;

/**
 * Same as {@link AllPunishesBrowser} but only shows the punishes for one
 * player.
 */
public final class PlayerPunishBrowser extends AbstractPunishBrowser {

  private final StorageProvider storageProvider;
  private final UUID target;

  //Showing up while saving performance:)
  public static void showTo(
      @NonNull final Player player,
      @NonNull final UUID target) {
    Scheduler.runAsync(() -> {
      LagCatcher.start("async-show-up-punish-browser");
      final val browser = new PlayerPunishBrowser(
          ChooseActionMenu.create(target),
          Providers.playerProvider(),
          Providers.storageProvider(),
          target);
      browser.displayTo(player);
      LagCatcher.end("async-show-up-punish-browser");
    });
  }


  private PlayerPunishBrowser(
      final Menu parent,
      final PlayerProvider playerProvider,
      final StorageProvider storageProvider,
      final UUID target) {
    super(parent, playerProvider, storageProvider.listPunishes(target));
    this.target = target;
    this.storageProvider = storageProvider;
  }

  @Override
  protected @Nullable List<String> loresToAdd(final Punish punish) {
    return Collections.singletonList(
        "&6Right click: Remove"
    );
  }
}
