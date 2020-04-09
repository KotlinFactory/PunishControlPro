package org.mineacademy.punishcontrol.spigot.menus.setting;

import javax.inject.Inject;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.punishcontrol.spigot.DaggerSpigotComponent;
import org.mineacademy.punishcontrol.spigot.menus.MainMenu;
import org.mineacademy.punishcontrol.spigot.menus.settings.Setting;

public final class ChooseSettingsMenu extends Menu {

  public static void showTo(@NonNull final Player player) {
    DaggerSpigotComponent.create().chooseSettingsMenu().displayTo(player);
  }

  @Inject
  public ChooseSettingsMenu(final MainMenu mainMenu) {
    super(mainMenu);
    setTitle("&8Choose Settings");
  }


  @Override
  @Nullable
  public ItemStack getItemAt(final int slot) {
    for (final Setting value : Setting.values()) {
      if (value.slot() == slot) {
        return value.itemCreator().make();
      }
    }

    return null;
  }

  @Override
  protected void onMenuClick(final Player player, final int slot,
      final ItemStack clicked) {
    super.onMenuClick(player, slot, clicked);

    for (final Setting value : Setting.values()) {
      if (value.slot() == slot) {
        value.showMenu(getViewer());
      }
    }
  }
}
