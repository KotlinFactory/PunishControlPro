package org.mineacademy.punishcontrol.proxy.menus.settings;

import static org.mineacademy.punishcontrol.core.util.PunishControlPermissions.*;

import de.exceptionflug.mccommons.inventories.api.ClickType;
import de.exceptionflug.protocolize.items.ItemType;
import java.util.Arrays;
import java.util.UUID;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.mineacademy.bfo.plugin.SimplePlugin;
import org.mineacademy.burst.item.Item;
import org.mineacademy.burst.util.Scheduler;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.util.PunishControlPermissions;
import org.mineacademy.punishcontrol.proxy.DaggerProxyComponent;
import org.mineacademy.punishcontrol.proxy.menu.browser.AbstractPlayerBrowser;
import org.mineacademy.punishcontrol.proxy.menus.browsers.*;

@Getter
@Accessors(fluent = true)
public enum SettingTypes {

  PLAYER {
    @Override
    public Item itemCreator() {
      return Item.of(
          ItemType.PLAYER_HEAD,
          "&6Player settings",
          "&7Show permissions",
          "&7to apply punishes",
          "&7or view",
          "&7which permissions",
          "&7the player has");
    }

    @Override
    public void showMenu(final ProxiedPlayer player) {
      Scheduler.runAsync(() -> new AbstractPlayerBrowser(
          Providers.playerProvider(),
          Providers.textureProvider(),
          DaggerProxyComponent.create().settingsBrowser(),
          true) {

        @Override
        protected void onClick(final ClickType clickType, final UUID uuid) {
          PlayerSettingsMenu.showTo(getPlayer(), uuid);
        }

      }.displayTo(player));
    }

    @Override
    public boolean hasAccess(final ProxiedPlayer player) {
      return player
          .hasPermission(MENU_SETTINGS_PLAYER.permission());
    }
  },

  STORAGE {
    @Override
    public Item itemCreator() {
      return Item
          .of(ItemType.ENDER_CHEST)
          .name("&6Punish storage")
          .lore(
              "&7Click to setup MySQL",
              "&7or to change the",
              "&7storage-type"
          );
    }

    @Override
    public void showMenu(final ProxiedPlayer player) {
      StorageSettingsMenu.showTo(player);
    }

    @Override
    public boolean hasAccess(final ProxiedPlayer player) {
      return player
          .hasPermission(MENU_SETTINGS_STORAGE.permission());
    }
  },

  PUNISH_TEMPLATE {
    @Override
    public Item itemCreator() {
      return Item.of(ItemType.PAPER)
          .name("&6Punish templates")
          .lore(
              "&7View and add",
              "&7Punish templates");
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
  },

  CUSTOMIZATION {
    @Override
    public boolean hasAccess(ProxiedPlayer player) {
      return player.hasPermission(MENU_SETTINGS_CUSTOMIZATION.permission()
      );
    }

    @Override
    public Item itemCreator() {
      return Item
          .of(ItemType.WOODEN_AXE)
          .name("&6Customization")
          .lore(
              Arrays.asList(
                  "",
                  "&7Customize menus"
              )
          );
    }

    @Override
    public void showMenu(ProxiedPlayer player) {
      CustomItemBrowser.showTo(player);
    }
  },

  PUNISH_IMPORTER {
    @Override
    public boolean hasAccess(ProxiedPlayer player) {
      return player
          .hasPermission(IMPORT_PUNISHMENTS.permission());
    }

    @Override
    public Item itemCreator() {
      return Item
          .of(ItemType.CHEST_MINECART)
          .name("&6Import punishments")
          .lore(
              "Import punishments from",
              "other plugins or from vanialla"
          );
    }

    @Override
    public void showMenu(ProxiedPlayer player) {
      PunishImporterBrowser.showTo(player);
    }
  },

  NOTIFICATION {
    @Override
    public Item itemCreator() {
      return Item
          .of(
              ItemType.FIREWORK_ROCKET,
              "&6Notifications",
              "&7",
              "&7Notifications");
    }

    @Override
    public void showMenu(final ProxiedPlayer player) {
      NotificationBrowser.showTo(player);
    }

    @Override
    public boolean hasAccess(final ProxiedPlayer player) {
      return player.hasPermission(MENU_SETTINGS_NOTIFICATIONS.permission());
    }
  },

  LANGUAGE {
    @Override
    public boolean hasAccess(ProxiedPlayer player) {
      return player.hasPermission(MENU_SETTINGS_LANGUAGE.permission());
    }

    @Override
    public Item itemCreator() {
      return Item
          .of(
              ItemType.BOOKSHELF,
              "&6Language",
              "&7",
              "&7Choose the language ",
              "&7" + SimplePlugin.getNamed() + " is using");
    }

    @Override
    public void showMenu(ProxiedPlayer player) {
      LanguageBrowser.showTo(player);
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
