package org.mineacademy.punishcontrol.spigot.menus;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import lombok.NonNull;
import lombok.val;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NonNls;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.punishcontrol.core.injector.annotations.Localizable;
import org.mineacademy.punishcontrol.core.providers.TextureProvider;
import org.mineacademy.punishcontrol.spigot.DaggerSpigotComponent;
import org.mineacademy.punishcontrol.spigot.Scheduler;
import org.mineacademy.punishcontrol.spigot.menu.ChangingMenu;
import org.mineacademy.punishcontrol.spigot.menu.buttons.ChangingButton;
import org.mineacademy.punishcontrol.spigot.menus.browsers.AllPunishesBrowser;
import org.mineacademy.punishcontrol.spigot.menus.browsers.PlayerBrowser;
import org.mineacademy.punishcontrol.spigot.menus.browsers.PunishedPlayerBrowser;
import org.mineacademy.punishcontrol.spigot.menus.browsers.SettingsBrowser;
import org.mineacademy.punishcontrol.spigot.menus.punish.PunishCreatorMenu;
import org.mineacademy.punishcontrol.spigot.util.ItemStacks;

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

  private static final int PLAYER_BROWSER_SLOT = 9 * 2 + 6; // 24
  private static final int PUNISHES_BUTTON_SLOT = 9 * 2 + 2; // 20
  private static final int NEW_PUNISH_BUTTON_SLOT = 9 + 4; // 12
  private static final int SETTINGS_BUTTON_SLOT = 9 * 3 + 4; // 30

  // ----------------------------------------------------------------------------------------------------
  // Displaying
  // ----------------------------------------------------------------------------------------------------

  public static void showTo(@NonNull final Player player) {
    Scheduler.runAsync(() -> {
      final val menu = DaggerSpigotComponent.create().menuMain();
      menu.displayTo(player);
    });
  }

  // ----------------------------------------------------------------------------------------------------
  // Fields & Constructor's
  // ----------------------------------------------------------------------------------------------------

  private final Button punishesButton;
  private final Button newButton;
  private final Button settingsButton;

  @Inject
  public MainMenu(final TextureProvider textureProvider) {
    super(
        null,
        Collections.singletonList(
            ChangingButton
                .fromCustomHashes(
                    textureProvider.listTextures())
                .name("&6" + PLAYERS)
                .slot(PLAYER_BROWSER_SLOT)
                .lore(PLAYER_LORE)));

    setSize(9 * 5);
    setTitle("§3Punish§bControl");

    punishesButton = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player, final Menu menu, final ClickType click) {
        if (click.isRightClick()) {
          PunishedPlayerBrowser.showTo(player);
          return;
        }
        AllPunishesBrowser.showTo(player);
      }

      @Override
      public ItemStack getItem() {
        return ItemCreator
            .of(
                CompMaterial.CHEST,
                "&6" + PUNISHMENTS,
                punishLore
               )
            .glow(true).build().make();
      }
    };

    // This also sends the player to another menu but you can do additional stuff on clicking the button
    newButton = new Button() {

      @Override
      public void onClickedInMenu(
          final Player pl,
          final Menu menu,
          final ClickType click) {
        PunishCreatorMenu.showTo(pl);
      }

      @Override
      public ItemStack getItem() {
        return ItemCreator
            .of(ItemStacks.cyanDye())
            .name("&6" + CREATE_NEW_NAME)
            .lores(Arrays.asList(CREATE_NEW_LORE))
            .build().make();
      }
    };

    settingsButton = new Button() {

      @Override
      public void onClickedInMenu(
          final Player pl, final Menu menu,
          final ClickType click) {
        SettingsBrowser.showTo(pl);
      }

      @Override
      public ItemStack getItem() {
        return ItemCreator
            .of(
                CompMaterial.COMPARATOR,
                "&6" + SETTINGS,
                "&7" + VIEW_SETTINGS,
                SimplePlugin.getNamed(),
                ""
               )
            .build().make();
      }
    };
  }

  @Override
  public ItemStack findItem(final int slot) {

    if (Arrays.asList(0, 9, 18, 27, 36, 8, 17, 26, 35, 44, 1, 7, 37, 43)
        .contains(slot)) {
      return ItemCreator.of(ItemStacks.cyanGlassPane())
          .name(" ")
          .build()
          .make();
    }
    if (slot == 9 * 2 + 2) {
      return punishesButton.getItem();
    }

    if (slot == 9 + 4) {
      return newButton.getItem();
    }

    if (slot == 9 * 3 + 4) {
      return settingsButton.getItem();
    }

    return null;
  }

  @Override
  protected void onMenuClick(
      final Player player,
      final int slot,
      final ItemStack clicked) {

    if (slot != PLAYER_BROWSER_SLOT) {
      return;
    }

    PlayerBrowser.showTo(player);
  }

  @Override
  protected String[] getInfo() {
    return null;
  }
}
