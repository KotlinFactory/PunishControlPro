package org.mineacademy.punishcontrol.proxy.menus.browsers;

import de.exceptionflug.mccommons.inventories.api.ClickType;
import de.exceptionflug.protocolize.items.ItemStack;
import java.util.Arrays;
import javax.inject.Inject;
import lombok.NonNull;
import org.mineacademy.bfo.settings.SimpleLocalization;
import org.mineacademy.burst.menu.AbstractBrowser;
import org.mineacademy.punishcontrol.proxy.DaggerProxyComponent;
import org.mineacademy.punishcontrol.proxy.menus.MainMenu;
import org.mineacademy.punishcontrol.proxy.menus.settings.SettingTypes;

public final class SettingsBrowser extends AbstractBrowser<SettingTypes> {

  public static void showTo(@NonNull final SimpleLocalization.Player player) {
    DaggerProxyComponent.create().settingsBrowser().displayTo(player);
  }

  @Inject
  public SettingsBrowser(final MainMenu mainMenu) {
    super("SettingsBrowser",  mainMenu, Arrays.asList(SettingTypes.values()));
    setTitle("&8Choose Settings");
  }

  @Override
  protected void onClick(final ClickType clickType, final SettingTypes item) {
    if (!item.hasAccess(player)) {
      animateTitle("&cInsufficient permission");
      return;
    }
    item.showMenu(getPlayer());
  }

  @Override
  protected ItemStack convertToItemStack(final SettingTypes item) {
    return item.itemCreator();
  }


  @Override
  protected String[] getInfo() {
    return new String[]{"&7Menu to choose", "&7a setting"};
  }
}
