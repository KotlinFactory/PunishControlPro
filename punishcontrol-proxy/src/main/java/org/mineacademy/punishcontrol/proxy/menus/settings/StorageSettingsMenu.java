package org.mineacademy.punishcontrol.proxy.menus.settings;


import de.exceptionflug.mccommons.inventories.api.CallResult;
import de.exceptionflug.protocolize.inventory.InventoryModule;
import de.exceptionflug.protocolize.items.ItemType;
import javax.inject.Inject;
import lombok.NonNull;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NonNls;
import org.mineacademy.bfo.debug.Debugger;
import org.mineacademy.burst.item.Item;
import org.mineacademy.punishcontrol.core.PunishControlManager;
import org.mineacademy.punishcontrol.core.conversation.StorageSettable;
import org.mineacademy.punishcontrol.core.injector.annotations.Localizable;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.setting.Replacer;
import org.mineacademy.punishcontrol.core.settings.ItemSettings;
import org.mineacademy.punishcontrol.core.settings.Settings.MySQL;
import org.mineacademy.punishcontrol.core.storage.StorageType;
import org.mineacademy.punishcontrol.core.storage.StorageTypes;
import org.mineacademy.punishcontrol.proxy.DaggerProxyComponent;
import org.mineacademy.punishcontrol.proxy.conversations.MySQLStorageConversation;
import org.mineacademy.punishcontrol.proxy.menus.browsers.SettingsBrowser;
import org.mineacademy.punishcontrol.proxy.menus.setting.AbstractSettingsMenu;

