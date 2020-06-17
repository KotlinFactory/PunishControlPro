package org.mineacademy.punishcontrol.spigot.menus.browsers;

import javax.inject.Inject;
import lombok.NonNull;
import lombok.val;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NonNls;
import org.mineacademy.fo.debug.LagCatcher;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;
import org.mineacademy.punishcontrol.spigot.DaggerSpigotComponent;
import org.mineacademy.punishcontrol.spigot.Scheduler;
import org.mineacademy.punishcontrol.spigot.menu.browser.AbstractPunishBrowser;
import org.mineacademy.punishcontrol.spigot.menus.MainMenu;

public final class AllPunishesBrowser extends AbstractPunishBrowser {

  @NonNls
  private static final String BROWSE_PUNISHES = "Browse Punishes";

  //Showing up while saving performance:)
  public static void showTo(@NonNull final Player player) {
    Scheduler.runAsync(() -> {
      LagCatcher.start("async-show-up-punish-browser");
      final val browser = DaggerSpigotComponent.create().punishBrowserMenu();
      browser.displayTo(player, true);
      LagCatcher.end("async-show-up-punish-browser");
    });
  }

  @Inject
  public AllPunishesBrowser(
      final MainMenu parent,
      final PlayerProvider playerProvider,
      final StorageProvider storageProvider) {
    super(parent, playerProvider, storageProvider.listPunishes());
    setTitle("&7" + BROWSE_PUNISHES);
  }

  @Override
  protected void redrawForPlayer(final Player player) {
    showTo(player);
  }
}
