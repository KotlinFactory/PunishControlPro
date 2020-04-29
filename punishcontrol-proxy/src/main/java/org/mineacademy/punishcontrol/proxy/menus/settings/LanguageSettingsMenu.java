package org.mineacademy.punishcontrol.proxy.menus.settings;

import javax.inject.Inject;
import lombok.NonNull;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.mineacademy.punishcontrol.proxy.DaggerProxyComponent;
import org.mineacademy.punishcontrol.proxy.menus.browsers.SettingsBrowser;
import org.mineacademy.punishcontrol.proxy.menus.setting.AbstractSettingsMenu;

public final class LanguageSettingsMenu extends AbstractSettingsMenu {

  public static void showTo(@NonNull final ProxiedPlayer player) {
    DaggerProxyComponent.create().languageSettingsMenu().displayTo(player);
  }

  @Inject
  public LanguageSettingsMenu(final SettingsBrowser settingsBrowser) {
    super(settingsBrowser);
  }

  @Override
  public void reDisplay() {
    showTo(getPlayer());
  }
}
