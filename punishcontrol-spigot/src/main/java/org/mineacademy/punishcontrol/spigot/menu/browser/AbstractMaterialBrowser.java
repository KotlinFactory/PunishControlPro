package org.mineacademy.punishcontrol.spigot.menu.browser;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.val;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.MinecraftVersion;
import org.mineacademy.fo.MinecraftVersion.V;
import org.mineacademy.fo.debug.Debugger;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.punishcontrol.core.Searcher;
import org.mineacademy.punishcontrol.core.injector.annotations.Localizable;
import org.mineacademy.punishcontrol.spigot.util.ItemStacks;
import org.mineacademy.punishcontrol.spigot.util.Schedulable;

@Localizable
public abstract class AbstractMaterialBrowser
    extends AbstractSearchableBrowser<CompMaterial>
    implements Schedulable {

  // ----------------------------------------------------------------------------------------------------
  // Localization
  // ----------------------------------------------------------------------------------------------------

  @Localizable("Menu.Proxy.Material-Browser.MenuInformation")
  private static String[] MENU_INFORMATION = {
      "",
      "&7Menu to select materials",
      "&7that fit your wishes best",
      "&7more items will be added soon"
  };
  @Localizable("Parts.Materials")
  private static String MATERIALS = "Materials";

  // ----------------------------------------------------------------------------------------------------
  // Static fields
  // ----------------------------------------------------------------------------------------------------

  private static List<CompMaterial> items = Arrays
      .stream(CompMaterial.values())
      .filter(Objects::nonNull)
      .filter(AbstractMaterialBrowser::isItem)
      .collect(Collectors.toList());

  private static boolean isItem(final CompMaterial material) {
    try {
      if (MinecraftVersion.newerThan(V.v1_13))
        return material.getMaterial().isItem();
      return ItemStacks.isItem(material) && material.getMaterial() != Material.AIR;
    } catch (Throwable throwable) {
      Debugger.debug("items", "Exception while checking whether an item is obtainable");
      return false;
    }
  }

  // ----------------------------------------------------------------------------------------------------
  // Fields and constructors
  // ----------------------------------------------------------------------------------------------------

  protected AbstractMaterialBrowser(@NonNull final Menu parent) {
    this(parent, items);
  }

  protected AbstractMaterialBrowser(
      @NonNull final Menu parent,
      @NonNull final Collection<CompMaterial> items) {
    super(parent, items);
    setTitle("&8" + MATERIALS);
  }

  @Override
  protected ItemStack convertToItemStack(CompMaterial item) {
    val creator = ItemCreator.of(item);

    creator.name("&3" + item.name());

    creator.lores(Arrays.asList(lore(item)));

    return creator.build().make();
  }

  protected String[] lore(final CompMaterial material) {
    return new String[0];
  }

  @Override
  protected String[] getInfo() {
    return MENU_INFORMATION;
  }

  @Override
  public final void redisplay(Collection<CompMaterial> content) {
    async(() -> new AbstractMaterialBrowser(this, content) {
      @Override
      protected void onPageClick(Player player, CompMaterial item, ClickType click) {
        AbstractMaterialBrowser.this.onPageClick(player, item, click);
      }
    }.displayTo(getViewer()));
  }

  @Override
  public final Collection<CompMaterial> searchByPartialString(String partial) {
    return Searcher.search(
        partial,
        items
            .stream()
            .map(Enum::toString)
            .collect(Collectors.toList()))
        .stream()
        .map(string -> Material.valueOf(string.result()))
        .map(CompMaterial::fromMaterial)
        .collect(Collectors.toList());
  }

  @Override
  protected String[] compassLore() {
    return super.compassLore();
  }
}
