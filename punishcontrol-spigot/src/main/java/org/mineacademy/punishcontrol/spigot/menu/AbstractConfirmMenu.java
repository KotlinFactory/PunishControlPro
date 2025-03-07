package org.mineacademy.punishcontrol.spigot.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.debug.Debugger;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.punishcontrol.core.injector.annotations.Localizable;
import org.mineacademy.punishcontrol.core.settings.ItemSettings;
import org.mineacademy.punishcontrol.spigot.util.Schedulable;

@Localizable
public abstract class AbstractConfirmMenu extends Menu implements Schedulable {

  @Localizable(value = "Parts.Confirm")
  private static String CONFIRM = "Confirm";
  @Localizable(value = "Parts.Break")
  private static String BREAKUP = "Breakup";

  @Localizable("Menu.Spigot.ConfirmMenu.ApplyAction.Lore")
  private static String[] MENU_INFORMATION = {"&7Apply action"};

  public AbstractConfirmMenu() {
    setSize(9);
    setTitle("&a" + CONFIRM);
  }

  public AbstractConfirmMenu(final Menu parent) {
    super(parent);
    setSize(9);
    setTitle("&8" + CONFIRM);
  }

  public AbstractConfirmMenu(
      final Menu parent,
      final boolean returnMakesNewInstance) {
    super(parent, returnMakesNewInstance);
    setSize(9);
    setTitle("&8" + CONFIRM);
  }

  public abstract void onConfirm();

  protected void showParent() {
    if (getParent() == null)
      return;
    if (getViewer() == null)
      return;
    async(() -> {
      getParent().displayTo(getViewer());
      getParent().restartMenu();
    });
  }

  @Override
  public final ItemStack getItemAt(final int slot) {
    if (slot == 0)
      return ItemCreator
          .of(
              ItemSettings.APPLY_ITEM.itemType(),
              "&a" + CONFIRM,
              "",
              "&7Click to confirm")
          .build()
          .makeMenuTool();

    if (slot == 8)
      ItemCreator
          .ofString(ItemSettings.BREAK_UP_ITEM.itemType())
          .name("&3" + BREAKUP);

    return null;
  }

  @Override
  protected void onMenuClick(
      final Player player,
      final int slot,
      final ItemStack clicked) {
    super.onMenuClick(player, slot, clicked);
    if (slot == 0) {
      try {
        onConfirm();
      } catch (final Throwable throwable) {
        Debugger.saveError(throwable, "Exception while confirming");
        Common.tell(
            player,
            "&cException while confirming",
            "&7See console & report this error with your error.log");
      }
      showParent();
    }

    if (slot == 8)
      showParent();
  }
}
