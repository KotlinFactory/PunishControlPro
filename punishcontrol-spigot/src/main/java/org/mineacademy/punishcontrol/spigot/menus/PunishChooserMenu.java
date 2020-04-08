package org.mineacademy.punishcontrol.spigot.menus;

import javax.inject.Inject;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.punishcontrol.spigot.DaggerSpigotComponent;

public final class PunishChooserMenu extends Menu {

  @Inject
  public PunishChooserMenu() {
    super();
    setSize(9 * 4);
    setTitle("§3Choose an action");
  }

  public static void showTo(@NonNull final Player player) {
    DaggerSpigotComponent.create().punishChooserMenu().displayTo(player);
  }

  @Override
  protected String[] getInfo() {
    return new String[0];
  }
}
