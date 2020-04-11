package org.mineacademy.punishcontrol.spigot.menus.browser;

import java.util.List;
import java.util.UUID;
import javax.inject.Inject;
import lombok.NonNull;
import lombok.val;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.debug.LagCatcher;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.providers.TextureProvider;
import org.mineacademy.punishcontrol.spigot.DaggerSpigotComponent;
import org.mineacademy.punishcontrol.spigot.Scheduler;
import org.mineacademy.punishcontrol.spigot.menu.AbstractPlayerBrowser;
import org.mineacademy.punishcontrol.spigot.menus.ChooseActionMenu;
import org.mineacademy.punishcontrol.spigot.menus.MainMenu;

public final class PlayerBrowser extends AbstractPlayerBrowser {


  @Inject
  public PlayerBrowser(
      final PlayerProvider playerProvider,
      final TextureProvider textureProvider,
      final MainMenu parent) {
    super(playerProvider, textureProvider, parent);
    setTitle("§3Player-Browser");
  }

  public static void showTo(@NonNull final Player player) {
    Scheduler.runAsync(() -> {
      LagCatcher.start("async-show-up-player-browser");
      final val browser = DaggerSpigotComponent.create().playerBrowserMenu();
      Scheduler.runSync(() -> browser.displayTo(player));
      LagCatcher.end("async-show-up-player-browser");
    });
  }

  @Override
  protected @Nullable List<String> lore(final UUID uuid) {
    return super.lore(uuid);
  }

  @Override
  public void onClick(final UUID data) {
    ChooseActionMenu.showTo(getViewer());
  }
}
