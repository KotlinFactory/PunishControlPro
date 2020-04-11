package org.mineacademy.punishcontrol.spigot.menu;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.punishcontrol.spigot.menu.buttons.ChangingButton;
import org.mineacademy.punishcontrol.spigot.util.Schedulable;

/**
 * A menu that has items that will change randomly init.
 * <p>
 * Good performance because it will be build on a different thead:)
 */
public abstract class AbstractChangingMenu extends Menu implements Schedulable {

  private final Map<Integer, ItemStack> changingButtonsMap = new ConcurrentHashMap<>();

  public AbstractChangingMenu() {
  }

  public AbstractChangingMenu(final Menu parent) {
    super(parent);
  }

  public AbstractChangingMenu(
      final Menu parent,
      final boolean returnMakesNewInstance) {
    super(parent, returnMakesNewInstance);
    for (final ChangingButton button : buttons()) {
      changingButtonsMap
          .put(button.slot(), button.creators().get(0).build().makeMenuTool());
    }

    start();
  }

  private void start() {
    repeatAsync(() -> {
      for (final ChangingButton button : buttons()) {
        changingButtonsMap.put(button.slot(), button.nextItem());
      }
      redraw();
    }, updateDelay(), updateDelay());
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

  protected abstract List<ChangingButton> buttons();

  // ----------------------------------------------------------------------------------------------------
  // Overridden methods form Menu
  // ----------------------------------------------------------------------------------------------------


  @Override
  public final ItemStack getItemAt(final int slot) {
    //Will return null if nothing was found
    return changingButtonsMap.getOrDefault(slot, findItem(slot));
  }
}
