package org.mineacademy.punishcontrol.spigot.menu.browser;

import java.util.Arrays;
import java.util.Collection;
import lombok.val;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.punishcontrol.spigot.conversations.SearchConversation;

public abstract class AbstractSearchableBrowser<T> extends AbstractBrowser<T> {

  private final Button searchButton;

  protected AbstractSearchableBrowser(
      Menu parent,
      Collection<T> content) {
    super(parent, content);

    searchButton = new Button() {
      @Override
      public void onClickedInMenu(Player player, Menu menu, ClickType click) {
        SearchConversation.create(AbstractSearchableBrowser.this).start(player);
      }

      @Override
      public ItemStack getItem() {
        return ItemCreator
            .of(CompMaterial.COMPASS)
            .name("&6Search")
            .lores(Arrays.asList(compassLore()))
            .build()
            .makeMenuTool();
      }
    };
  }

  public abstract void redisplay(final Collection<T> content);

  public abstract Collection<T> searchByPartialString(final String partial);

  /**
   * Defines the lore our compass should have
   */
  protected String[] compassLore() {
    return new String[]{
        "&7Search for a value",
    };
  }

  @Override
  public final ItemStack getItemAt(int slot) {

    val superReturnValue = super.getItemAt(slot);

    if (superReturnValue != null) {
      return superReturnValue;
    }

    if (slot == getSize() - 5) {
      return searchButton.getItem();
    }

    return null;
  }
}
