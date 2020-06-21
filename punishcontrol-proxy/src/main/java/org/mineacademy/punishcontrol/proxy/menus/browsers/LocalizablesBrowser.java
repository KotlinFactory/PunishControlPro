package org.mineacademy.punishcontrol.proxy.menus.browsers;

import de.exceptionflug.mccommons.inventories.api.ClickType;
import de.exceptionflug.protocolize.items.ItemStack;
import de.exceptionflug.protocolize.items.ItemType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.NonNull;
import lombok.SneakyThrows;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.mineacademy.bfo.plugin.SimplePlugin;
import org.mineacademy.burst.item.Item;
import org.mineacademy.burst.menu.AbstractSearchableBrowser;
import org.mineacademy.burst.util.Scheduler;
import org.mineacademy.punishcontrol.core.Searcher;
import org.mineacademy.punishcontrol.core.localization.Localizable;
import org.mineacademy.punishcontrol.core.localization.Localizables;
import org.mineacademy.punishcontrol.proxy.DaggerProxyComponent;
import org.mineacademy.punishcontrol.proxy.menus.LocalizableEditorMenu;

@org.mineacademy.punishcontrol.core.injector.annotations.Localizable
public final class LocalizablesBrowser extends AbstractSearchableBrowser<Localizable> {

  @org.mineacademy.punishcontrol.core.injector.annotations.Localizable("Menu.Proxy.LocalizablesBrowser.Information")
  private static String[] MENU_INFORMATION = {
      "Menu to browse and alter",
      "localizable messages in " + SimplePlugin.getNamed()
  };
  @org.mineacademy.punishcontrol.core.injector.annotations.Localizable("Parts.Messages")
  private static String MESSAGES = "Messages";
  private final LanguageBrowser browser;

  public static void showTo(@NonNull final ProxiedPlayer player) {
    Scheduler.runAsync(
        () -> DaggerProxyComponent.create().localizableBrowser().displayTo(player)
    );
  }

  @Inject
  public LocalizablesBrowser(
      @NonNull final LanguageBrowser parent,
      @NonNull @Named("localizables") final Collection<Localizable> content) {
    super("LocalizableBrowser", parent, content);
    this.browser = parent;
    setTitle("&8" + MESSAGES);
  }

  @Override
  public void redisplay(final Collection<Localizable> content) {
    if (getPlayer() == null)
      return;

    async(() -> new LocalizablesBrowser(browser, content).displayTo(getPlayer()));
  }

  @Override
  public Collection<Localizable> searchByPartialString(final String partial) {
    return Searcher.search(
        partial,
        Localizables
            .localizables()
            .stream()
            .map(Localizable::path)
            .collect(Collectors.toList()))
        .stream()
        .map(result -> Localizables.find(result.result()))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toList());
  }

  @Override
  protected void onClick(final ClickType clickType, final Localizable localizable) {
    LocalizableEditorMenu.showTo(player, localizable);
  }

  @SneakyThrows
  @Override
  protected ItemStack convertToItemStack(final Localizable localizable) {
    final List<String> lore = new ArrayList<>();

    lore.add(" ");
    lore.add("&3Value: ");

    for (String value : localizable.value())
      lore.add("&3- &8" + value.replace("&", "§"));

    lore.add(" ");
    lore.add("&6Click to edit");

    return Item
        .of(ItemType.PAPER)
        .name("&6" + localizable.path())
        .lore(lore)
        .build();
  }

  @Override
  protected String[] getInfo() {
    return MENU_INFORMATION;
  }

  @Override
  public void reDisplay() {
    showTo(getPlayer());
  }
}
