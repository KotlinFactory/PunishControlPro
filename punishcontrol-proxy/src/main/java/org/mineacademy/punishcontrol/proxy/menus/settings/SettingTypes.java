package org.mineacademy.punishcontrol.proxy.menus.settings;


import de.exceptionflug.mccommons.inventories.api.ClickType;
import de.exceptionflug.protocolize.items.ItemType;
import java.util.UUID;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.mineacademy.burst.item.Item;
import org.mineacademy.burst.util.Scheduler;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.util.PunishControlPermissions;
import org.mineacademy.punishcontrol.proxy.DaggerProxyComponent;
import org.mineacademy.punishcontrol.proxy.menu.browser.AbstractPlayerBrowser;
import org.mineacademy.punishcontrol.proxy.menus.browsers.PunishTemplateBrowser;

@Getter
@Accessors(fluent = true)
public enum SettingTypes {

  PLAYER {
    @Override
    public Item itemCreator() {
      return Item.of(ItemType.PLAYER_HEAD,
          "&6Player settings",
          "&7Give permissions",
          "&7to apply punishes",
          "&7or view",
          "&7which permissions",
          "&7a player has");
    }

    @Override
    public void showMenu(final ProxiedPlayer player) {
      Scheduler.runAsync(() -> {
        final val browser = new AbstractPlayerBrowser(
            Providers.playerProvider(),
            Providers.textureProvider(),
            DaggerProxyComponent.create().menuMain(),
            true) {

          @Override
          protected void onClick(final ClickType clickType, final UUID uuid) {
            PlayerSettingsMenu.showTo(getPlayer(), uuid);
          }
        };
        browser.displayTo(player);
      });
    }

    @Override
    public boolean hasAccess(final ProxiedPlayer player) {
      return player.hasPermission(
          PunishControlPermissions.MENU_SETTINGS_PLAYER.permission());
    }
  },

  STORAGE {
    @Override
    public Item itemCreator() {
      return Item
          .of(ItemType.ENDER_CHEST)
          .name("&6Punish Storage")
          .lore(
              "&7Click here to",
              "&7change how punishes",
              "&7are stored");
    }

    @Override
    public void showMenu(final ProxiedPlayer player) {
      StorageSettingsMenu.showTo(player);
    }

    @Override
    public boolean hasAccess(final ProxiedPlayer player) {
      return player.hasPermission(
          PunishControlPermissions.MENU_SETTINGS_STORAGE.permission());
    }
  },

  PUNISH_TEMPLATE {
    @Override
    public Item itemCreator() {
      return Item.of(ItemType.PAPER)
          .name("&6Punish-Templates")
          .lore(
              "&7View and add",
              "&7Punish-Templates");
    }

    @Override
    public void showMenu(final ProxiedPlayer player) {
      PunishTemplateBrowser.showTo(player);
    }

    @Override
    public boolean hasAccess(final ProxiedPlayer player) {
      return player.hasPermission(
          PunishControlPermissions.MENU_SETTINGS_TEMPLATES.permission());
    }
  };

  public boolean hasAccess(final ProxiedPlayer player) {
    throw new AbstractMethodError("Not implemented");
  }

  public Item itemCreator() {
    throw new AbstractMethodError("Not implemented");
  }

  public void showMenu(final ProxiedPlayer player) {
    throw new AbstractMethodError("Not implemented");
  }
}
