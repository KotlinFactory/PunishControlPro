package org.mineacademy.punishcontrol.spigot.menus;

import javax.inject.Inject;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.punishcontrol.spigot.DaggerSpigotComponent;

public final class ChooseActionMenu extends Menu {

  @Inject
  public ChooseActionMenu() {
    super();
    setSize(9 * 4);
    setTitle("§3Choose an action");
  }

  /*
  TODO:
    - Punish
    - Punishes
    - As console view
    - Kick

   */

  public static void showTo(@NonNull final Player player) {
    DaggerSpigotComponent.create().punishChooserMenu().displayTo(player);
  }

  @Override
  protected String[] getInfo() {
    return new String[]{"&7Menu to select an", "&7Action for players"};
  }
}
