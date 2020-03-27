package org.mineacademy.punishcontrol.spigot.menus;

import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.Arrays;

public final class MenuMain extends Menu {

  // TODO See boss for design.
  private MenuMain() {
    setSize(9 * 5);
    setTitle("§3Punish§bControl");
  }

  public static void showTo(@NonNull final Player player) {
    new MenuMain().displayTo(player);
  }

  // You can't change anything here by clicking so we only need this gui once:)
  // And createNewInstance is already used:I
  public static MenuMain create() {
    return new MenuMain();
  }

  @Override
  public ItemStack getItemAt(final int slot) {

    if (Arrays.asList(0, 9, 18, 27, 36, 8, 17, 26, 35, 44, 1, 7, 37, 43).contains(slot)) {
      return ItemCreator.of(CompMaterial.LIGHT_BLUE_STAINED_GLASS_PANE, "").build().make();
    }

    return super.getItemAt(slot);
  }

  @Override
  protected void onButtonClick(
      final Player player,
      final int slot,
      final InventoryAction action,
      final ClickType click,
      final Button button) {
    super.onButtonClick(player, slot, action, click, button);
  }

  @Override
  protected String[] getInfo() {
    return null;
  }
}
