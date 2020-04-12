package org.mineacademy.punishcontrol.spigot.menus;

import javax.inject.Inject;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.punishcontrol.spigot.DaggerSpigotComponent;
import org.mineacademy.punishcontrol.spigot.Scheduler;

public final class DurationChooserMenu extends Menu {

  @Inject
  public DurationChooserMenu() {

  }

  public static void showTo(@NonNull final Player player) {
    Scheduler.runAsync(() -> DaggerSpigotComponent.create().durationChooserMenu().displayTo(player));
  }

  @Override
  protected String[] getInfo() {
    return new String[0];
  }
}
