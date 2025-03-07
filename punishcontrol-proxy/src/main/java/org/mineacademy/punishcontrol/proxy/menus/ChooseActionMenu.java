package org.mineacademy.punishcontrol.proxy.menus;

import de.exceptionflug.mccommons.inventories.api.CallResult;
import de.exceptionflug.protocolize.inventory.InventoryModule;
import de.exceptionflug.protocolize.items.ItemType;
import java.util.UUID;
import lombok.NonNull;
import lombok.val;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import org.jetbrains.annotations.NonNls;
import org.mineacademy.bfo.Players;
import org.mineacademy.bfo.plugin.SimplePlugin;
import org.mineacademy.burst.item.Item;
import org.mineacademy.burst.menu.AbstractMenu;
import org.mineacademy.burst.util.Scheduler;
import org.mineacademy.punishcontrol.core.injector.annotations.Localizable;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.providers.TextureProvider;
import org.mineacademy.punishcontrol.core.punish.PunishBuilder;
import org.mineacademy.punishcontrol.core.punish.PunishType;
import org.mineacademy.punishcontrol.core.settings.Localization;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;
import org.mineacademy.punishcontrol.proxy.DaggerProxyComponent;
import org.mineacademy.punishcontrol.proxy.ItemUtil;
import org.mineacademy.punishcontrol.proxy.conversations.KickConversation;
import org.mineacademy.punishcontrol.proxy.menus.browsers.PlayerPunishBrowser;
import org.mineacademy.punishcontrol.proxy.menus.punish.PunishCreatorMenu;
import org.mineacademy.punishcontrol.proxy.menus.settings.PlayerSettingsMenu;

@Localizable
public final class ChooseActionMenu extends AbstractMenu implements Listener {

  // ----------------------------------------------------------------------------------------------------
  // Localization
  // ----------------------------------------------------------------------------------------------------

  @Localizable("Menu.Proxy.ChooseAction.Lore")
  private static String[] MENU_INFORMATION = {
      "&7Menu to select ",
      "&7Action for players"};

  @NonNls
  @Localizable("Parts.Action_For")
  private static String ACTION_FOR = "Action for";

  @NonNls @Localizable("Parts.Punishments")
  private static String PUNISHMENTS = "Punishments";
  @NonNls @Localizable("Menu.Proxy.ChooseAction.Player_Offline_Lore")
  private static String[] PLAYER_IS_OFFLINE_LORE = {
      " ",
      "&cDisabled:",
      "&7Player is offline"};
  @NonNls @Localizable("Menu.Proxy.ChooseAction.View_Punishments")
  private static String[] VIEW_PUNISHMENT_LORE = {
      "",
      "&7View punishments"};
  @NonNls @Localizable("Parts.Settings")
  private static String SETTINGS = "Settings";
  @NonNls @Localizable("Parts.Kick")
  private static String KICK = "Kick";
  @NonNls @Localizable("Parts.Data_For")
  private static String DATA_FOR = "Data for";
  @NonNls @Localizable("Parts.Punish")
  private static String PUNISH = "Punish";
  @NonNls @Localizable("Menu.Proxy.ChooseAction.Settings_Lore")
  private static String[] SETTINGS_LORE = {
      " ",
      "&7Settings for player"};

  // ----------------------------------------------------------------------------------------------------
  // Button positions
  // ---------------------------------------------------------------------------------------------------
  private static final int LIST_PUNISHES_SLOT = 11;
  private static final int PLAYER_HEAD_SLOT = 13;
  private static final int SETTINGS_SLOT = 15;
  private static final int KICK_SLOT = 7;
  private static final int PUNISH_SLOT = 1;

  // ----------------------------------------------------------------------------------------------------
  // Constructors & Fields
  // ----------------------------------------------------------------------------------------------------

  private final PlayerProvider playerProvider;
  private final TextureProvider textureProvider;
  private final StorageProvider storageProvider;
  private final UUID target;
  private final String targetName;

  public ChooseActionMenu(
      final MainMenu mainMenu,
      final PlayerProvider playerProvider,
      final TextureProvider textureProvider,
      final StorageProvider storageProvider,
      final UUID target) {
    super("ChooseAction", mainMenu, 9 * 3);
    this.playerProvider = playerProvider;
    this.textureProvider = textureProvider;
    this.storageProvider = storageProvider;
    this.target = target;
    targetName = playerProvider.findName(target).orElse("unknown");
    ProxyServer.getInstance().getPluginManager()
        .registerListener(
            SimplePlugin.getInstance(),
            this);

    setTitle("§8" + ACTION_FOR + " " + targetName);
  }

