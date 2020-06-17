package org.mineacademy.punishcontrol.spigot.menus;

import java.util.Arrays;
import java.util.Collections;
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

  public static final int PLAYER_BROWSER_SLOT = 9 * 2 + 6;
  private static final String[] BROWSE_PLAYERS_LORE = {
      "&7View players",
      "&7to select",
      "&7one for more",
      "&7actions"};
  @NonNls
  private static final String PLAYERS = "Players";
  @NonNls
  private static final String PUNISHMENTS = "Punishments";
  private static final String[] BROWSER_PUNISHMENTS_LORE = {"",
      "Browse created punishments.",
      "Right click to view",
      "punished players"};
  private static final String[] CREATE_NEW_PUNISHMENT_LORE = {" ", "Make new punishment"};
  @NonNls
  private static final String CREATE_NEW = "Create New";
  @NonNls
  private static final String SETTINGS = "Settings";
  @NonNls
  private static final String VIEW_SETTINGS_FOR = "View Settings for";
  private final Button punishesButton;
  private final Button newButton;
  private final Button settingsButton;

  public static void showTo(@NonNull final Player player) {
    Scheduler.runAsync(() -> {
      final val menu = DaggerSpigotComponent.create().menuMain();
      menu.displayTo(player, true);
    });
  }

  @Inject
  public MainMenu(final TextureProvider textureProvider) {
    super(null,
        Collections.singletonList(
            ChangingButton
                .fromCustomHashes(
                    textureProvider.listTextures())
                .name("&6" + PLAYERS)
                .slot(24)
                .lore(
                    BROWSE_PLAYERS_LORE)
        ));

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
            .of(CompMaterial.CHEST,
                "&6" + PUNISHMENTS,
                BROWSER_PUNISHMENTS_LORE
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
            .name("&6" + CREATE_NEW)
            .lores(Arrays.asList(CREATE_NEW_PUNISHMENT_LORE))
            .build().make();
      }
    };

    settingsButton = new Button() {

      @Override
      public void onClickedInMenu(final Player pl, final Menu menu,
          final ClickType click) {
        SettingsBrowser.showTo(pl);
      }

      @Override
      public ItemStack getItem() {
        return ItemCreator
            .of(CompMaterial.COMPARATOR,
                "&6" + SETTINGS,
                "&7" + VIEW_SETTINGS_FOR,
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
