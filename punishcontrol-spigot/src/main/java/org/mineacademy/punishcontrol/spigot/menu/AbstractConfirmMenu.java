package org.mineacademy.punishcontrol.spigot.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.debug.Debugger;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.punishcontrol.spigot.util.Schedulable;

public abstract class AbstractConfirmMenu extends Menu implements Schedulable {

  public AbstractConfirmMenu() {
    setSize(9);
    setTitle("&aConfirm");
  }

  public AbstractConfirmMenu(final Menu parent) {
    super(parent);
    setSize(9);
    setTitle("&8Confirm");
  }

  public AbstractConfirmMenu(
      final Menu parent,
      final boolean returnMakesNewInstance) {
    super(parent, returnMakesNewInstance);
    setSize(9);
    setTitle("&8Confirm");
  }

  public abstract void onConfirm();

  protected void showParent() {
    if (getParent() == null) {
      return;
    }
    if (getViewer() == null) {
      return;
    }
    async(() -> {
      getParent().displayTo(getViewer());
      getParent().restartMenu();
    });
  }

  @Override
  public final ItemStack getItemAt(final int slot) {
    if (slot != 4) {
      return null;
    }

    return ItemCreator
        .of(CompMaterial.EMERALD_BLOCK,
            "&aConfirm",
            "",
            "&7Click to confirm")
        .build()
        .makeMenuTool();
  }

  @Override
  protected void onMenuClick(
      final Player player,
      final int slot,
      final ItemStack clicked) {
    super.onMenuClick(player, slot, clicked);
    if (slot != 4) {
      return;
    }
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

  @Override
  protected String[] getInfo() {
    return new String[]{"&7Apply action"};
  }
}
