package org.mineacademy.punishcontrol.proxy.menus;

import de.exceptionflug.mccommons.inventories.api.CallResult;
import de.exceptionflug.mccommons.inventories.api.ClickType;
import de.exceptionflug.protocolize.items.ItemType;
import java.util.Collections;
import javax.inject.Inject;
import lombok.NonNull;
import lombok.val;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.mineacademy.bfo.plugin.SimplePlugin;
import org.mineacademy.burst.item.ChangingButton;
import org.mineacademy.burst.item.Item;
import org.mineacademy.burst.menu.ChangingMenu;
import org.mineacademy.burst.util.Scheduler;
import org.mineacademy.punishcontrol.core.providers.TextureProvider;
import org.mineacademy.punishcontrol.proxy.DaggerProxyComponent;
import org.mineacademy.punishcontrol.proxy.menus.browsers.AllPunishesBrowser;
import org.mineacademy.punishcontrol.proxy.menus.browsers.PlayerBrowser;
import org.mineacademy.punishcontrol.proxy.menus.browsers.PunishedPlayerBrowser;
import org.mineacademy.punishcontrol.proxy.menus.browsers.SettingsBrowser;
import org.mineacademy.punishcontrol.proxy.menus.punish.PunishCreatorMenu;

public final class MainMenu extends ChangingMenu {

  public static final int PLAYER_BROWSER_SLOT = 9 * 2 + 6;
  public static final int PUNISHES_BUTTON_SLOT = 9 * 2 + 2;
  public static final int NEW_PUNISH_BUTTON_SLOT = 9 + 4;
  public static final int SETTINGS_BUTTON_SLOT = 9 * 3 + 4;

  public static void showTo(@NonNull final ProxiedPlayer player) {
    Scheduler.runAsync(() -> {
      final val menu = DaggerProxyComponent.create().menuMain();
      menu.displayTo(player);
    });
  }


  @Inject
  public MainMenu(final TextureProvider textureProvider) {
    super("MainMenu",
        Collections.singletonList(
            ChangingButton
                .fromCustomHashes(
                    textureProvider.listTextures())
                .name("&6Players")
                .slot(24)
                .lore("&7View players", "&7to select", "&7one for more",
                    "&7actions")),
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
              .of(ItemType.CHEST,
                  "&6Punishments",
                  "",
                  "Browse created punishments.",
                  "Right click to view",
                  "punished players")
              .slot(PUNISHES_BUTTON_SLOT)
              .actionHandler("ListPunishes")
      );
    }

    // Punish | "NewPunish"
    {
      set(
          Item
              .of(ItemType.CYAN_DYE,
                  "&6Create New",
                  " ",
                  "&7Create new punishment"
              )
              .actionHandler("NewPunish")
              .slot(NEW_PUNISH_BUTTON_SLOT)
      );
    }

    // Settings | "Settings"
    {
      set(
          Item
              .of(ItemType.COMPARATOR,
                  "&6Settings",
                  "&7View Settings for",
                  SimplePlugin.getNamed(),
                  ""
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
              .of(ItemType.COMPARATOR,
                  "&6Settings",
                  "&7View Settings for",
                  SimplePlugin.getNamed(),

                  ""
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

    registerActionHandler("Changing", (changing -> {
      PlayerBrowser.showTo(getPlayer());
      return CallResult.DENY_GRABBING;
    }));

    registerActionHandler("Settings", (settings -> {
      SettingsBrowser.showTo(getPlayer());
      return CallResult.DENY_GRABBING;
    }));

    registerActionHandler("NewPunish", (newPunish -> {
      PunishCreatorMenu.showTo(getPlayer());
      return CallResult.DENY_GRABBING;
    }));

    registerActionHandler("ListPunishes", (listPunishes -> {
      if (listPunishes.getClickType() == ClickType.RIGHT_CLICK) {
        PunishedPlayerBrowser.showTo(getPlayer());
        return CallResult.DENY_GRABBING;
      }

      AllPunishesBrowser.showTo(getPlayer());
      return CallResult.DENY_GRABBING;
    }));
  }
}
