package org.mineacademy.punishcontrol.proxy.menu.browser;

import de.exceptionflug.mccommons.inventories.api.CallResult;
import de.exceptionflug.mccommons.inventories.proxy.utils.ItemUtils;
import de.exceptionflug.protocolize.inventory.InventoryModule;
import de.exceptionflug.protocolize.items.ItemStack;
import de.exceptionflug.protocolize.items.ItemType;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.burst.item.Item;
import org.mineacademy.burst.menu.AbstractBrowser;
import org.mineacademy.burst.menu.BurstMenu;
import org.mineacademy.punishcontrol.core.fo.constants.FoConstants;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.providers.TextureProvider;
import org.mineacademy.punishcontrol.proxy.conversations.SearchPlayerConversation;

public abstract class AbstractPlayerBrowser extends AbstractBrowser<UUID> {

  protected final PlayerProvider playerProvider;
  protected final TextureProvider textureProvider;

  public AbstractPlayerBrowser(
      final PlayerProvider playerProvider,
      final TextureProvider textureProvider,
      final BurstMenu mainMenu) {
    this(playerProvider, textureProvider, mainMenu, false);
  }

  public AbstractPlayerBrowser(
      final PlayerProvider playerProvider,
      final TextureProvider textureProvider,
      final BurstMenu mainMenu,
      final boolean onlineOnly) {

    this(
        playerProvider, textureProvider, mainMenu,
        onlineOnly
            ? playerProvider.getOnlinePlayers()
            : playerProvider.offlinePlayers()
    );
  }

  protected AbstractPlayerBrowser(
      final PlayerProvider playerProvider,
      final TextureProvider textureProvider,
      final BurstMenu mainMenu,
      final Collection<UUID> players
  ) {
    super("Players", mainMenu, players);
    this.playerProvider = playerProvider;
    this.textureProvider = textureProvider;
    setTitle("&8Players");

    registerActionHandler("apply", (apply -> {
      InventoryModule.closeAllInventories(getPlayer());
      SearchPlayerConversation.create(getPlayer(), this).start();
      return CallResult.DENY_GRABBING;
    }));
    players.remove(FoConstants.CONSOLE);
  }

  // ----------------------------------------------------------------------------------------------------
  // Overridden methods
  // ----------------------------------------------------------------------------------------------------


  @Override
  public void updateInventory() {
    super.updateInventory();

    if (!isSearchable()) {
      return;
    }

    set(
        Item
            .of(ItemType.COMPASS)
            .name("&6Search")
            .lore(
                "&7Search for player by its",
                "67partial name"
            )
            .actionHandler("Search")
            .slot(getParentItemSlot() - 5)

    );
  }

  @Nullable
  protected List<String> lore(final UUID uuid) {
    return null;
  }

  @Override
  protected final ItemStack convertToItemStack(final UUID uuid) {
    final String name = playerProvider.findName(uuid).orElse("unknown");
    final String hash = textureProvider.getSkinTexture(uuid);

    final Item item = Item.of(hash).name("§3" + name);

    if (lore(uuid) == null) {
      item.lore("&7Choose action");
    } else {
      item.lore(lore(uuid));
    }

    return ItemUtils.addGlow(item.stack());
  }

  @Override
  protected final String[] getInfo() {
    return new String[]{
        "&7Menu to select",
        "&7an player"
    };
  }

  // ----------------------------------------------------------------------------------------------------
  // Methods that might be overridden
  // ----------------------------------------------------------------------------------------------------

  @Override
  public void reDisplay() {
    throw new AbstractMethodError("Not implemented");
  }

  public void redisplay(@NonNull List<UUID> newContent) {
    throw new AbstractMethodError("Not implemented");
  }

  protected boolean isSearchable() {
    return false;
  }
}
