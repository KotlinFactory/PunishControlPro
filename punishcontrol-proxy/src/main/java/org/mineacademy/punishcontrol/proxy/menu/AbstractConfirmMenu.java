package org.mineacademy.punishcontrol.proxy.menu;

import de.exceptionflug.mccommons.inventories.api.CallResult;
import org.mineacademy.bfo.Common;
import org.mineacademy.bfo.debug.Debugger;
import org.mineacademy.burst.item.Item;
import org.mineacademy.burst.menu.AbstractMenu;
import org.mineacademy.burst.menu.BurstMenu;
import org.mineacademy.punishcontrol.core.injector.annotations.Localizable;
import org.mineacademy.punishcontrol.core.settings.ItemSettings;

@Localizable
public abstract class AbstractConfirmMenu extends AbstractMenu {

  @Localizable(value = "Parts.Confirm")
  private static String CONFIRM = "Confirm";
  @Localizable(value = "Parts.Break")
  private static String BREAKUP = "Breakup";

//  public AbstractConfirmMenu() {
//    super("Confirm", 9);
//    setTitle("&aConfirm");
//  }

  public AbstractConfirmMenu(final BurstMenu parent) {
    super(CONFIRM, parent, 9);
    updateInventory();
  }

  @Override
  public void updateInventory() {
    setTitle("&8" + CONFIRM);
    set(
        Item
            .ofString(ItemSettings.APPLY_ITEM.itemType())
            .name("&a" + CONFIRM)
            .lore("&7" + CONFIRM)
            .slot(0)
            .actionHandler("Confirm")
    );

    set(
        Item
            .ofString(ItemSettings.BREAK_UP_ITEM.itemType())
            .name("&3" + BREAKUP)
            .slot(8)
            .actionHandler("Break-Up")
    );
  }

  @Override
  public void registerActionHandlers() {
    registerActionHandler("Break-Up", (
        breakUp -> {

          showParent();

          return CallResult.DENY_GRABBING;
        }));

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
