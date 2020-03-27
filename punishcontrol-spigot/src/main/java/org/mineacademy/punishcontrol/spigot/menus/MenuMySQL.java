package org.mineacademy.punishcontrol.spigot.menus;

import lombok.NonNull;
import org.bukkit.entity.Player;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.punishcontrol.core.storage.MySQLStorageProvider;
import org.mineacademy.punishcontrol.spigot.DaggerSpigotComponent;

import javax.inject.Inject;

public final class MenuMySQL extends Menu {

  private final MySQLStorageProvider mySQLStorageProvider;

  @Inject
  public MenuMySQL(@NonNull final MySQLStorageProvider mySQLStorageProvider) {
    this.mySQLStorageProvider = mySQLStorageProvider;
  }

  public static void showTo(@NonNull final Player player) {
    DaggerSpigotComponent.create().mysqlModule().displayTo(player);

    //		return MySQLStorageProvider.getInstance (Providers.workingDirectoryProvider());
  }

  @Override
  protected String[] getInfo() {
    return new String[0];
  }
}
