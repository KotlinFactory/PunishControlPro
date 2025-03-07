package org.mineacademy.punishcontrol.proxy.menus;

import de.exceptionflug.mccommons.inventories.api.CallResult;
import de.exceptionflug.mccommons.inventories.api.ClickType;
import de.exceptionflug.protocolize.items.ItemType;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import lombok.NonNull;
import lombok.val;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NonNls;
import org.mineacademy.bfo.plugin.SimplePlugin;
import org.mineacademy.burst.item.ChangingButton;
import org.mineacademy.burst.item.Item;
import org.mineacademy.burst.menu.ChangingMenu;
import org.mineacademy.burst.util.Scheduler;
import org.mineacademy.punishcontrol.core.injector.annotations.Localizable;
import org.mineacademy.punishcontrol.core.providers.TextureProvider;
import org.mineacademy.punishcontrol.proxy.DaggerProxyComponent;
import org.mineacademy.punishcontrol.proxy.menus.browsers.AllPunishesBrowser;
import org.mineacademy.punishcontrol.proxy.menus.browsers.PlayerBrowser;
import org.mineacademy.punishcontrol.proxy.menus.browsers.PunishedPlayerBrowser;
import org.mineacademy.punishcontrol.proxy.menus.browsers.SettingsBrowser;
import org.mineacademy.punishcontrol.proxy.menus.punish.PunishCreatorMenu;

@Localizable
public final class MainMenu extends ChangingMenu {

  // ----------------------------------------------------------------------------------------------------
  // Localization
  // ----------------------------------------------------------------------------------------------------

  @NonNls
  @Localizable("Parts.Punishments")
  private static String PUNISHMENTS = "Punishments";
  @Localizable("Parts.CreateNew")
  private static String CREATE_NEW_NAME = "Create New";
  @Localizable("Menu.Proxy.Create_New_Lore")
  private static String[] CREATE_NEW_LORE = {
      " ",
      "&7Create new punishment"};

  @Localizable("Parts.Settings")
  private static String SETTINGS = "Settings";

  @Localizable(value = "Menu.Proxy.Main.View_Settings_Lore")
  private static String VIEW_SETTINGS = "View Settings for";

  @Localizable(value = "Menu.Proxy.Main.Player_Lore")
  private static List<String> PLAYER_LORE = Arrays.asList(
      "&7View players",
      "&7to select",
      "&7one for more",
      "&7actions");

  @Localizable(value = "Parts.Players")
  private static String PLAYERS = "Players";

  @Localizable(value = "Menu.Main.Punish_Lore")
  private static List<String> punishLore = Arrays.asList(
      "Browse created punishments.",
      "Right click to view",
      "punished players");

  // ----------------------------------------------------------------------------------------------------
  // Button positions
  // ----------------------------------------------------------------------------------------------------

  private static final int PLAYER_BROWSER_SLOT = 9 * 2 + 6;
  private static final int PUNISHES_BUTTON_SLOT = 9 * 2 + 2;
  private static final int NEW_PUNISH_BUTTON_SLOT = 9 + 4;
  private static final int SETTINGS_BUTTON_SLOT = 9 * 3 + 4;

  // ----------------------------------------------------------------------------------------------------
  // Displaying
  // ----------------------------------------------------------------------------------------------------

  public static void showTo(@NonNull final ProxiedPlayer player) {
    Scheduler.runAsync(() -> {
      final val menu = DaggerProxyComponent.create().menuMain();
      menu.displayTo(player);
    });
  }

  // ----------------------------------------------------------------------------------------------------
  // Constructor's and field's
  // ----------------------------------------------------------------------------------------------------

  @Inject
  public MainMenu(final TextureProvider textureProvider) {
    super(
        "MainMenu",
        Collections.singletonList(
            ChangingButton
                .fromCustomHashes(
                    textureProvider.listTextures())
                .name("&6" + PLAYERS)
                .slot(24)
                .lore(PLAYER_LORE)),
        9 * 5
    );
    setTitle("§3Punish§bControl");
  }

  // ----------------------------------------------------------------------------------------------------
  // Overridden methods
  // ----------------------------------------------------------------------------------------------------

  @Override
  public void updateInventory() {
    super.updateInventory();

    // Punishments | "ListPunishes"
    {
      set(
          Item
              .of(
                  ItemType.CHEST,
                  "&6" + PUNISHMENTS,
                  punishLore)
              .slot(PUNISHES_BUTTON_SLOT)
              .actionHandler("ListPunishes")
      );
    }

    // Punish | "NewPunish"
    {
      set(
          Item
              .of(
                  ItemType.CYAN_DYE,
                  "&6" + CREATE_NEW_NAME,
                  CREATE_NEW_LORE
              )
              .actionHandler("NewPunish")
              .slot(NEW_PUNISH_BUTTON_SLOT)
      );
    }

    // Settings | "Settings"
    {
      set(
          Item
              .of(
                  ItemType.COMPARATOR,
                  "&6" + SETTINGS,
                  " ",
                  VIEW_SETTINGS,
                  SimplePlugin.getNamed()
              )
              .slot(SETTINGS_BUTTON_SLOT)
              .actionHandler("Settings")
      );
    }

    set(
        Item
            .of(ItemType.CYAN_STAINED_GLASS_PANE)
            .name("")
            .lore("")
            .slot(45)
            .actionHandler("noAction")
    );

    setBackgroundItems(
        Item.of(ItemType.CYAN_STAINED_GLASS_PANE).build(),
        0, 9, 18, 27, 36, 8, 17, 26, 35, 44, 1, 7, 37, 43);
    // Settings | "Settings"
    {
      set(
          Item
              .of(
                  ItemType.COMPARATOR,
                  "&6" + SETTINGS,
                  "",
                  "&7" + VIEW_SETTINGS,
                  SimplePlugin.getNamed()
              )
              .slot(45)
              .actionHandler("Settings")
      );
    }
  }

  @Override
  public void reDisplay() {
    showTo(getPlayer());
  }

  @Override
  public void registerActionHandlers() {
    super.registerActionHandlers();

    registerActionHandler("Changing", (
        changing -> {
          PlayerBrowser.showTo(getPlayer());
          return CallResult.DENY_GRABBING;
        }));

    registerActionHandler("Settings", (
        settings -> {
          SettingsBrowser.showTo(getPlayer());
          return CallResult.DENY_GRABBING;
        }));

    registerActionHandler("NewPunish", (
        newPunish -> {
          PunishCreatorMenu.showTo(getPlayer());
          return CallResult.DENY_GRABBING;
        }));

    registerActionHandler("ListPunishes", (
        listPunishes -> {
          if (listPunishes.getClickType() == ClickType.RIGHT_CLICK) {
            PunishedPlayerBrowser.showTo(getPlayer());
            return CallResult.DENY_GRABBING;
          }

          AllPunishesBrowser.showTo(getPlayer());
          return CallResult.DENY_GRABBING;
        }));
  }
}
