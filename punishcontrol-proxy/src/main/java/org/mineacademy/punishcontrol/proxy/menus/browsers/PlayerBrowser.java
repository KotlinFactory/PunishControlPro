package org.mineacademy.punishcontrol.proxy.menus.browsers;

import de.exceptionflug.mccommons.inventories.api.ClickType;
import java.util.List;
import java.util.UUID;
import javax.inject.Inject;
import lombok.NonNull;
import lombok.val;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.bfo.debug.LagCatcher;
import org.mineacademy.burst.util.Scheduler;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.providers.TextureProvider;
import org.mineacademy.punishcontrol.proxy.DaggerProxyComponent;
import org.mineacademy.punishcontrol.proxy.menu.browser.AbstractPlayerBrowser;
import org.mineacademy.punishcontrol.proxy.menus.ChooseActionMenu;
import org.mineacademy.punishcontrol.proxy.menus.MainMenu;

public final class PlayerBrowser extends AbstractPlayerBrowser {

  @Inject
  public PlayerBrowser(
      final PlayerProvider playerProvider,
      final TextureProvider textureProvider,
      final MainMenu parent) {
    super(playerProvider, textureProvider, parent);
    setTitle("§3Player-Browser");
  }

  public static void showTo(@NonNull final ProxiedPlayer player) {
    Scheduler.runAsync(() -> {
      LagCatcher.start("async-show-up-player-browser");
      final val browser = DaggerProxyComponent.create().playerBrowserMenu();
      browser.displayTo(player);
      LagCatcher.end("async-show-up-player-browser");
    });
  }

  @Override
  protected @Nullable List<String> lore(final UUID uuid) {
    return super.lore(uuid);
  }

  @Override
  public void onClick(final ClickType clickType, final UUID data) {
    ChooseActionMenu.showTo(getPlayer(), data);
  }
}
