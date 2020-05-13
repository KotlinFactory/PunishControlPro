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
import org.mineacademy.punishcontrol.core.PunishControlManager;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.settings.Settings.MySQL;
import org.mineacademy.punishcontrol.core.storage.MySQLStorageProvider;
import org.mineacademy.punishcontrol.core.storage.StorageType;
import org.mineacademy.punishcontrol.spigot.DaggerSpigotComponent;
import org.mineacademy.punishcontrol.spigot.Scheduler;
import org.mineacademy.punishcontrol.spigot.conversations.MySQLStorageConversation;
import org.mineacademy.punishcontrol.spigot.menus.browsers.SettingsBrowser;
import org.mineacademy.punishcontrol.spigot.menus.setting.AbstractSettingsMenu;
import org.mineacademy.punishcontrol.spigot.util.Schedulable;

public final class StorageSettingsMenu
    extends AbstractSettingsMenu
    implements Schedulable {

  public static final int CONNECT_SLOT = 12;
  public static final int USE_SLOT = 14;
  public static final int HOST_SLOT = 2;
  public static final int PORT_SLOT = 3;
  public static final int DATABASE_SLOT = 4;
  public static final int USER_SLOT = 5;
  public static final int PASSWORD_SLOT = 6;

  private static final MySQLStorageProvider mySQLStorageProvider =
      Providers.storageProvider() instanceof MySQLStorageProvider
          ? (MySQLStorageProvider) Providers.storageProvider()
          : new MySQLStorageProvider(Providers.exceptionHandler());

  private final Button connect;
  private final Button host;
  private final Button port;
  private final Button database;
  private final Button user;
  private final Button password;
  private final Button useButton;


  private boolean isConnecting;


  @Inject
  public StorageSettingsMenu(@NonNull final SettingsBrowser settingsBrowser) {
    super(settingsBrowser);

    setSize(9*2);
    useButton = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player,
          final Menu menu,
          final ClickType click) {

      }

      @Override
      public ItemStack getItem() {
        if (PunishControlManager.storageType() == StorageType.JSON) {
          return
              ItemCreator
                  .of(CompMaterial.COMMAND_BLOCK)
                  .name("&6Storage Type")
                  .lores(
                      Arrays.asList(
                          "",
                          "&7Click to use",
                          "&7MySQL as storage"
                      )
                  )
                  .build()
                  .makeMenuTool();
        } else {
          return ItemCreator
              .of(CompMaterial.COMMAND_BLOCK)
              .name("&6Storage Type")
              .lores(
                  Arrays.asList(
                      "",
                      "&7Click to use",
                      "&7JSON as storage"
                  )
              )
              .build()
              .makeMenuTool();
        }
      }
    };

    connect = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player,
          final Menu menu,
          final ClickType click) {

        try {
          if (isConnecting) {
            animateTitle("&cAlready connecting");
            return;
          }

          if (mySQLStorageProvider.isConnected()) {
            animateTitle("&cAlready connected");
            return;
          }

          isConnecting = true;
          restartMenu();
          async(() -> {
            try {
              mySQLStorageProvider.connect();
              isConnecting = false;
              Debugger.debug("MySQL", "Connected");
              restartMenu();
            } catch (final Throwable throwable) {
              animateTitle("&cCan't connect - See console");
              Debugger.saveError(throwable);
            } finally {
              isConnecting = false;
            }
          });
          return;
        } catch (final Throwable throwable) {
          animateTitle("&cCan't connect - See console");
          Debugger.saveError(throwable);
        }
      }

      @Override
      public ItemStack getItem() {
        if (mySQLStorageProvider.isConnected()) {
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
        return ItemCreator
            .of(CompMaterial.GREEN_STAINED_GLASS_PANE)
            .name("&7Connect")
            .lores(Arrays.asList(
                " ",
                "&7Try to connect ",
                "&7to MySQL using",
                "&7these settings",
                "&7Current state: &cfailed"))
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
            .name("&7Port")
            .lores(Arrays.asList(
                " ",
                "&7Click to",
                "&7set the port",
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

  @Override
  public ItemStack getItemAt(final int slot) {
    if (slot == USE_SLOT) {
      return useButton.getItem();
    }

    if (slot == CONNECT_SLOT) {
      return connect.getItem();
    }

    if (slot == HOST_SLOT) {
      return host.getItem();
    }

    if (slot == PORT_SLOT) {
      return port.getItem();
    }

    if (slot == DATABASE_SLOT) {
      return database.getItem();
    }

    if (slot == USER_SLOT) {
      return user.getItem();
    }

    if (slot == PASSWORD_SLOT) {
      return password.getItem();
    }

    return super.getItemAt(slot);
  }

  public static void showTo(@NonNull final Player player) {
    Scheduler.runAsync(() -> {
      DaggerSpigotComponent.create().storageMenu().displayTo(player, true);
    });
  }
}
