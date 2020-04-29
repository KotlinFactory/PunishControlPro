package org.mineacademy.punishcontrol.proxy.menus.browsers;

import java.util.UUID;
import lombok.NonNull;
import lombok.val;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.mineacademy.bfo.debug.LagCatcher;
import org.mineacademy.burst.menu.AbstractMenu;
import org.mineacademy.burst.util.Scheduler;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;
import org.mineacademy.punishcontrol.proxy.menu.browser.AbstractPunishBrowser;
import org.mineacademy.punishcontrol.proxy.menus.ChooseActionMenu;

/**
 * Same as {@link AllPunishesBrowser} but only shows the punishes for one
 * player.
 */
public final class PlayerPunishBrowser extends AbstractPunishBrowser {

  private final StorageProvider storageProvider;
  private final UUID target;

  //Showing up while saving performance:)
  public static void showTo(
      @NonNull final ProxiedPlayer player,
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
      final AbstractMenu parent,
      final PlayerProvider playerProvider,
      final StorageProvider storageProvider,
      final UUID target) {
    super(
        "PlayerPunishBrowser",
        parent,
        playerProvider,
        storageProvider.listPunishes(target));
    this.target = target;
    this.storageProvider = storageProvider;
  }

  @Override
  public void reDisplay() {
    showTo(getPlayer(), target);
  }
}
