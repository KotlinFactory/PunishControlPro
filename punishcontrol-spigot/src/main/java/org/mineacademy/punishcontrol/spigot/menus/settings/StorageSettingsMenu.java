package org.mineacademy.punishcontrol.spigot.menus.settings;

import javax.inject.Inject;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.mineacademy.punishcontrol.core.storage.MySQLStorageProvider;
import org.mineacademy.punishcontrol.spigot.DaggerSpigotComponent;
import org.mineacademy.punishcontrol.spigot.menus.browsers.SettingsBrowser;
import org.mineacademy.punishcontrol.spigot.menus.setting.AbstractSettingsMenu;

public final class StorageSettingsMenu extends AbstractSettingsMenu {

  private final MySQLStorageProvider mySQLStorageProvider;

  @Inject
  public StorageSettingsMenu(
      @NonNull final MySQLStorageProvider mySQLStorageProvider,
      @NonNull final SettingsBrowser settingsBrowser) {
    super(settingsBrowser);
    this.mySQLStorageProvider = mySQLStorageProvider;
  }

  public static void showTo(@NonNull final Player player) {
    DaggerSpigotComponent.create().mysqlModule().displayTo(player, true);
  }
}
