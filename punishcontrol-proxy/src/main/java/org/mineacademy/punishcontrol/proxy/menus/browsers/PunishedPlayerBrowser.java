package org.mineacademy.punishcontrol.proxy.menus.browsers;

import de.exceptionflug.mccommons.inventories.api.ClickType;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.inject.Inject;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.bfo.debug.Debugger;
import org.mineacademy.burst.util.Scheduler;
import org.mineacademy.punishcontrol.core.Searcher;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.providers.TextureProvider;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;
import org.mineacademy.punishcontrol.proxy.DaggerProxyComponent;
import org.mineacademy.punishcontrol.proxy.ItemUtil;
import org.mineacademy.punishcontrol.proxy.menu.browser.AbstractPlayerBrowser;
import org.mineacademy.punishcontrol.proxy.menus.ChooseActionMenu;
import org.mineacademy.punishcontrol.proxy.menus.MainMenu;

public class PunishedPlayerBrowser extends AbstractPlayerBrowser {

  private final StorageProvider storageProvider;

  public static void showTo(final ProxiedPlayer player) {
    Scheduler.runAsync(() -> DaggerProxyComponent
        .create().punishedPlayerBrowser().displayTo(player));
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
    return ItemUtil.loreForPlayer(target, storageProvider);
  }

  @Override
  protected void onClick(final ClickType clickType, final UUID uuid) {
    ChooseActionMenu.showTo(getPlayer(), uuid);
  }

  @Override
  public void reDisplay() {
    try {
      showTo(getPlayer());
    } catch (Throwable throwable) {
      Debugger.saveError(
          throwable,
          "Exception while reDisplaying PunishedPlayerBrowser");
    }
  }

  @Override
  public Collection<UUID> searchByPartialString(String partial) {
    return Searcher
        .search(
            partial,
            getContent()
                .stream()
                .map(uuid -> playerProvider.findName(uuid).orElse("unknown"))
                .collect(Collectors.toList()))
        .stream()
        .map(result -> playerProvider.findUUID(result.result()).orElse(null))
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }
}
