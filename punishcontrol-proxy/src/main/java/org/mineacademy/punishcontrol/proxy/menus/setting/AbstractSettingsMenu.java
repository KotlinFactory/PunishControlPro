package org.mineacademy.punishcontrol.proxy.menus.setting;


import org.mineacademy.burst.menu.Menu;
import org.mineacademy.punishcontrol.proxy.menus.browsers.SettingsBrowser;

public abstract class AbstractSettingsMenu extends Menu {

  protected AbstractSettingsMenu(final SettingsBrowser settingsBrowser) {
    super("Settings", settingsBrowser, 9*4);
  }

  @Override
  protected final String[] getInfo() {
    return new String[]{"Menu to change settings."};
  }
}
