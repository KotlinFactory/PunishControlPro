package org.mineacademy.punishcontrol.spigot.menus.settings;

import static org.mineacademy.punishcontrol.core.util.PunishControlPermissions.IMPORT_PUNISHMENTS;
import static org.mineacademy.punishcontrol.core.util.PunishControlPermissions.MENU_SETTINGS_LANGUAGE;

import java.util.Arrays;
import java.util.UUID;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;
import org.bukkit.entity.Player;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.util.PunishControlPermissions;
import org.mineacademy.punishcontrol.spigot.DaggerSpigotComponent;
import org.mineacademy.punishcontrol.spigot.Scheduler;
import org.mineacademy.punishcontrol.spigot.menu.browser.AbstractPlayerBrowser;
import org.mineacademy.punishcontrol.spigot.menus.browsers.*;

@Getter
@Accessors(fluent = true)
public enum SettingTypes {

  PLAYER {
    @Override
    public ItemCreator itemCreator() {
      return ItemCreator.of(
          CompMaterial.PLAYER_HEAD,
          "&6Player settings",
          "&7Show permissions",
          "&7to apply punishes",
          "&7or view",
          "&7which permissions",
          "&7a player has")
          .build();
    }

    @Override
    public void showMenu(final Player player) {
      Scheduler.runAsync(() -> {
        final val browser = new AbstractPlayerBrowser(
            Providers.playerProvider(),
            Providers.textureProvider(),
            DaggerSpigotComponent.create().settingsBrowser(),
            true) {

          @Override
          public void onClick(final UUID data) {
            PlayerSettingsMenu.showTo(getViewer(), data);
          }
        };
        Scheduler.runSync(() -> browser.displayTo(player));
      });
    }

    @Override
    public boolean hasAccess(final Player player) {
      return player.hasPermission(
          PunishControlPermissions.MENU_SETTINGS_PLAYER.permission());
    }
  },

  STORAGE {
    @Override
    public ItemCreator itemCreator() {
      return ItemCreator
          .of(
              CompMaterial.ENDER_CHEST,
              "&6Punish storage",
              "&7Click to setup MySQL",
              "&7or to change the",
              "&7storage-type")
          .build();
    }

    @Override
    public void showMenu(final Player player) {
      StorageSettingsMenu.showTo(player);
    }

    @Override
    public boolean hasAccess(final Player player) {
      return player.hasPermission(
          PunishControlPermissions.MENU_SETTINGS_STORAGE.permission());
    }
  },

  PUNISH_TEMPLATE {
    @Override
    public ItemCreator itemCreator() {
      return ItemCreator.of(
          CompMaterial.PAPER,
          "&6Punish templates",
          "&7View and add",
          "&7Punish-Templates")
          .build();
    }

    @Override
    public void showMenu(final Player player) {
      PunishTemplateBrowser.showTo(player);
    }

    @Override
    public boolean hasAccess(final Player player) {
      return player.hasPermission(
          PunishControlPermissions.MENU_SETTINGS_TEMPLATES.permission());
    }
  },

  CUSTOMIZATION {
    @Override
    public boolean hasAccess(Player player) {
      return player.hasPermission(
          PunishControlPermissions.MENU_SETTINGS_CUSTOMIZATION.permission()
                                 );
    }

    @Override
    public ItemCreator itemCreator() {
      return ItemCreator
          .of(CompMaterial.WOODEN_AXE)
          .name("&6Customization")
          .lores(
              Arrays.asList(
                  "",
                  "&7Customize menus"
                           )
                )
          .build();
    }

    @Override
    public void showMenu(Player player) {
      CustomItemBrowser.showTo(player);
    }
  },

  PUNISH_IMPORTER {
    @Override
    public boolean hasAccess(Player player) {
      return player.hasPermission(IMPORT_PUNISHMENTS.permission());
    }

    @Override
    public ItemCreator itemCreator() {
      return ItemCreator
          .of(CompMaterial.CHEST_MINECART)
          .name("&6Import punishments")
          .lores(
              Arrays.asList(
                  "Import punishments from",
                  "other plugins or from vanialla"
                           )
                )
          .build();
    }

    @Override
    public void showMenu(Player player) {
      PunishImporterBrowser.showTo(player);
    }
  },

  NOTIFICATION {
    @Override
    public ItemCreator itemCreator() {
      return ItemCreator.of(
          CompMaterial.FIREWORK_ROCKET,
          "&6Notifications",
          "&7",
          "&7View notifications")
          .build();
    }

    @Override
    public void showMenu(final Player player) {
      NotificationBrowser.showTo(player);
    }

    @Override
    public boolean hasAccess(final Player player) {
      return player.hasPermission(
          PunishControlPermissions.MENU_SETTINGS_NOTIFICATIONS.permission());
    }
  },

  LANGUAGE {
    @Override
    public boolean hasAccess(Player player) {
      return player.hasPermission(MENU_SETTINGS_LANGUAGE.permission());
    }

    @Override
    public ItemCreator itemCreator() {
      return ItemCreator
          .of(
              CompMaterial.BOOKSHELF,
              "&6Language",
              "&7",
              "&7Choose the language ",
              "&7" + SimplePlugin.getNamed() + " is using")
          .build();
    }

    @Override
    public void showMenu(Player player) {
      LanguageBrowser.showTo(player);
    }

  };

  public boolean hasAccess(final Player player) {
    throw new AbstractMethodError("Not implemented");
  }

  public ItemCreator itemCreator() {
    throw new AbstractMethodError("Not implemented");
  }

  public void showMenu(final Player player) {
    throw new AbstractMethodError("Not implemented");
  }
}
