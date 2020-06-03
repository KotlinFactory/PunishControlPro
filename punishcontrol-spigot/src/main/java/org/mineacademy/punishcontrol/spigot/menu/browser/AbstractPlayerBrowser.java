package org.mineacademy.punishcontrol.spigot.menu.browser;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.val;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.punishcontrol.core.fo.constants.FoConstants;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.providers.TextureProvider;
import org.mineacademy.punishcontrol.spigot.util.ItemStacks;

public abstract class AbstractPlayerBrowser extends AbstractSearchableBrowser<UUID> {

  protected final PlayerProvider playerProvider;
  protected final TextureProvider textureProvider;


  public AbstractPlayerBrowser(
      final PlayerProvider playerProvider,
      final TextureProvider textureProvider,
      final Menu mainMenu) {
    this(playerProvider, textureProvider, mainMenu, false);
  }

  public AbstractPlayerBrowser(
      final PlayerProvider playerProvider,
      final TextureProvider textureProvider,
      final Menu mainMenu,
      final boolean onlineOnly) {

    this(
        playerProvider,
        textureProvider,
        mainMenu,
        onlineOnly
            ? playerProvider.getOnlinePlayers()
            : playerProvider.offlinePlayers()
    );
  }

  protected AbstractPlayerBrowser(
      final PlayerProvider playerProvider,
      final TextureProvider textureProvider,
      final Menu mainMenu,
      final Collection<UUID> players
  ) {

    super(mainMenu, players);
    this.playerProvider = playerProvider;
    this.textureProvider = textureProvider;
    setTitle("&8Players");
    players.remove(FoConstants.CONSOLE);
  }

  @Nullable
  protected List<String> lore(final UUID uuid) {
    return null;
  }

  public abstract void onClick(final UUID data);


  @Override
  protected final ItemStack convertToItemStack(final UUID uuid) {
    final String name = playerProvider.findNameUnsafe(uuid);
    final String hash = textureProvider.getSkinTexture(uuid);

    final ItemCreator.ItemCreatorBuilder builder = ItemCreator.ofSkullHash(hash)
        .name("§3" + name);

    if (lore(uuid) == null) {
      builder.lores(Collections.singletonList("&7Choose action"));
    } else {
      builder.lores(lore(uuid));
    }

    builder.glow(true);

    return ItemStacks.glow(builder.build());
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

  public void redisplay(@NonNull Collection<UUID> newContent) {
    async(() -> {
      val menu = new AbstractPlayerBrowser(
          playerProvider,
          textureProvider,
          AbstractPlayerBrowser.this,
          newContent) {

        @Override
        public void onClick(UUID uuid) {
          AbstractPlayerBrowser.this.onClick(uuid);
        }
        @Override
        public Collection<UUID> searchByPartialString(String partial) {
          return AbstractPlayerBrowser.this.searchByPartialString(partial);
        }
      };

      menu.displayTo(AbstractPlayerBrowser.this.getViewer());

      if (newContent.size() == 1) {
        menu.animateTitle("&8Found " + newContent.size() + " result");
      } else {
        menu.animateTitle("&8Found " + newContent.size() + " results");
      }
    });
  }

  @Override
  public Collection<UUID> searchByPartialString(String partial) {
    return playerProvider
        .search(partial)
        .stream()
        .map((uuid -> playerProvider.findUUID(uuid.result()).orElse(UUID.randomUUID())))
        .collect(Collectors.toList());
  }

  @Override
  protected String[] compassLore() {
    return new String[]{"&7Search for a player"};
  }
}
