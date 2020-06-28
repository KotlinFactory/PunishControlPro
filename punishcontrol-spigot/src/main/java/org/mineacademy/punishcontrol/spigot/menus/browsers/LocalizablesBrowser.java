package org.mineacademy.punishcontrol.spigot.menus.browsers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.punishcontrol.core.Searcher;
import org.mineacademy.punishcontrol.core.localization.Localizable;
import org.mineacademy.punishcontrol.core.localization.Localizables;
import org.mineacademy.punishcontrol.spigot.DaggerSpigotComponent;
import org.mineacademy.punishcontrol.spigot.Scheduler;
import org.mineacademy.punishcontrol.spigot.menu.browser.AbstractSearchableBrowser;
import org.mineacademy.punishcontrol.spigot.menus.LocalizableEditorMenu;

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

  public static void showTo(@NonNull final Player player) {
    Scheduler.runAsync(
        () -> DaggerSpigotComponent.create().localizableBrowser().displayTo(player)
                      );
  }

  @Inject
  public LocalizablesBrowser(
      @NonNull final LanguageBrowser parent,
      @NonNull @Named("localizables") final Collection<Localizable> content) {
    super(parent, content);
    this.browser = parent;
    setTitle("&8" + MESSAGES);
  }

  @Override
  public void redisplay(final Collection<Localizable> content) {
    if (getViewer() == null)
      return;

    async(() -> new LocalizablesBrowser(browser, content).displayTo(getViewer()));
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
  protected void onPageClick(
      Player player, Localizable localizable, ClickType click) {
    LocalizableEditorMenu.showTo(getViewer(), localizable);
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

    return ItemCreator
        .of(CompMaterial.PAPER)
        .name("&6" + localizable.path())
        .lores(lore)
        .build()
        .make();
  }

  @Override
  protected String[] getInfo() {
    return MENU_INFORMATION;
  }
}
