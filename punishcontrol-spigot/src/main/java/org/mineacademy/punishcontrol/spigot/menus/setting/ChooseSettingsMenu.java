package org.mineacademy.punishcontrol.spigot.menus.setting;

import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;

public final class ChooseSettingsMenu extends Menu {


  public static void showTo(@NonNull final Player player){
  }



  @Override
  public ItemStack getItemAt(final int slot) {
    return super.getItemAt(slot);
  }

  @Override
  protected void onMenuClick(final Player player, final int slot,
      final ItemStack clicked) {
    super.onMenuClick(player, slot, clicked);
  }
}
