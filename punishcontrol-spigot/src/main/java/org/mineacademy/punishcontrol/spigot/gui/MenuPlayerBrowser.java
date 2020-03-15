package org.mineacademy.punishcontrol.spigot.gui;

import lombok.NonNull;
import lombok.val;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.MenuPagged;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.provider.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;
import org.mineacademy.punishcontrol.spigot.util.ItemUtils;

import java.util.List;
import java.util.UUID;

public final class MenuPlayerBrowser extends MenuPagged<UUID> {

  // Providers (Can't be injected here:I)

  private static final PlayerProvider PLAYER_PROVIDER = Providers.playerProvider();
  private static final StorageProvider STORAGE_PROVIDER = Providers.storageProvider();

  private MenuPlayerBrowser(final int pageSize, final Menu parent, final Iterable<UUID> pages) {
    super(pageSize, parent, pages);
    setTitle("§3Player-Browser");
  }

  public static void showTo(@NonNull final Player player) {
    showTo(player, true);
  }

  public static void showTo(@NonNull final Player player, final boolean offlinePlayer) {
    // Handling stuff
    of(offlinePlayer).displayTo(player);
  }

  static MenuPagged<UUID> of(final boolean offlinePlayer) {
    // Handling stuff
    if (offlinePlayer) {
      final List<UUID> offlinePlayers = Providers.playerProvider().getOfflinePlayers();
      return new MenuPlayerBrowser(
          getSizeForPlayers(offlinePlayers.size()), MenuMain.create(), offlinePlayers);
    } else {
      final List<UUID> onlinePlayers = Providers.playerProvider().getOnlinePlayers();
      return new MenuPlayerBrowser(
          getSizeForPlayers(onlinePlayers.size()), MenuMain.create(), onlinePlayers);
    }
  }

  protected static int getSizeForPlayers(final int playersOnline) {
    // TODO Rework
    return playersOnline / 9 == 0 ? 9 : (playersOnline / 9) * 9;
  }

  @Override
  protected boolean addPageNumbers() {
    return true;
  }

  @Override
  protected ItemStack convertToItemStack(final UUID uuid) {
    final String name = Providers.playerProvider().getName(uuid);
    final val builder = ItemCreator.of(CompMaterial.PLAYER_HEAD).name("§3" + name).lore("");

    //		if (uuid.equals(getViewer().getUniqueId())) {
    //			System.out.println("glow added");
    //			ItemUtils.addGlow(stack);
    //		}

    final String hash = Providers.textureProvider().getSkinTexture(uuid);
    if (hash != null && !hash.isEmpty()) {
      return ItemUtils.setSkullTexture(builder.build().make(), hash);
    }
    return builder.build().make();
  }

  @Override
  protected void onPageClick(final Player player, final UUID uuid, final ClickType clickType) {
    player.closeInventory();
    MenuPunishmentChooser.showTo(player);
  }

  @Override
  protected String[] getInfo() {
    return new String[0];
  }
}
