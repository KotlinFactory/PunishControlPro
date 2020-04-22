package org.mineacademy.punishcontrol.proxy.menus.browsers;

import javax.inject.Inject;
import lombok.NonNull;
import lombok.val;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.mineacademy.bfo.debug.LagCatcher;
import org.mineacademy.burst.util.Scheduler;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;
import org.mineacademy.punishcontrol.proxy.DaggerProxyComponent;
import org.mineacademy.punishcontrol.proxy.menu.browser.AbstractPunishBrowser;
import org.mineacademy.punishcontrol.proxy.menus.MainMenu;

public final class AllPunishesBrowser extends AbstractPunishBrowser {

  //Showing up while saving performance:)
  public static void showTo(@NonNull final ProxiedPlayer player) {
    Scheduler.runAsync(() -> {
      LagCatcher.start("async-show-up-punish-browser");
      final val browser = DaggerProxyComponent.create().punishBrowserMenu();
      browser.displayTo(player);
      LagCatcher.end("async-show-up-punish-browser");
    });
  }

  @Inject
  public AllPunishesBrowser(
      final MainMenu parent,
      final PlayerProvider playerProvider,
      final StorageProvider storageProvider) {
    super("AllPunishesBrowser", parent, playerProvider, storageProvider.listPunishes());
    setTitle("&7Browse Punishes");
  }
}
