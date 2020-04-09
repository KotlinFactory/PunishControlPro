package org.mineacademy.punishcontrol.spigot.menu;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.val;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.providers.TextureProvider;
import org.mineacademy.punishcontrol.spigot.menus.MainMenu;

public abstract class AbstractPlayerBrowser extends AbstractBrowser<UUID> {

  protected final PlayerProvider playerProvider;
  protected final TextureProvider textureProvider;


  public AbstractPlayerBrowser(
      final PlayerProvider playerProvider,
      final TextureProvider textureProvider,
      final MainMenu mainMenu) {
    this(playerProvider, textureProvider, mainMenu, false);
  }

  public AbstractPlayerBrowser(
      final PlayerProvider playerProvider,
      final TextureProvider textureProvider,
      final MainMenu mainMenu,
      final boolean onlineOnly) {

    this(
        playerProvider, textureProvider, mainMenu,
        onlineOnly
            ? playerProvider.getOnlinePlayers()
            : playerProvider.getOfflinePlayers()
    );
  }

  private AbstractPlayerBrowser(
      final PlayerProvider playerProvider,
      final TextureProvider textureProvider,
      final MainMenu mainMenu,
      final List<UUID> players
  ) {

    super(mainMenu, players);
    this.playerProvider = playerProvider;
    this.textureProvider = textureProvider;
  }


  @Nullable
  protected List<String> lore(final UUID uuid) {
    return null;
  }

  public abstract void onClick(final UUID data);


  @Override
  protected final boolean addPageNumbers() {
    return true;
  }

  @Override
  protected final ItemStack convertToItemStack(final UUID uuid) {
    final String name = playerProvider.getName(uuid);
    final String hash = textureProvider.getSkinTexture(uuid);

    final val builder = ItemCreator.fromCustomHash(hash)
        .name("§3" + name);

    if (lore(uuid) == null) {
      builder.lores(Collections.singletonList("&7Choose action"));
    } else {
      builder.lores(lore(uuid));
    }

    builder.glow(true);

    return builder.build().make();
  }

  @Override
  protected final void onPageClick(
      final Player player,
      final UUID uuid,
      final ClickType clickType) {
    onClick(uuid);
  }

  @Override
  protected final String[] getInfo() {
    return new String[]{
        "&7Menu to select",
        "&7an player"
    };
  }
}
