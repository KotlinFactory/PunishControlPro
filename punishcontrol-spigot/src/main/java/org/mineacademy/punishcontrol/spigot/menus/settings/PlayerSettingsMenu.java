package org.mineacademy.punishcontrol.spigot.menus.settings;

import javax.inject.Inject;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.mineacademy.punishcontrol.spigot.DaggerSpigotComponent;
import org.mineacademy.punishcontrol.spigot.menus.setting.AbstractSettingsMenu;

public final class PlayerSettingsMenu extends AbstractSettingsMenu {
  public static void showTo(@NonNull final Player player){
    DaggerSpigotComponent.create().playerSettingsMenu().displayTo(player);
  }

  @Inject
  public PlayerSettingsMenu(){

  }

}
