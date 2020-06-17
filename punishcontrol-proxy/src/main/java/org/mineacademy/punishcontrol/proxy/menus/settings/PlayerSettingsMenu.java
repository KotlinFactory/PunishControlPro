package org.mineacademy.punishcontrol.proxy.menus.settings;

import de.exceptionflug.mccommons.inventories.api.CallResult;
import de.exceptionflug.protocolize.items.ItemType;
import java.util.Arrays;
import java.util.UUID;
import lombok.NonNull;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.mineacademy.bfo.Players;
import org.mineacademy.burst.item.Item;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.settings.ItemSettings;
import org.mineacademy.punishcontrol.core.util.PunishControlPermissions;
import org.mineacademy.punishcontrol.proxy.DaggerProxyComponent;
import org.mineacademy.punishcontrol.proxy.menus.setting.AbstractSettingsMenu;
import org.mineacademy.punishcontrol.proxy.menus.browsers.GroupBrowser;
import org.mineacademy.punishcontrol.proxy.menus.browsers.PlayerPermissionsBrowser;

public final class PlayerSettingsMenu extends AbstractSettingsMenu {

  public static final int PERMISSION_BROWSER_SLOT = 0;
  public static final int GROUP_BROWSER_SLOT = 1;
  public static final int TOGGLE_PUNISHABLE_SLOT = 2;

  private final boolean targetOnline;
  private final UUID target;
  private final boolean targetPunishable;

  /*
    TODO:
      Make unpunishable
   */

  public static void showTo(
      @NonNull final ProxiedPlayer player,
      @NonNull final UUID target) {
    new PlayerSettingsMenu(target).displayTo(player);
  }

  private PlayerSettingsMenu(final UUID target) {
    super(DaggerProxyComponent.create().settingsBrowser(), 9 * 2);
    this.target = target;
    targetOnline = Players.find(target).isPresent();
    targetPunishable = Providers.playerProvider().punishable(target);
    setTitle("&8Settings for player");
  }

  @Override
  public void updateInventory() {
    super.updateInventory();

    // Groups | "Groups"
    {
      set(
          Item
              .of(ItemType.OBSIDIAN,
                  "&6Groups",
                  " ",
                  "&7View groups",
                  "&7the player has",
                  "&7See your settings.yml",
                  "&7to edit them")
              .slot(GROUP_BROWSER_SLOT)
              .actionHandler("Groups")
      );
    }

    // Permissions | "Permissions"
    {
      if (!targetOnline) {
        Item
            .of(ItemType.ICE,
                "&6Permissions",
                "",
                "&7Cant be used:",
                "&7target is offline")
            .slot(PERMISSION_BROWSER_SLOT)
            .actionHandler("Permissions");
      } else {
        set(
            Item
                .of(ItemType.ICE,
                    "&6Permissions",
                    "",
                    "&7View which",
                    "&7actions the player",
                    "&7is allowed to take")
                .slot(PERMISSION_BROWSER_SLOT)
                .actionHandler("Permissions")
        );
      }

      // Toggle punishable | "punishable"
      {
        if (targetPunishable) {
          set(
              Item
                  .ofString(ItemSettings.DISABLED.itemType())
                  .name("&6Toggle punishable")
                  .lore(
                      Arrays.asList(
                          "",
                          "&7Click to make target un-punishable",
                          "&7Target is currently punishable"
                      ))
                  .slot(TOGGLE_PUNISHABLE_SLOT)
                  .actionHandler("Punishable")
          );
        } else {
          set(
              Item
                  .ofString(ItemSettings.ENABLED.itemType())
                  .name("&6Toggle punishable")
                  .lore(
                      Arrays.asList(
                          "",
                          "&7Click to make target punishable",
                          "&7Target is currently unpunishable"
                      ))
                  .slot(TOGGLE_PUNISHABLE_SLOT)
                  .actionHandler("Punishable")
          );
        }
      }
    }
  }

  @Override
  public void reDisplay() {
    showTo(getPlayer(), target);
  }

  @Override
  public void registerActionHandlers() {
    registerActionHandler("Groups", (groups -> {
      GroupBrowser.showTo(getPlayer(), target, this);
      return CallResult.DENY_GRABBING;
    }));

    registerActionHandler("Permissions", (permissions -> {
      PlayerPermissionsBrowser.showTo(getPlayer(), target, this);
      return CallResult.DENY_GRABBING;
    }));

    registerActionHandler("Punishable", (punishable -> {
      if (!player.hasPermission(
          PunishControlPermissions.TOGGLE_PUNISHABLE.permission()
      )) {
        animateTitle("&cNo access");
        return CallResult.DENY_GRABBING;
      }
      Providers.playerProvider().punishable(target, !targetPunishable);
      showTo(getPlayer(), target);

      return CallResult.DENY_GRABBING;
    }));
  }
}

// ----------------------------------------------------------------------------------------------------
// Sub-Classes that are only needed for this menu
// ----------------------------------------------------------------------------------------------------

