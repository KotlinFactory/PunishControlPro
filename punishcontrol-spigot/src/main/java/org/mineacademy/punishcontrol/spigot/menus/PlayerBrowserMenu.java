package org.mineacademy.punishcontrol.spigot.menus;

import java.util.UUID;
import javax.inject.Inject;
import lombok.NonNull;
import lombok.val;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.debug.LagCatcher;
import org.mineacademy.fo.menu.MenuPagged;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.providers.TextureProvider;
import org.mineacademy.punishcontrol.spigot.DaggerSpigotComponent;
import org.mineacademy.punishcontrol.spigot.Scheduler;

public final class PlayerBrowserMenu extends MenuPagged<UUID> {

  private final PlayerProvider playerProvider;
  private final TextureProvider textureProvider;

  @Inject
  public PlayerBrowserMenu(
      final PlayerProvider playerProvider,
      final TextureProvider textureProvider,
      final MainMenu parent) {
    super(9 * 5, parent, playerProvider.getOfflinePlayers());
    this.playerProvider = playerProvider;
    this.textureProvider = textureProvider;
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
  protected boolean addPageNumbers() {
    return true;
  }

  @Override
  protected ItemStack convertToItemStack(final UUID uuid) {
    final String name = playerProvider.getName(uuid);
    final String hash = textureProvider.getSkinTexture(uuid);

    final val builder = ItemCreator.fromCustomHash(hash)
        .name("§3" + name).lore("");

    builder.glow(true);

    return builder.build().make();
  }

  @Override
  protected void onPageClick(final Player player, final UUID uuid,
      final ClickType clickType) {
    player.closeInventory();
    PunishChooserMenu.showTo(player);
  }

  @Override
  protected String[] getInfo() {
    return new String[0];
  }
}
