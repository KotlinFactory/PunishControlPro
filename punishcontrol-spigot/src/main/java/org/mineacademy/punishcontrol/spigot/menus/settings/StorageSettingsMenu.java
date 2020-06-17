package org.mineacademy.punishcontrol.spigot.menus.settings;

import java.util.Arrays;
import javax.inject.Inject;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NonNls;
import org.mineacademy.fo.debug.Debugger;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.punishcontrol.core.PunishControlManager;
import org.mineacademy.punishcontrol.core.setting.Replacer;
import org.mineacademy.punishcontrol.core.settings.ItemSettings;
import org.mineacademy.punishcontrol.core.settings.Settings.MySQL;
import org.mineacademy.punishcontrol.core.storage.StorageType;
import org.mineacademy.punishcontrol.core.storage.StorageTypes;
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

  @NonNls
  private static final String USER = "User";
  @NonNls
  private static final String CONNECT = "Connect";
  private static final String[] CONNECTION_FAILED_LORE = {" ",
      "&7Try to connect ",
      "&7to MySQL using",
      "&7these settings",
      "&7Current state: &cfailed"};
  private static final String[] CONNECTION_SUCCEEDED_LORE = {" ",
      "&7Try to connect ",
      "&7to MySQL using",
      "&7these settings",
      "&7Current state: &aSucceeded"};
  private static final String[] USE_JSON_LORE = {"",
      "&7Click to use",
      "&7JSON as storage"};
  private static final String[] USE_MYSQL_LORE = {"",
      "&7Click to use",
      "&7MySQL as storage"};
  @NonNls
  private static final String STORAGE_TYPE = "Storage Type";
  @NonNls
  private static final String ALREADY_CONNECTING = "Already connecting";
  @NonNls
  private static final String ALREADY_CONNECTED = "Already connected";
  @NonNls
  private static final String STORAGE_SETTINGS = "Storage settings";
  @NonNls
  private static final String PASSWORD = "Password";

  private static Replacer replacer = Replacer.of(
      " ",
      "&7Click to",
      "&7set the {type}",
      "&7Currently: {currently}");

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

    setTitle("&8" + STORAGE_SETTINGS);
    setSize(9 * 2);
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
                  .name("&6" + STORAGE_TYPE)
                  .lores(
                      Arrays.asList(
                          USE_MYSQL_LORE
                      )
                  )
                  .build()
                  .makeMenuTool();
        } else {
          return ItemCreator
              .of(CompMaterial.COMMAND_BLOCK)
              .name("&6" + STORAGE_TYPE)
              .lores(
                  Arrays.asList(
                      USE_JSON_LORE
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
            animateTitle("&c" + ALREADY_CONNECTING);
            return;
          }

          if (StorageTypes.mySQLStorageProvider.isConnected()) {
            animateTitle("&c" + ALREADY_CONNECTED);
            return;
          }

          isConnecting = true;
          restartMenu();
          async(() -> {
            try {
              StorageTypes.mySQLStorageProvider.connect();
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
        //
        if (StorageTypes.mySQLStorageProvider.isConnected()) {
          return ItemCreator
              .ofString(ItemSettings.ENABLED.itemType())
              .name("&7" + CONNECT)
              .lores(Arrays.asList(
                  CONNECTION_SUCCEEDED_LORE))
              .build()
              .makeMenuTool();
        }
        return ItemCreator
            .ofString(ItemSettings.DISABLED.itemType())
            .name("&7" + CONNECT)
            .lores(Arrays.asList(
                CONNECTION_FAILED_LORE))
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
            .of(CompMaterial.YELLOW_STAINED_GLASS_PANE)
            .name("&7Host")
            .lores(Arrays.asList(
                replacer.replaceAll("type", "host", "currently", (MySQL.HOST.isEmpty()
                    ? "&cNot set"
                    : MySQL.HOST)).replacedMessage()
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
                replacer.replaceAll("type", "port", "currently", MySQL.PORT)
                    .replacedMessage()
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
                replacer
                    .replaceAll("type", "database", "currently", (MySQL.DATABASE.isEmpty()
                        ? "&cNot set"
                        : MySQL.DATABASE)).replacedMessage()
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
        MySQLStorageConversation.create(USER).start(player);
      }

      @Override
      public ItemStack getItem() {

        return ItemCreator
            .of(CompMaterial.YELLOW_STAINED_GLASS_PANE)
            .name("&7" + USER)
            .lores(Arrays.asList(
                replacer
                    .replaceAll("type", "user", "currently", (MySQL.USER.isEmpty()
                        ? "&cNot set"
                        : MySQL.USER)).replacedMessage()
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

        MySQLStorageConversation.create(PASSWORD).start(player);
      }

      @Override
      public ItemStack getItem() {
        return ItemCreator
            .of(CompMaterial.YELLOW_STAINED_GLASS_PANE)
            .name("&7" + PASSWORD)
            .lores(Arrays.asList(
                " ",
                "&7Click to",
                "&7set the password",
                "&7Currently: " + (MySQL.PASSWORD.isEmpty()
                    ? "&cnot set"
                    : "****")
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
