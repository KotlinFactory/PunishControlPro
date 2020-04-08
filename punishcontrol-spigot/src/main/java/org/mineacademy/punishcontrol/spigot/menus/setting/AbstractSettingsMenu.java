package org.mineacademy.punishcontrol.spigot.menus.setting;

import org.mineacademy.fo.menu.Menu;

public abstract class AbstractSettingsMenu extends Menu {

  @Override
  protected final String[] getInfo() {
    return new String[]{"Menu to change settings."};
  }
}
