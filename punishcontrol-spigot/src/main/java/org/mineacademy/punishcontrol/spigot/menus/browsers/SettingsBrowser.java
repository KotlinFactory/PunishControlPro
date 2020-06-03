package org.mineacademy.punishcontrol.spigot.menus.browsers;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.punishcontrol.core.Searcher;
import org.mineacademy.punishcontrol.spigot.DaggerSpigotComponent;
import org.mineacademy.punishcontrol.spigot.menu.browser.AbstractSearchableBrowser;
import org.mineacademy.punishcontrol.spigot.menus.MainMenu;
import org.mineacademy.punishcontrol.spigot.menus.settings.SettingTypes;

public final class SettingsBrowser extends AbstractSearchableBrowser<SettingTypes> {

  public static void showTo(@NonNull final Player player) {
    DaggerSpigotComponent.create().settingsBrowser().displayTo(player, true);
  }

  @Inject
  public SettingsBrowser(
      @NonNull final MainMenu mainMenu,
      @NonNull @Named("settings") final Collection<SettingTypes> settings) {
    super(mainMenu, settings);
    setTitle("&8Choose Settings");
  }

  public SettingsBrowser(
      @NonNull final Menu mainMenu,
      @NonNull @Named("settings") final Collection<SettingTypes> settings) {
    super(mainMenu, settings);
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
    if (!item.hasAccess(player)) {
      animateTitle("&cInsufficient permission");
      return;
    }
    item.showMenu(getViewer());
  }

  @Override
  protected String[] getInfo() {
    return new String[]{"&7Menu to choose", "&7a setting"};
  }

  @Override
  public void redisplay(Collection<SettingTypes> content) {
    new SettingsBrowser(this, content).displayTo(getViewer());
  }

  @Override
  public Collection<SettingTypes> searchByPartialString(String partial) {
    return Searcher.search(
        partial,
        Arrays
            .stream(SettingTypes.values())
            .map(Enum::toString)
            .collect(Collectors.toList()))
        .stream()
        .map(string -> SettingTypes.valueOf(string.result()))
        .collect(Collectors.toList());
  }
}
