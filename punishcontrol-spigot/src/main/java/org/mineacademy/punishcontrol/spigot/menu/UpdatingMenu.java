package org.mineacademy.punishcontrol.spigot.menu;

import java.util.Arrays;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.model.InventoryDrawer;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.punishcontrol.spigot.menu.buttons.UpdatingButton;
import org.mineacademy.punishcontrol.spigot.util.Schedulable;

public class UpdatingMenu extends Menu implements Schedulable {

  private final List<UpdatingButton> buttons;
  private boolean cancelled;

  protected UpdatingMenu(final UpdatingButton... buttons) {
    this.buttons = Arrays.asList(buttons);
  }

  protected UpdatingMenu(final List<UpdatingButton> buttons) {
    this.buttons = buttons;
  }

  protected UpdatingMenu(final Menu parent, final List<UpdatingButton> buttons) {
    super(parent);

    this.buttons = buttons;
  }

  @Override
  protected final void onDisplay(final InventoryDrawer drawer) {
    cancelled = false;
    start();
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

        for (final UpdatingButton button : buttons) {
          getViewer().getOpenInventory().setItem(button.slot(), button.build());
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
    return 50;
  }

  @Nullable
  protected ItemStack findItem(final int slot) {
    return null;
  }

  // ----------------------------------------------------------------------------------------------------
  // Overridden methods form Menu
  // ----------------------------------------------------------------------------------------------------


  @Override
  protected final void onMenuClose(final Player player,
      final Inventory inventory) {
    cancelled = true;
  }

  @Override
  public final ItemStack getItemAt(final int slot) {
    for (final UpdatingButton button : buttons) {
      if (slot == button.slot()) {
        return button.build();
      }
    }
    return findItem(slot);
  }
}
