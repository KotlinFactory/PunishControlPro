package org.mineacademy.punishcontrol.proxy.menus.settings;


import de.exceptionflug.mccommons.inventories.api.CallResult;
import de.exceptionflug.protocolize.inventory.InventoryModule;
import de.exceptionflug.protocolize.items.ItemType;
import javax.inject.Inject;
import lombok.NonNull;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.mineacademy.bfo.debug.Debugger;
import org.mineacademy.burst.item.Item;
import org.mineacademy.punishcontrol.core.PunishControlManager;
import org.mineacademy.punishcontrol.core.conversation.StorageSettable;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.settings.ItemSettings;
import org.mineacademy.punishcontrol.core.settings.Settings.MySQL;
import org.mineacademy.punishcontrol.core.storage.MySQLStorageProvider;
import org.mineacademy.punishcontrol.core.storage.StorageType;
import org.mineacademy.punishcontrol.proxy.DaggerProxyComponent;
import org.mineacademy.punishcontrol.proxy.conversations.MySQLStorageConversation;
import org.mineacademy.punishcontrol.proxy.menus.browsers.SettingsBrowser;
import org.mineacademy.punishcontrol.proxy.menus.setting.AbstractSettingsMenu;

public final class StorageSettingsMenu
    extends AbstractSettingsMenu
    implements StorageSettable {

  private static final MySQLStorageProvider mySQLStorageProvider =
      Providers.storageProvider() instanceof MySQLStorageProvider
          ? (MySQLStorageProvider) Providers.storageProvider()
          : new MySQLStorageProvider(Providers.exceptionHandler());
  public static final int CONNECT_SLOT = 12;
  public static final int USE_SLOT = 14;
  public static final int HOST_SLOT = 2;
  public static final int PORT_SLOT = 3;
  public static final int DATABASE_SLOT = 4;
  public static final int USER_SLOT = 5;
  public static final int PASSWORD_SLOT = 6;

  private boolean isConnecting;

  @Inject
  public StorageSettingsMenu(@NonNull final SettingsBrowser settingsBrowser) {
    super(settingsBrowser, 9 * 2);
    setTitle("&8MySQL");
  }

  public static void showTo(@NonNull final ProxiedPlayer player) {
    DaggerProxyComponent.create().storageSettingsMenu().displayTo(player);
  }

  @Override
  public void reDisplay() {
    showTo(getPlayer());
  }

  @Override
  public void updateInventory() {
    super.updateInventory();

    // Connect | "Connect"
    {
      try {

        if (isConnecting) {
          set(
              Item
                  .ofString(ItemSettings.APPLY_ITEM.itemType())
                  .name("&7Connecting...")
                  .slot(CONNECT_SLOT)
                  .actionHandler("noAction")
          );
        } else if (mySQLStorageProvider.isConnected()) {
          set(
              Item
                  .of(ItemType.GREEN_STAINED_GLASS_PANE)
                  .name("&7Connect")
                  .lore(" ",
                      "&7Try to connect ",
                      "&7to MySQL using",
                      "&7these settings",
                      "&7Current state: &aSucceeded")
                  .slot(CONNECT_SLOT)
                  .actionHandler("Connect")
          );

        } else {
          set(
              Item
                  .of(ItemType.GREEN_STAINED_GLASS_PANE)
                  .name("&7Connect")
                  .lore(" ",
                      "&7Try to connect ",
                      "&7to MySQL using",
                      "&7these settings",
                      "&7Current state: &cNot connected")
                  .slot(CONNECT_SLOT)
                  .actionHandler("Connect")
          );
        }
      } catch (final Throwable throwable) {
      }
    }

    // Use | "Use"
    {
      if (PunishControlManager.storageType() == StorageType.JSON) {
        set(
            Item
                .of(ItemType.COMMAND_BLOCK)
                .name("&6Storage Type")
                .lore(
                    "",
                    "&7Click to use",
                    "&7MySQL as storage"
                )
                .slot(USE_SLOT)
                .actionHandler("Use")
        );
      } else {
        set(
            Item
                .of(ItemType.COMMAND_BLOCK)
                .name("&6Storage Type")
                .lore(
                    "",
                    "&7Click to use",
                    "&7JSON as storage"
                )
                .slot(USE_SLOT)
                .actionHandler("Use")
        );
      }
    }

    // Host | "Host"
    {
      set(
          Item
              .of(ItemType.YELLOW_STAINED_GLASS_PANE)
              .name("&7Host")
              .lore(
                  " ",
                  "&7Click to",
                  "&7set the host",
                  "&7Currently: " + (MySQL.HOST.isEmpty() ? "&cNot set" : MySQL.HOST))
              .actionHandler("Host")
              .slot(HOST_SLOT)
      );
    }

    // Port | "Port"
    {
      set(
          Item
              .of(ItemType.YELLOW_STAINED_GLASS_PANE)
              .name("&7Port")
              .lore(
                  " ",
                  "&7Click to",
                  "&7set the port",
                  "&7Currently: " + MySQL.PORT)
              .actionHandler("Port")
              .slot(PORT_SLOT)
      );
    }

    // Database | "Database"
    {
      set(
          Item
              .of(ItemType.YELLOW_STAINED_GLASS_PANE)
              .name("&7Database")
              .lore(
                  " ",
                  "&7Click to",
                  "&7set the database",
                  "&7Currently: " + (MySQL.DATABASE.isEmpty() ? "&cNot set"
                      : MySQL.DATABASE))
              .actionHandler("Database")
              .slot(DATABASE_SLOT)
      );
    }

    // User | "User"
    {
      set(
          Item
              .of(ItemType.YELLOW_STAINED_GLASS_PANE)
              .name("&7User")
              .lore(
                  " ",
                  "&7Click to",
                  "&7set the user",
                  "&7Currently: " + (MySQL.USER.isEmpty() ? "&cNot set" : MySQL.USER))
              .actionHandler("User")
              .slot(USER_SLOT)
      );
    }

    // Password | "Password"
    {
      set(
          Item
              .of(ItemType.YELLOW_STAINED_GLASS_PANE)
              .name("&7Password")
              .lore(
                  " ",
                  "&7Click to",
                  "&7set the password",
                  "&7Currently: " + (MySQL.PASSWORD.isEmpty()
                      ? "&cnot set" :
                      "****"))
              .actionHandler("Password")
              .slot(PASSWORD_SLOT)
      );
    }
  }

  @Override
  public void registerActionHandlers() {
    super.registerActionHandlers();

    registerActionHandler("Use", (use -> {
      if (PunishControlManager.storageType() == StorageType.JSON) {
        if (!mySQLStorageProvider.isConnected()) {
          animateTitle("&cNot connected!");
          return CallResult.DENY_GRABBING;
        }
        //Switching to json
        setToConfig("Storage", "MYSQL");
        PunishControlManager.setStorageType(StorageType.MYSQL);
        Providers.storageProvider(mySQLStorageProvider);
      } else {
        //Switching to json
        setToConfig("Storage", "JSON");
        PunishControlManager.setStorageType(StorageType.JSON);
        Providers.storageProvider(StorageType.JSON.getStorageProvider());
      }
      build();
      return CallResult.DENY_GRABBING;
    }));

    registerActionHandler("Connect", (connect -> {
      if (isConnecting) {
        animateTitle("&cAlready connecting");
        return CallResult.DENY_GRABBING;
      }

      if (mySQLStorageProvider.isConnected()) {
        animateTitle("&cAlready connected");
        return CallResult.DENY_GRABBING;
      }

      isConnecting = true;
      build();
      async(() -> {
        try {
          mySQLStorageProvider.connect();
          isConnecting = false;
          Debugger.debug("MySQL", "Connected");
          build();
        } catch (final Throwable throwable) {
          animateTitle("&cCan't connect - See console");
          Debugger.saveError(throwable);
        } finally {
          isConnecting = false;
        }
      });
      return CallResult.DENY_GRABBING;
    }));

    registerActionHandler("Host", (click -> {
      InventoryModule.closeAllInventories(getPlayer());
      MySQLStorageConversation.create(getPlayer(), "Host").start();
      return CallResult.DENY_GRABBING;
    }));

    registerActionHandler("Port", (port -> {
      InventoryModule.closeAllInventories(getPlayer());
      MySQLStorageConversation.create(getPlayer(), "Port").start();
      return CallResult.DENY_GRABBING;
    }));

    registerActionHandler("Database", (database -> {
      InventoryModule.closeAllInventories(getPlayer());

      MySQLStorageConversation.create(getPlayer(), "Database").start();
      return CallResult.DENY_GRABBING;
    }));

    registerActionHandler("User", (user -> {
      InventoryModule.closeAllInventories(getPlayer());

      MySQLStorageConversation.create(getPlayer(), "User").start();
      return CallResult.DENY_GRABBING;
    }));

    registerActionHandler("Password", (password -> {
      InventoryModule.closeAllInventories(getPlayer());

      MySQLStorageConversation.create(getPlayer(), "Password").start();
      return CallResult.DENY_GRABBING;
    }));
  }
}
