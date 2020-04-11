package org.mineacademy.punishcontrol.spigot.menus;

import java.util.Arrays;
import java.util.Collections;
import javax.inject.Inject;
import lombok.NonNull;
import lombok.val;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
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
import org.mineacademy.punishcontrol.spigot.menus.browser.PlayerBrowser;
import org.mineacademy.punishcontrol.spigot.menus.browser.PunishBrowser;
import org.mineacademy.punishcontrol.spigot.menus.browser.SettingsBrowser;
import org.mineacademy.punishcontrol.spigot.menus.punish.PunishCreatorMenu;
import org.mineacademy.punishcontrol.spigot.util.ItemStacks;

public final class MainMenu extends ChangingMenu {

  public static final int PLAYER_BROWSER_SLOT = 9 * 2 + 6;
  private final Button punishesButton;
  private final Button newButton;
  private final Button settingsButton;
  private final TextureProvider textureProvider;

  @Inject
  public MainMenu(final TextureProvider textureProvider) {
    super(null, Collections.singletonList(
        ChangingButton
            .fromCustomHashes(
                textureProvider.listTextures())
            .name("&6Players")
            .slot(24)
            .lore("&7View players", "&7to select", "&7one for more",
                "&7actions")
    ));
    this.textureProvider = textureProvider;
    setSize(9 * 5);
    setTitle("§3Punish§bControl");

    punishesButton = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player, final Menu menu, final ClickType click) {

        PunishBrowser.showTo(player);
      }

      @Override
      public ItemStack getItem() {
        return ItemCreator
            .of(CompMaterial.CHEST,
                "&6Punishes",
                "",
                "Browse created Punishes.")
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
            .name("&6Create New")
            .lores(Arrays.asList(" ", "Make new punish"))
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
                "&6Settings",
                "&7View Settings for",
                SimplePlugin.getNamed(),
                ""
            )
            .build().make();
      }
    };
  }

  public static void showTo(@NonNull final Player player) {
    Scheduler.runAsync(() -> {
      final val menu = DaggerSpigotComponent.create().menuMain();

      Scheduler.runSync(() -> menu.displayTo(player));
    });
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
  protected void onMenuClick(final Player player, final int slot,
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
