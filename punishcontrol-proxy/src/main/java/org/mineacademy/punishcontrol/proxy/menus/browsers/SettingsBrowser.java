package org.mineacademy.punishcontrol.proxy.menus.browsers;

import de.exceptionflug.mccommons.inventories.api.ClickType;
import de.exceptionflug.protocolize.items.ItemStack;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.NonNull;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.mineacademy.burst.menu.AbstractSearchableBrowser;
import org.mineacademy.burst.menu.BurstMenu;
import org.mineacademy.punishcontrol.core.Searcher;
import org.mineacademy.punishcontrol.proxy.DaggerProxyComponent;
import org.mineacademy.punishcontrol.proxy.menus.MainMenu;
import org.mineacademy.punishcontrol.proxy.menus.settings.SettingTypes;

public final class SettingsBrowser extends AbstractSearchableBrowser<SettingTypes> {

  public static void showTo(@NonNull final ProxiedPlayer player) {
    DaggerProxyComponent.create().settingsBrowser().displayTo(player);
  }

  @Inject
  public SettingsBrowser(
      @NonNull final MainMenu mainMenu,
      @NonNull @Named("settings") final Collection<SettingTypes> settings) {
    super("SettingsBrowser", mainMenu, settings);
    setTitle("&8Choose Settings");
  }

  public SettingsBrowser(
      @NonNull final BurstMenu mainMenu,
      @NonNull @Named("settings") final Collection<SettingTypes> settings) {
    super("SettingsBrowser", mainMenu, settings);
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
    return item.itemCreator().build();
  }

  @Override
  protected String[] getInfo() {
    return new String[]{"&7Menu to choose", "&7a setting"};
  }

  @Override
  public void reDisplay() {
    showTo(getPlayer());
  }

  @Override
  public void redisplay(Collection<SettingTypes> content) {
    new SettingsBrowser(this, content).displayTo(getPlayer());
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

  @Override
  public void updateInventory() {
    super.updateInventory();
  }
}
