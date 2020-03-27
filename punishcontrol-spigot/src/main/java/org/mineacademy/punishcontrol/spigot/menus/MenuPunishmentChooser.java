package org.mineacademy.punishcontrol.spigot.menus;

import lombok.NonNull;
import org.bukkit.entity.Player;
import org.mineacademy.fo.menu.Menu;

public final class MenuPunishmentChooser extends Menu {

  private MenuPunishmentChooser() {
    super(MenuPlayerBrowser.of(true));
    setSize(9 * 4);
    setTitle("§3Choose an action");
  }

  public static void showTo(@NonNull final Player player) {
    create().displayTo(player);
  }

  public static MenuPunishmentChooser create() {
    return new MenuPunishmentChooser();
  }

  @Override
  protected String[] getInfo() {
    return new String[0];
  }
}
