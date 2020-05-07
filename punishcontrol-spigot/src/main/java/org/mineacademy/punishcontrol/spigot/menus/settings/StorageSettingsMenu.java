package org.mineacademy.punishcontrol.spigot.menus.settings;

import java.util.Arrays;
import javax.inject.Inject;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.debug.Debugger;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.punishcontrol.core.settings.Settings.MySQL;
import org.mineacademy.punishcontrol.core.storage.MySQLStorageProvider;
import org.mineacademy.punishcontrol.spigot.DaggerSpigotComponent;
import org.mineacademy.punishcontrol.spigot.conversations.MySQLStorageConversation;
import org.mineacademy.punishcontrol.spigot.menus.browsers.SettingsBrowser;
import org.mineacademy.punishcontrol.spigot.menus.setting.AbstractSettingsMenu;
import org.mineacademy.punishcontrol.spigot.util.Schedulable;

public final class StorageSettingsMenu
    extends AbstractSettingsMenu
    implements Schedulable {

  private final MySQLStorageProvider mySQLStorageProvider;
  private final Button connect;
  private final Button host;
  private final Button port;
  private final Button database;
  private final Button user;
  private final Button password;

  @Inject
  public StorageSettingsMenu(
      @NonNull final MySQLStorageProvider mySQLStorageProvider,
      @NonNull final SettingsBrowser settingsBrowser) {
    super(settingsBrowser);
    this.mySQLStorageProvider = mySQLStorageProvider;
    connect = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player,
          final Menu menu,
          final ClickType click) {


        try {
          mySQLStorageProvider.connect();
        } catch (final Throwable throwable) {
          animateTitle("&cCan't connect - See console");
          Debugger.saveError(throwable);
        }
      }

      @Override
      public ItemStack getItem() {
        return ItemCreator
            .of(CompMaterial.GREEN_STAINED_GLASS_PANE)
            .name("&7Connect")
            .lores(Arrays.asList(
                " ",
                "&7Try to connect ",
                "&7to MySQL using",
                "&7these settings",
                "&7Current state: &aSucceeded"))
            .build()
            .makeMenuTool();
      }
    };

    host = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player,
          final Menu menu,
          final ClickType click) {
        MySQLStorageConversation.create("Host").start(player);
      }

      @Override
      public ItemStack getItem() {
        return ItemCreator
            .of(CompMaterial.GREEN_STAINED_GLASS_PANE)
            .name("&7Connect")
            .lores(Arrays.asList(
                " ",
                "&7Try to connect ",
                "&7to MySQL using",
                "&7these settings",
                "&7Current state: &cFailed"
            ))
            .build()
            .makeMenuTool();
      }
    };

    port = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player,
          final Menu menu,
          final ClickType click) {

        MySQLStorageConversation.create("Port").start(player);

      }

      @Override
      public ItemStack getItem() {
        return ItemCreator
            .of(CompMaterial.YELLOW_STAINED_GLASS_PANE)
            .name("&7Host")
            .lores(Arrays.asList(
                " ",
                "&7Click to",
                "&7set the host",
                "&7Currently: " + (MySQL.HOST.isEmpty() ? "&cNot set" : MySQL.HOST)
            ))
            .build()
            .makeMenuTool();
      }
    };

    database = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player,
          final Menu menu,
          final ClickType click) {
        MySQLStorageConversation.create("Database").start(player);
      }

      @Override
      public ItemStack getItem() {
        return ItemCreator
            .of(CompMaterial.YELLOW_STAINED_GLASS_PANE)
            .name("&7Database")
            .lores(Arrays.asList(
                " ",
                "&7Click to",
                "&7set the database",
                "&7Currently: " + (MySQL.DATABASE.isEmpty() ? "&cNot set"
                    : MySQL.DATABASE)
            ))
            .build()
            .makeMenuTool();
      }
    };

    user = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player,
          final Menu menu,
          final ClickType click) {
        MySQLStorageConversation.create("User").start(player);
      }

      @Override
      public ItemStack getItem() {
        return ItemCreator
            .of(CompMaterial.YELLOW_STAINED_GLASS_PANE)
            .name("&7User")
            .lores(Arrays.asList(
                " ",
                "&7Click to",
                "&7set the user",
                "&7Currently: " + (MySQL.USER.isEmpty() ? "&cNot set" : MySQL.USER)
            ))
            .build()
            .makeMenuTool();
      }
    };

    password = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player,
          final Menu menu,
          final ClickType click) {

        MySQLStorageConversation.create("Password").start(player);
      }

      @Override
      public ItemStack getItem() {
        return ItemCreator
            .of(CompMaterial.YELLOW_STAINED_GLASS_PANE)
            .name("&7Password")
            .lores(Arrays.asList(
                " ",
                "&7Click to",
                "&7set the password",
                "&7Currently: " + (MySQL.PASSWORD.isEmpty()
                    ? "&cnot set" :
                    "****")
            ))
            .build()
            .makeMenuTool();
      }
    };
  }

  public static void showTo(@NonNull final Player player) {
    DaggerSpigotComponent.create().storageMenu().displayTo(player, true);
  }
}
