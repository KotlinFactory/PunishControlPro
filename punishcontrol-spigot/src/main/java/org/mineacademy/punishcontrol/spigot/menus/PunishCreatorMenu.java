package org.mineacademy.punishcontrol.spigot.menus;

import javax.inject.Inject;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.punishcontrol.spigot.DaggerSpigotComponent;

public final class PunishCreatorMenu extends Menu {

  public static void showTo(@NonNull final Player player) {
    DaggerSpigotComponent.create().punishCreatorMenu().displayTo(player);
  }

  @Inject
  public PunishCreatorMenu(final MainMenu mainMenu) {
    super(mainMenu);
  }

  @Override
  protected String[] getInfo() {
    return new String[0];
  }
}
    