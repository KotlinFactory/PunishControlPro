package org.mineacademy.punishcontrol.spigot.menus.setting;

import org.mineacademy.fo.menu.Menu;
import org.mineacademy.punishcontrol.spigot.menus.browsers.SettingsBrowser;

public abstract class AbstractSettingsMenu extends Menu {

  private static final String[] MENU_INFORMATION = {"Menu to change settings."};

  protected AbstractSettingsMenu(final SettingsBrowser settingsBrowser) {
    super(settingsBrowser);
  }

  @Override
  protected final String[] getInfo() {
    return MENU_INFORMATION;
  }
}