@Localizable
public final class StorageSettingsMenu
    extends AbstractSettingsMenu
    implements StorageSettable {

  // ----------------------------------------------------------------------------------------------------
  // Localization
  // ----------------------------------------------------------------------------------------------------

  @NonNls
  @Localizable("Menu.Proxy.StorageSettingsMenu.Can_t_Connect")
  private static final String CAN_T_CONNECT = "Can't connect - See console";
  @Localizable("Menu.Proxy.StorageSettingsMenu.AlreadyConnected")
  @NonNls
  private static final String ALREADY_CONNECTED = "Already connected";
  @NonNls
  @Localizable("Menu.Proxy.StorageSettingsMenu.AlreadyConnecting")
  private static final String ALREADY_CONNECTING = "Already connecting";
  @Localizable("Menu.Proxy.StorageSettingsMenu.ConnectionFailed")
  private static final String[] CONNECT_FAILED_LORE = {
      " ",
      "&7Try to connect ",
      "&7to MySQL using",
      "&7these settings",
      "&7Current state: &cNot connected"};
  @Localizable("Menu.Proxy.StorageSettingsMenu.ConnectionFailed")
  private static final String[] CONNECT_SUCCESS_LORE = {
      " ",
      "&7Try to connect ",
      "&7to MySQL using",
      "&7these settings",
      "&7Current state: &aSucceeded"};
  @Localizable("Parts.Connect")
  private static final String CONNECT = "Connect";
  @Localizable("Parts.StorageType")
  private static final String STORAGE_TYPE = "Storage Type";

  private static final String[] USE_MYSQL_LORE = {
      "",
      "&7Click to use",
      "&7MySQL as storage"};

  private static final String[] USE_JSON_LORE = {
      "",
      "&7Click to use",
      "&7JSON as storage"};

  // ----------------------------------------------------------------------------------------------------
  // Button positions
  // ----------------------------------------------------------------------------------------------------

  private static final int CONNECT_SLOT = 12;
  private static final int USE_SLOT = 14;
  private static final int HOST_SLOT = 2;
  private static final int PORT_SLOT = 3;
  private static final int DATABASE_SLOT = 4;
  private static final int USER_SLOT = 5;
  private static final int PASSWORD_SLOT = 6;

  // ----------------------------------------------------------------------------------------------------
  // Showing up
  // ----------------------------------------------------------------------------------------------------

  public static void showTo(@NonNull final ProxiedPlayer player) {
    DaggerProxyComponent.create().storageSettingsMenu().displayTo(player);
  }

  // ----------------------------------------------------------------------------------------------------
  // Fields & Constructors
  // ----------------------------------------------------------------------------------------------------

  private boolean isConnecting;

  @Inject
  public StorageSettingsMenu(@NonNull final SettingsBrowser settingsBrowser) {
    super(settingsBrowser, 9 * 2);
    setTitle("&8MySQL");
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

        if (isConnecting)
          set(
              Item
                  .ofString(ItemSettings.APPLY_ITEM.itemType())
                  .name("&7Connecting...")
                  .slot(CONNECT_SLOT)
                  .actionHandler("noAction")
          );
        else if (StorageTypes.mySQLStorageProvider.isConnected())
          set(
              Item
                  .of(ItemType.GREEN_STAINED_GLASS_PANE)
                  .name("&6" + CONNECT)
                  .lore(CONNECT_SUCCESS_LORE)
                  .slot(CONNECT_SLOT)
                  .actionHandler("Connect")
          );
        else
          set(
              Item
                  .of(ItemType.GREEN_STAINED_GLASS_PANE)
                  .name("&7Connect")
                  .lore(CONNECT_FAILED_LORE)
                  .slot(CONNECT_SLOT)
                  .actionHandler("Connect")
          );
      } catch (final Throwable throwable) {
      }
    }

    // Use | "Use"
    {
      if (PunishControlManager.storageType() == StorageType.JSON)
        set(
            Item
                .of(ItemType.COMMAND_BLOCK)
                .name(STORAGE_TYPE)
                .lore(
                    USE_MYSQL_LORE
                )
                .slot(USE_SLOT)
                .actionHandler("Use")
        );
      else
        set(
            Item
                .of(ItemType.COMMAND_BLOCK)
                .name(STORAGE_TYPE)
                .lore(
                    USE_JSON_LORE
                )
                .slot(USE_SLOT)
                .actionHandler("Use")
        );
    }

    // Host | "Host"
    {
      final Replacer HOST_REPLACER = Replacer
          .of(" ",
              "&7Click to",
              "&7set the host",
              "&7Currently: {currently}");

      HOST_REPLACER
          .find("currently")
          .replace((MySQL.HOST.isEmpty() ? "&cNot set" : MySQL.HOST));
      set(
          Item
              .of(ItemType.YELLOW_STAINED_GLASS_PANE)
              .name("&7Host")
              .lore(
                  HOST_REPLACER.replacedMessage()
              )
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
                  "&7Currently: " + (MySQL.DATABASE.isEmpty()
                      ? "&cNot set"
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
                  "&7Currently: " + (MySQL.USER.isEmpty()
                      ? "&cNot set"
                      : MySQL.USER))
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
                      ? "&cnot set"
                      : "****"))
              .actionHandler("Password")
              .slot(PASSWORD_SLOT)
      );
    }
  }

  // ----------------------------------------------------------------------------------------------------
  // Overridden methods
  // ----------------------------------------------------------------------------------------------------

  @Override
  public void registerActionHandlers() {
    super.registerActionHandlers();

    registerActionHandler("Use", (use -> {
      if (PunishControlManager.storageType() == StorageType.JSON) {
        if (!StorageTypes.mySQLStorageProvider.isConnected()) {
          animateTitle("&cNot connected!");
          return CallResult.DENY_GRABBING;
        }
        //Switching to json
        setToConfig("Storage", "MYSQL");
        PunishControlManager.setStorageType(StorageType.MYSQL);
        Providers.storageProvider(StorageTypes.mySQLStorageProvider);
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
        animateTitle("&c" + ALREADY_CONNECTING);
        return CallResult.DENY_GRABBING;
      }

      if (StorageTypes.mySQLStorageProvider.isConnected()) {
        animateTitle("&c" + ALREADY_CONNECTED);
        return CallResult.DENY_GRABBING;
      }

      isConnecting = true;
      build();
      async(() -> {
        try {
          StorageTypes.mySQLStorageProvider.connect();
          isConnecting = false;
          Debugger.debug("MySQL", "Connected");
          build();
        } catch (final Throwable throwable) {
          animateTitle("&c" + CAN_T_CONNECT);
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
