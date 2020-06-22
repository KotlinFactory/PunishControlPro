package org.mineacademy.punishcontrol.proxy.menus.setting;

import org.mineacademy.burst.menu.AbstractMenu;
import org.mineacademy.punishcontrol.proxy.menus.browsers.SettingsBrowser;

public abstract class AbstractSettingsMenu extends AbstractMenu {

  private static final String[] MENU_INFORMATION = {"Menu to change settings."};

  protected AbstractSettingsMenu(final SettingsBrowser settingsBrowser) {
    this(settingsBrowser, 9 * 4);
  }

  protected AbstractSettingsMenu(final SettingsBrowser settingsBrowser, final int size) {
    super("Settings", settingsBrowser, size);
  }

  @Override
  protected final String[] getInfo() {
    return MENU_INFORMATION;
  }
}
