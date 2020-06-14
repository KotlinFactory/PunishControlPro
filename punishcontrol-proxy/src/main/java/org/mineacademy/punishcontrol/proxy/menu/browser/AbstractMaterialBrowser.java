package org.mineacademy.punishcontrol.proxy.menu.browser;

import de.exceptionflug.mccommons.inventories.api.ClickType;
import de.exceptionflug.mccommons.inventories.proxy.utils.Schedulable;
import de.exceptionflug.protocolize.api.util.ProtocolVersions;
import de.exceptionflug.protocolize.items.ItemStack;
import de.exceptionflug.protocolize.items.ItemType;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.val;
import org.mineacademy.burst.item.Item;
import org.mineacademy.burst.menu.AbstractSearchableBrowser;
import org.mineacademy.burst.menu.BurstMenu;
import org.mineacademy.punishcontrol.core.Searcher;
import org.mineacademy.punishcontrol.core.injector.annotations.Localizable;
import org.mineacademy.punishcontrol.core.settings.Settings.Advanced;

@Localizable
public abstract class AbstractMaterialBrowser
    extends AbstractSearchableBrowser<ItemType>
    implements Schedulable {

  // ----------------------------------------------------------------------------------------------------
  // Localization
  // ----------------------------------------------------------------------------------------------------

  @Localizable("Menu.Proxy.Material-Browser.Information")
  private static String[] MENU_INFORMATION = {
      "",
      "&7Menu to select materials",
      "&7that fit your wishes best",
      "&7more items will be added soon"
  };
  @Localizable("Parts.Materials")
  private static String MATERIALS = "Materials";


  private static List<ItemType> items = Arrays
      .stream(ItemType.values())
      .filter(AbstractMaterialBrowser::canApply)
      .filter(mat -> mat != ItemType.AIR)
      .collect(Collectors.toList());

  protected AbstractMaterialBrowser(@NonNull final BurstMenu parent) {
    this(parent, items);
  }

  protected AbstractMaterialBrowser(
      @NonNull final BurstMenu parent,
      @NonNull final Collection<ItemType> items) {
    super("MaterialBrowser", parent, items);
    setTitle("&8" + MATERIALS);
  }

  /**
   * Checks whether an ItemType can actually be displayed on the server Depends on which
   * ProtocolVersion is Supported.
   *
   * @param type Type of the item
   */
  private static boolean canApply(@NonNull final ItemType type) {
    try {
      de.exceptionflug.mccommons.inventories.api.item.ItemType.valueOf(type.name());
      // Must be available in the latest & in the minimal supported minecraft version
      return type.getApplicableMapping(ProtocolVersions.MINECRAFT_LATEST) != null
          && type.getApplicableMapping(Advanced.MIN_PROTOCOL_VERSION_SUPPORTED) != null;
    } catch (final Throwable throwable) {
      return false;
    }
  }

  @Override
  protected ItemStack convertToItemStack(ItemType item) {
    val creator = Item.of(item);

    creator.name("&3" + item.name());

    creator.lore(Arrays.asList(lore(item)));

    return creator.build();
  }

  protected String[] lore(final ItemType material) {
    return new String[0];
  }

  @Override
  protected String[] getInfo() {
    return MENU_INFORMATION;
  }

  @Override
  public final void redisplay(@NonNull final Collection<ItemType> content) {
    async(() -> new AbstractMaterialBrowser(this, content) {
      @Override
      public void reDisplay() {
        AbstractMaterialBrowser.this.reDisplay();
      }

      @Override
      protected void onClick(ClickType clickType, ItemType itemType) {
        AbstractMaterialBrowser.this.onClick(clickType, itemType);
      }
    }.displayTo(getPlayer()));
  }

  @Override
  public final Collection<ItemType> searchByPartialString(String partial) {
    return Searcher.search(
        partial,
        items
            .stream()
            .map(Enum::toString)
            .collect(Collectors.toList()))
        .stream()
        .map(string -> ItemType.valueOf(string.result()))
        .collect(Collectors.toList());
  }

  @Override
  protected String[] compassLore() {
    return super.compassLore();
  }

  @Override
  public void reDisplay() {
    async(() -> new AbstractMaterialBrowser(this) {
      @Override
      public void reDisplay() {
        AbstractMaterialBrowser.this.reDisplay();
      }

      @Override
      protected void onClick(ClickType clickType, ItemType itemType) {
        AbstractMaterialBrowser.this.onClick(clickType, itemType);
      }
    }.displayTo(getPlayer()));
  }
}
