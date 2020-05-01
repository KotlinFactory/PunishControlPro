package org.mineacademy.punishcontrol.proxy.menu;

import de.exceptionflug.mccommons.inventories.api.CallResult;
import de.exceptionflug.protocolize.items.ItemType;
import org.mineacademy.bfo.Common;
import org.mineacademy.bfo.debug.Debugger;
import org.mineacademy.burst.item.Item;
import org.mineacademy.burst.menu.AbstractMenu;
import org.mineacademy.burst.menu.BurstMenu;

public abstract class AbstractConfirmMenu extends AbstractMenu {

  public AbstractConfirmMenu() {
    super("Confirm", 9);
    setTitle("&aConfirm");
  }

  public AbstractConfirmMenu(final BurstMenu parent) {
    super("Confirm", parent, 9);
  }

  @Override
  public void updateInventory() {
    setTitle("&8Confirm");
    set(
        Item
            .of(ItemType.EMERALD_BLOCK).name("&aConfirm").lore("&7Confirm")
            .slot(4)
            .actionHandler("Confirm")
    );
  }

  @Override
  public void registerActionHandlers() {
    registerActionHandler("Confirm", (click) -> {
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
      return CallResult.DENY_GRABBING;
    });
  }

  public abstract void onConfirm();

  protected void showParent() {
    if (getParent() == null) {
      return;
    }
    if (getPlayer() == null) {
      return;
    }

    getParent().reDisplay();
  }

  @Override
  protected String[] getInfo() {
    return new String[]{"&7Apply action"};
  }

  @Override
  public void reDisplay() {

  }
}
