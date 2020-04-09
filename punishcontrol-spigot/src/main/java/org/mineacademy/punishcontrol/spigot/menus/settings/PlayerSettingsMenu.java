package org.mineacademy.punishcontrol.spigot.menus.settings;

import java.util.UUID;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.mineacademy.punishcontrol.spigot.DaggerSpigotComponent;
import org.mineacademy.punishcontrol.spigot.menus.setting.AbstractSettingsMenu;

public final class PlayerSettingsMenu extends AbstractSettingsMenu {

  private final UUID target;

  public static void showTo(@NonNull final Player player, @NonNull final UUID target){
    new PlayerSettingsMenu(target).displayTo(player);
  }

  private PlayerSettingsMenu(final UUID target) {
    super(DaggerSpigotComponent.create().menuMain());
    this.target = target;
  }
}
