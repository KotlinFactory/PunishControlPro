package org.mineacademy.punishcontrol.spigot.menus.browsers;

import java.util.List;
import java.util.UUID;
import javax.inject.Inject;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.providers.TextureProvider;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;
import org.mineacademy.punishcontrol.spigot.DaggerSpigotComponent;
import org.mineacademy.punishcontrol.spigot.Scheduler;
import org.mineacademy.punishcontrol.spigot.menu.browser.AbstractPlayerBrowser;
import org.mineacademy.punishcontrol.spigot.menus.ChooseActionMenu;
import org.mineacademy.punishcontrol.spigot.menus.MainMenu;
import org.mineacademy.punishcontrol.spigot.util.ItemStacks;

public class PunishedPlayerBrowser extends AbstractPlayerBrowser {

  private final StorageProvider storageProvider;

  public static void showTo(final Player player) {
    Scheduler.runAsync(() -> DaggerSpigotComponent.create().punishedPlayerBrowser()
        .displayTo(player, true));
  }

  @Inject
  public PunishedPlayerBrowser(
      final PlayerProvider playerProvider,
      final TextureProvider textureProvider,
      final StorageProvider storageProvider,
      final MainMenu mainMenu) {
    super(playerProvider, textureProvider, mainMenu,
        storageProvider.listPunishedPlayers());
    this.storageProvider = storageProvider;
  }

  @Override
  protected @Nullable List<String> lore(final UUID target) {
    return ItemStacks.loreForPlayer(target, storageProvider, playerProvider);
  }

  @Override
  public void onClick(final UUID data) {
    ChooseActionMenu.showTo(getViewer(), data);
  }
}
