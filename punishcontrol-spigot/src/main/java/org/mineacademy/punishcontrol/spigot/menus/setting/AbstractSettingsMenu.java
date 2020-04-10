package org.mineacademy.punishcontrol.spigot.menus.setting;

import org.mineacademy.fo.menu.Menu;
import org.mineacademy.punishcontrol.spigot.menus.browser.SettingsBrowser;

public abstract class AbstractSettingsMenu extends Menu {

  protected AbstractSettingsMenu(final SettingsBrowser settingsBrowser) {
    super(settingsBrowser);
  }

  @Override
  protected final String[] getInfo() {
    return new String[]{"Menu to change settings."};
  }
}
