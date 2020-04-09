package org.mineacademy.punishcontrol.spigot.menus.setting;

import org.mineacademy.fo.menu.Menu;
import org.mineacademy.punishcontrol.spigot.menus.MainMenu;

public abstract class AbstractSettingsMenu extends Menu {

  protected AbstractSettingsMenu(final MainMenu mainMenu) {
    super(mainMenu);
  }

  @Override
  protected final String[] getInfo() {
    return new String[]{"Menu to change settings."};
  }
}
