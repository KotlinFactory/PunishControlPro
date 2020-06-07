package org.mineacademy.punishcontrol.spigot.menu;

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
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.punishcontrol.core.Searcher;
import org.mineacademy.punishcontrol.spigot.menu.browser.AbstractSearchableBrowser;
import org.mineacademy.punishcontrol.spigot.util.Schedulable;

public abstract class AbstractMaterialBrowser
    extends AbstractSearchableBrowser<CompMaterial>
    implements Schedulable {

  private static List<CompMaterial> items = Arrays
      .stream(Material.values())
      .filter(AbstractMaterialBrowser::isItem)
      .map(CompMaterial::fromMaterial)
      .collect(Collectors.toList())
      .stream()
      .filter(Objects::nonNull)
      .collect(Collectors.toList());

  private static boolean isItem(final Material material) {
    if (MinecraftVersion.newerThan(V.v1_13)) {
      return material.isItem();
    }
    return material.getMaxStackSize() == 64;
  }

  protected AbstractMaterialBrowser(@NonNull final Menu parent) {
    this(parent, items);
  }

  protected AbstractMaterialBrowser(
      @NonNull final Menu parent,
      @NonNull final Collection<CompMaterial> items) {
    super(parent, items);
    setTitle("&8Materials");
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
    return new String[]{
        "",
        "&7Menu to select materials",
        "&7that fit your wishes best",
        "&7more items will be added soon"
    };
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
