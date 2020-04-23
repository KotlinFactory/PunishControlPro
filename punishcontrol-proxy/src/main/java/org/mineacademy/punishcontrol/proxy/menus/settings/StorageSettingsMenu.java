package org.mineacademy.punishcontrol.proxy.menus.settings;


import javax.inject.Inject;
import lombok.NonNull;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.mineacademy.punishcontrol.core.storage.MySQLStorageProvider;
import org.mineacademy.punishcontrol.proxy.DaggerProxyComponent;
import org.mineacademy.punishcontrol.proxy.menus.browsers.SettingsBrowser;
import org.mineacademy.punishcontrol.proxy.menus.setting.AbstractSettingsMenu;

public final class StorageSettingsMenu extends AbstractSettingsMenu {

  private final MySQLStorageProvider mySQLStorageProvider;

  @Inject
  public StorageSettingsMenu(
      @NonNull final MySQLStorageProvider mySQLStorageProvider,
      @NonNull final SettingsBrowser settingsBrowser) {
    super(settingsBrowser);
    this.mySQLStorageProvider = mySQLStorageProvider;
  }

  public static void showTo(@NonNull final ProxiedPlayer player) {
    DaggerProxyComponent.create().storageSettingsMenu().displayTo(player);
  }
}
