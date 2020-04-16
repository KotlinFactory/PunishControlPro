package org.mineacademy.punishcontrol.spigot.menus.browsers;

import java.util.Arrays;
import javax.inject.Inject;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.punishcontrol.spigot.DaggerSpigotComponent;
import org.mineacademy.punishcontrol.spigot.menu.browser.AbstractBrowser;
import org.mineacademy.punishcontrol.spigot.menus.MainMenu;
import org.mineacademy.punishcontrol.spigot.menus.settings.SettingTypes;

public final class SettingsBrowser extends AbstractBrowser<SettingTypes> {

  public static void showTo(@NonNull final Player player) {
    DaggerSpigotComponent.create().settingsBrowser().displayTo(player);
  }

  @Inject
  public SettingsBrowser(final MainMenu mainMenu) {
    super(mainMenu, Arrays.asList(SettingTypes.values()));
    setTitle("&8Choose Settings");
  }

  @Override
  protected ItemStack convertToItemStack(final SettingTypes item) {
    return item.itemCreator().make();
  }

  @Override
  protected void onPageClick(
      final Player player,
      final SettingTypes item,
      final ClickType click) {
      item.showMenu(getViewer());
  }

  @Override
  protected String[] getInfo() {
    return new String[]{"&7Menu to choose", "&7a setting"};
  }
}
