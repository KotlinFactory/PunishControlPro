package org.mineacademy.punishcontrol.spigot.menu;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.val;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.punishcontrol.spigot.menu.buttons.UpdatingButton;
import org.mineacademy.punishcontrol.spigot.util.Schedulable;

public abstract class AbstractUpdatingMenu extends Menu implements Schedulable {

  private final Map<Integer, ItemStack> changingButtonsMap = new ConcurrentHashMap<>();
  private final Map<Integer, UpdatingButton> buttonMap = new HashMap<>();

  public AbstractUpdatingMenu() {
  }

  public AbstractUpdatingMenu(final Menu parent) {
    super(parent);
  }

  public AbstractUpdatingMenu(final Menu parent,
      final boolean returnMakesNewInstance) {
    super(parent, returnMakesNewInstance);
  }

  protected final void registerButton(final int slot,
      final UpdatingButton updatingButton) {
    buttonMap.put(slot, updatingButton);
  }

  private void start() {
    repeatAsync(() -> {
      for (final val entry : buttonMap.entrySet()) {
        changingButtonsMap.put(entry.getKey(), entry.getValue().build());
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

  // ----------------------------------------------------------------------------------------------------
  // Overridden methods form Menu
  // ----------------------------------------------------------------------------------------------------


  @Override
  public final ItemStack getItemAt(final int slot) {
    //Will return null if nothing was found
    return changingButtonsMap.getOrDefault(slot, findItem(slot));
  }

  interface ItemStackFactory {
    ItemStack build();
  }
}
