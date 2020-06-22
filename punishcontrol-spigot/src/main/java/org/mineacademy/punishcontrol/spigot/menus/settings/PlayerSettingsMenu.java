package org.mineacademy.punishcontrol.spigot.menus.settings;

import java.util.Arrays;
import java.util.UUID;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.settings.ItemSettings;
import org.mineacademy.punishcontrol.core.util.PunishControlPermissions;
import org.mineacademy.punishcontrol.spigot.DaggerSpigotComponent;
import org.mineacademy.punishcontrol.spigot.Players;
import org.mineacademy.punishcontrol.spigot.menus.browsers.GroupBrowser;
import org.mineacademy.punishcontrol.spigot.menus.browsers.PermissionsBrowser;
import org.mineacademy.punishcontrol.spigot.menus.setting.AbstractSettingsMenu;

public final class PlayerSettingsMenu extends AbstractSettingsMenu {

  public static final int PERMISSION_BROWSER_SLOT = 0;
  public static final int GROUP_BROWSER_SLOT = 1;
  public static final int TOGGLE_PUNISHABLE_SLOT = 2;
  private final Button groupBrowser;
  private final Button permissionBrowser;

  private final Button togglePunishable;

  private final boolean targetOnline, targetPunishable;
  private final UUID target;

  /*
  TODO:
   Make unpunishable
   */

  public static void showTo(
      @NonNull final Player player,
      @NonNull final UUID target) {
    new PlayerSettingsMenu(target).displayTo(player, true);
  }

  private PlayerSettingsMenu(final UUID target) {
    super(DaggerSpigotComponent.create().settingsBrowser());
    this.target = target;
    targetOnline = Players.find(target).isPresent();
    targetPunishable = Providers.playerProvider().punishable(target);
    setTitle("&8Settings for player");

    setSize(9 * 2);

    groupBrowser = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player, final Menu menu, final ClickType click) {
        GroupBrowser.showTo(player, target, PlayerSettingsMenu.this);
      }

      @Override
      public ItemStack getItem() {
        return ItemCreator
            .of(
                CompMaterial.OBSIDIAN,
                "&6Groups",
                " ",
                "&7View groups",
                "&7the player has",
                "&7See your settings.yml",
                "&7to edit them")
            .build()
            .makeMenuTool();
      }
    };

    permissionBrowser = new Button() {

      @Override
      public void onClickedInMenu(
          final Player player, final Menu menu, final ClickType click) {
        if (!targetOnline) {
          animateTitle("&cTarget is offline");
          return;
        }
        PermissionsBrowser.showTo(player, target, PlayerSettingsMenu.this);
      }

      @Override
      public ItemStack getItem() {
        if (!targetOnline) {
          return ItemCreator
              .of(
                  CompMaterial.ICE,
                  "&6Permissions",
                  "",
                  "&7Cant be used:",
                  "&7target is offline")
              .build()
              .makeMenuTool();
        }

        return ItemCreator
            .of(
                CompMaterial.ICE,
                "&6Permissions",
                "",
                "&7View which",
                "&7actions the player",
                "&7is allowed to take")
            .build()
            .makeMenuTool();
      }
    };

    togglePunishable = new Button() {
      @Override
      public void onClickedInMenu(Player player, Menu menu, ClickType click) {
        if (!player.hasPermission(
            PunishControlPermissions.TOGGLE_PUNISHABLE.permission()
        )) {
          animateTitle("&cNo access");
          return;
        }
        Providers.playerProvider().punishable(target, !targetPunishable);
        showTo(getViewer(), target);
      }

      @Override
      public ItemStack getItem() {
        if (targetPunishable) {
          return ItemCreator
              .ofString(ItemSettings.DISABLED.itemType())
              .name("&6Toggle punishable")
              .lores(
                  Arrays.asList(
                      "",
                      "&7Click to make target unpunishable",
                      "&7Target is currently punishable"
                  )
              )
              .build()
              .makeMenuTool();

        }

        return ItemCreator
            .ofString(ItemSettings.ENABLED.itemType())
            .name("&6Toggle punishable")
            .lores(
                Arrays.asList(
                    "",
                    "&7Click to make target punishable",
                    "&7Target is currently unpunishable"
                )
            )
            .build()
            .makeMenuTool();

      }
    };
  }

  @Override
  public ItemStack getItemAt(final int slot) {
    if (slot == PERMISSION_BROWSER_SLOT) {
      return permissionBrowser.getItem();
    }
    if (slot == GROUP_BROWSER_SLOT) {
      return groupBrowser.getItem();
    }

    if (slot == TOGGLE_PUNISHABLE_SLOT) {
      return togglePunishable.getItem();
    }

    return null;
  }
}

// ----------------------------------------------------------------------------------------------------
// Sub-Classes that are only needed for this menu
// ----------------------------------------------------------------------------------------------------

