package org.mineacademy.punishcontrol.proxy.menus.browsers;

import de.exceptionflug.mccommons.inventories.api.ClickType;
import de.exceptionflug.protocolize.items.ItemStack;
import de.exceptionflug.protocolize.items.ItemType;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.inject.Inject;
import lombok.NonNull;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.mineacademy.bfo.debug.Debugger;
import org.mineacademy.burst.item.Item;
import org.mineacademy.burst.menu.AbstractSearchableBrowser;
import org.mineacademy.punishcontrol.core.Searcher;
import org.mineacademy.punishcontrol.core.setting.CustomItem;
import org.mineacademy.punishcontrol.core.settings.ItemSettings;
import org.mineacademy.punishcontrol.proxy.DaggerProxyComponent;
import org.mineacademy.punishcontrol.proxy.menu.browser.AbstractMaterialBrowser;

public final class CustomItemBrowser extends AbstractSearchableBrowser<CustomItem> {

  private final ItemSettings itemSettings;

  public static void showTo(@NonNull final ProxiedPlayer player) {
    DaggerProxyComponent.create().customItemBrowser().displayTo(player);
  }

  @Inject
  public CustomItemBrowser(SettingsBrowser parent, ItemSettings itemSettings) {
    this(parent, itemSettings, ItemSettings.items());
  }

  public CustomItemBrowser(
      SettingsBrowser parent,
      ItemSettings itemSettings,
      Collection<CustomItem> content) {
    super("ItemBrowser", parent, content);
    this.itemSettings = itemSettings;
    setTitle("&8Custom items");
  }


  public void redisplay() {
    redisplay(ItemSettings.items());
  }

  @Override
  public void redisplay(Collection<CustomItem> content) {
    new CustomItemBrowser((SettingsBrowser) getParent(), itemSettings, content)
        .displayTo(getPlayer());
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

    ItemType itemType;

    if (item.itemType() == null || item.itemType().isEmpty()) {
      itemType = ItemType.STONE;
    } else {
      try {
        itemType = ItemType.valueOf(item.itemType());
      } catch (IllegalArgumentException e) {
        Debugger.debug(
            "Items",
            "Can't parse itemtype of item: " + item.name(),
            "defaulting to stone");
        itemType = ItemType.STONE;
      }
    }

    return Item
        .of(
            itemType,
            "&8" + item.name(),
            item.description())
        .lore("")
        .lore("&6Click to edit")
        .build();
  }

  @Override
  protected void onClick(ClickType clickType, CustomItem customItem) {
    new AbstractMaterialBrowser(this) {
      @Override
      protected void onClick(ClickType clickType, ItemType itemType) {
        itemSettings.setType(customItem, itemType.toString());
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

  @Override
  public void reDisplay() {
    showTo(getPlayer());
  }
}