  public static void showTo(
      @NonNull final ProxiedPlayer player,
      @NonNull final UUID target) {
    Scheduler.runAsync(() -> {
      final val menu = create(target);
      menu.displayTo(player);
    });
  }

  public static ChooseActionMenu create(@NonNull final UUID target) {
    return new ChooseActionMenu(
        DaggerProxyComponent.create().menuMain(),
        Providers.playerProvider(),
        Providers.textureProvider(),
        Providers.storageProvider(),
        target);
  }

  // ----------------------------------------------------------------------------------------------------
  // Overridden methods from menu
  // ----------------------------------------------------------------------------------------------------

  @Override
  public void updateInventory() {
    super.updateInventory();

    setTitle("§8" + ACTION_FOR + " " + targetName);

    set(
        Item
            .of(textureProvider.getSkinTexture(target))
            .name("&7" + DATA_FOR + ": &6" + (targetOnline() ? "&a" : "&7") + targetName)
            .lore(ItemUtil.loreForPlayer(target, storageProvider, playerProvider))
            .slot(PLAYER_HEAD_SLOT)
            .actionHandler("NoAction")
       );

    set(
        Item
            .of(
                ItemType.ANVIL,
                "&6" + PUNISH,
                " ",
                "&7" + PUNISH + ": " + playerProvider.findName(target).orElse("unknown"))
            .slot(PUNISH_SLOT)
            .actionHandler("Punish")
       );

    set(
        Item
            .of(
                ItemType.CHEST,
                "&6" + PUNISHMENTS,
                VIEW_PUNISHMENT_LORE)
            .slot(LIST_PUNISHES_SLOT)
            .actionHandler("ListPunishes")
       );

    if (!targetOnline())
      set(Item
          .of(
              ItemType.COMPARATOR,
              "&6" + SETTINGS,
              PLAYER_IS_OFFLINE_LORE)
          .slot(SETTINGS_SLOT)
          .actionHandler("Settings")
         );
    else
      set(
          Item
              .of(
                  ItemType.COMPARATOR,
                  "&6" + SETTINGS,
                  SETTINGS_LORE)
              .slot(SETTINGS_SLOT)
              .actionHandler("Settings")
         );

    if (!targetOnline())
      set(
          Item
              .of(
                  ItemType.BLAZE_POWDER,
                  "&6" + KICK,
                  PLAYER_IS_OFFLINE_LORE)
              .slot(KICK_SLOT)
              .actionHandler("Kick")
         );
    else
      set(
          Item
              .of(
                  ItemType.BLAZE_POWDER,
                  "&6" + KICK,
                  "",
                  "&7Kick " + targetName)
              .slot(KICK_SLOT)
              .actionHandler("Kick")
         );
  }

  @Override
  public void reDisplay() {
    showTo(getPlayer(), target);
  }

  @Override
  public void registerActionHandlers() {
    registerActionHandler("Punish", (punish) -> {
      PunishCreatorMenu.showTo(
          getPlayer(),
          PunishBuilder
              .of(PunishType.BAN)
              .target(target));
      return CallResult.DENY_GRABBING;
    });

    registerActionHandler("ListPunishes", (
        listPunishes -> {
          PlayerPunishBrowser.showTo(getPlayer(), target);
          return CallResult.DENY_GRABBING;
        }));

    registerActionHandler("Settings", (
        settings -> {
          if (!targetOnline()) {
            animateTitle(Localization.TARGET_IS_OFFLINE);
            return CallResult.DENY_GRABBING;
          }

          PlayerSettingsMenu.showTo(getPlayer(), target);
          return CallResult.DENY_GRABBING;
        }));

    registerActionHandler("Kick", (
        kick -> {
          if (!targetOnline()) {
            animateTitle(Localization.TARGET_IS_OFFLINE);
            return CallResult.DENY_GRABBING;
          }

          InventoryModule.closeAllInventories(getPlayer());
          KickConversation.create(getPlayer(), this, target, targetName).start();
          return CallResult.DENY_GRABBING;
        }));
  }

  private boolean targetOnline() {
    return Players.find(target).isPresent();
  }

  @Override
  protected String[] getInfo() {
    return MENU_INFORMATION;
  }
}
