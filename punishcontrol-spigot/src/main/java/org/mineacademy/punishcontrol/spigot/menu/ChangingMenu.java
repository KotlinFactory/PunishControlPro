package org.mineacademy.punishcontrol.spigot.menu;

import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.model.InventoryDrawer;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.punishcontrol.spigot.menu.buttons.ChangingButton;
import org.mineacademy.punishcontrol.spigot.util.Schedulable;

/**
 * A menu that has items that will change randomly init.
 * <p>
 * Good performance because it will be build on a different thead:)
 */
public class ChangingMenu extends Menu implements Schedulable {

  private final List<ChangingButton> buttons;
  private boolean cancelled;

  protected ChangingMenu(final List<ChangingButton> buttons) {
    this(null, buttons);
  }

  @Override
  protected final void onDisplay(final InventoryDrawer drawer) {
    cancelled = false;
    start();
  }

  protected ChangingMenu(
      final Menu parent,
      final List<ChangingButton> buttons) {
    super(parent);
    this.buttons = buttons;
  }

  private void start() {
    new BukkitRunnable() {
      @Override
      public void run() {
        if (getViewer() == null) {
          cancel();
          return;
        }

        if (cancelled) {
          cancel();
          return;
        }

        for (final ChangingButton button : buttons) {
          getViewer().getOpenInventory().setItem(button.slot(), button.nextItem());
        }

        getViewer().updateInventory();
      }
    }.runTaskTimerAsynchronously(SimplePlugin.getInstance(), updateDelay(),
        updateDelay());
  }

  // ----------------------------------------------------------------------------------------------------
  // Methods to override / implement
  // ----------------------------------------------------------------------------------------------------

  /**
   * Tick delay between the changing of the Buttons
   */
  protected int updateDelay() {
    return 30;
  }

  @Nullable
  protected ItemStack findItem(final int slot) {
    return null;
  }

  // ----------------------------------------------------------------------------------------------------
  // Overridden methods form Menu
  // ----------------------------------------------------------------------------------------------------

  @Override
  protected final void onMenuClose(final Player player, final Inventory inventory) {
    cancelled = true;
  }

  @Override
  public final ItemStack getItemAt(final int slot) {
    //Will return null if nothing was found
    for (final ChangingButton button : buttons) {
      if (slot == button.slot()) {
        return button.nextItem();
      }
    }
    return findItem(slot);
  }
}
