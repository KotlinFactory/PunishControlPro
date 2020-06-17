package org.mineacademy.punishcontrol.spigot.menus.browsers;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.inject.Inject;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NonNls;
import org.mineacademy.fo.debug.Debugger;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.punishcontrol.core.Searcher;
import org.mineacademy.punishcontrol.core.setting.CustomItem;
import org.mineacademy.punishcontrol.core.settings.ItemSettings;
import org.mineacademy.punishcontrol.spigot.DaggerSpigotComponent;
import org.mineacademy.punishcontrol.spigot.menu.browser.AbstractMaterialBrowser;
import org.mineacademy.punishcontrol.spigot.menu.browser.AbstractSearchableBrowser;

public final class CustomItemBrowser extends AbstractSearchableBrowser<CustomItem> {

  @NonNls
  private static final String CUSTOM_ITEMS = "Custom items";
  private final ItemSettings itemSettings;

  public static void showTo(@NonNull final Player player) {
    DaggerSpigotComponent.create().customItemBrowser().displayTo(player);
  }

  @Inject
  public CustomItemBrowser(SettingsBrowser parent, ItemSettings itemSettings) {
    this(parent, itemSettings, ItemSettings.items());
  }

  public CustomItemBrowser(
      SettingsBrowser parent,
      ItemSettings itemSettings,
      Collection<CustomItem> content) {
    super(parent, content);
    this.itemSettings = itemSettings;
    setTitle("&8" + CUSTOM_ITEMS);
  }


  public void redisplay() {
    redisplay(ItemSettings.items());
  }

  @Override
  public void redisplay(Collection<CustomItem> content) {
    new CustomItemBrowser((SettingsBrowser) getParent(), itemSettings, content)
        .displayTo(getViewer());
  }

  @Override
  public Collection<CustomItem> searchByPartialString(String partial) {
    return Searcher.search(
        partial,
        ItemSettings.items()
            .stream()
            .map(CustomItem::name)
            .collect(Collectors.toList()))
        .stream()
        .map(result -> itemSettings.find(result.result()).orElse(null))
        .filter(Objects::isNull)
        .collect(Collectors.toList());
  }

  @Override
  protected ItemStack convertToItemStack(CustomItem item) {

    CompMaterial compMaterial;

    if (item.itemType().isEmpty()) {
      compMaterial = CompMaterial.STONE;
    } else {
      try {
        compMaterial = CompMaterial.fromString(item.itemType());
      } catch (IllegalArgumentException e) {
        Debugger.debug(
            "Items",
            "Can't parse itemtype of item: " + item.name(),
            "defaulting to stone");
        compMaterial = CompMaterial.STONE;
      }
    }

    return ItemCreator
        .of(
            compMaterial,
            "&8" + item.name(),
            item.description())
        .lore("")
        .lore("&6Click to edit")
        .build()
        .make();
  }

  @Override
  protected void onPageClick(Player player, CustomItem customItem, ClickType click) {
    new AbstractMaterialBrowser(this) {
      @Override
      protected void onPageClick(Player player, CompMaterial item, ClickType click) {
        itemSettings.setType(customItem, item.toString());
        CustomItemBrowser.this.redisplay();
      }
    }.displayTo(player);
  }

  @Override
  protected String[] getInfo() {
    return new String[]{
        ""
    };
  }
}
