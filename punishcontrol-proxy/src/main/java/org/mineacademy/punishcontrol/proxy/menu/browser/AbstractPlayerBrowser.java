package org.mineacademy.punishcontrol.proxy.menu.browser;

import de.exceptionflug.mccommons.inventories.api.ClickType;
import de.exceptionflug.mccommons.inventories.proxy.utils.ItemUtils;
import de.exceptionflug.protocolize.items.ItemStack;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.burst.item.Item;
import org.mineacademy.burst.menu.AbstractSearchableBrowser;
import org.mineacademy.burst.menu.BurstMenu;
import org.mineacademy.punishcontrol.core.fo.constants.FoConstants;
import org.mineacademy.punishcontrol.core.injector.annotations.Localizable;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.providers.TextureProvider;
import org.mineacademy.punishcontrol.core.setting.Replacer;

/**
 * Abstract class for our PlayerBrowser Implementations mustn't have any particular
 * constructor-logic or they must override {@link #reDisplay()} and {@link
 * #redisplay(Collection)}
 */
@Localizable
public abstract class AbstractPlayerBrowser extends AbstractSearchableBrowser<UUID> {

  // ----------------------------------------------------------------------------------------------------
  // Localization
  // ----------------------------------------------------------------------------------------------------

  @NonNls @Localizable("Parts.Players")
  private static String PLAYERS = "Players";
  @NonNls @Localizable("Parts.Choose_Action")
  private static String CHOOSE_ACTION = "Choose action";

  @Localizable("Menu.Proxy.PlayerBrowser.Information")
  private static String[] MENU_INFORMATION = {
      "&7Menu to select",
      "&7an player"
  };
  @Localizable("Menu.Proxy.PlayerBrowser.Compass_Lore")
  private static String[] COMPASS_LORE = {"&7Search for a player"};

  @Localizable("Menu.Proxy.Results_Found")
  private static Replacer results = Replacer
      .of("Found {results} result(s)");

  // ----------------------------------------------------------------------------------------------------
  // Fields & Constructor's
  // ----------------------------------------------------------------------------------------------------

  protected final PlayerProvider playerProvider;
  protected final TextureProvider textureProvider;

  public AbstractPlayerBrowser(
      final PlayerProvider playerProvider,
      final TextureProvider textureProvider) {
    this(playerProvider, textureProvider, null, false);
  }

  public AbstractPlayerBrowser(
      final PlayerProvider playerProvider,
      final TextureProvider textureProvider,
      final BurstMenu parent) {
    this(playerProvider, textureProvider, parent, false);
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
    super(
        "Players",
        mainMenu,
        players);
    this.playerProvider = playerProvider;
    this.textureProvider = textureProvider;
    setTitle("&8" + PLAYERS);

    players.remove(FoConstants.CONSOLE);
  }

  // ----------------------------------------------------------------------------------------------------
  // Methods to override
  // ----------------------------------------------------------------------------------------------------

  @Nullable
  protected List<String> lore(final UUID uuid) {
    return null;
  }

  @Override
  public void reDisplay() {
  }

  // ----------------------------------------------------------------------------------------------------
  // Overridden methods
  // ----------------------------------------------------------------------------------------------------

  @Override
  protected final ItemStack convertToItemStack(final UUID uuid) {
    final String name = playerProvider.findName(uuid).orElse("unknown");
    final String hash = textureProvider.getSkinTexture(uuid);

    final Item item = Item.of(hash).name("§3" + name);

    if (lore(uuid) == null)
      item.lore("&7" + CHOOSE_ACTION);
    else
      item.lore(lore(uuid));

    return ItemUtils.addGlow(item.stack());
  }

  @Override
  protected final String[] getInfo() {
    return MENU_INFORMATION;
  }

  @Override
  public void redisplay(@NonNull final Collection<UUID> newContent) {
    async(() -> {
      val menu = new AbstractPlayerBrowser(
          playerProvider,
          textureProvider,
          AbstractPlayerBrowser.this,
          newContent) {

        @Override
        protected void onClick(final ClickType clickType, final UUID uuid) {
          AbstractPlayerBrowser.this.onClick(clickType, uuid);
        }

        @Override
        public Collection<UUID> searchByPartialString(final String partial) {
          return AbstractPlayerBrowser.this.searchByPartialString(partial);
        }
      };

      menu.displayTo(AbstractPlayerBrowser.this.getPlayer());

      results.replaceAll("results", newContent.size());

      menu.animateTitle("&8" + results.replacedMessageJoined());
    });
  }

  @Override
  public Collection<UUID> searchByPartialString(final String partial) {
    return playerProvider
        .search(partial)
        .stream()
        .map((uuid -> playerProvider.findUUID(uuid.result()).orElse(UUID.randomUUID())))
        .collect(Collectors.toList());
  }

  @Override
  protected String[] compassLore() {
    return COMPASS_LORE;
  }
}
