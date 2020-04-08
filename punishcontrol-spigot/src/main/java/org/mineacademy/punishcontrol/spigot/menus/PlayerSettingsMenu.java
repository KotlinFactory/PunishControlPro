package org.mineacademy.punishcontrol.spigot.menus;

import java.util.ArrayList;
import java.util.UUID;
import javax.inject.Inject;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.MenuPagged;
import org.mineacademy.punishcontrol.spigot.DaggerSpigotComponent;

public final class PlayerSettingsMenu extends MenuPagged<UUID> {

  @Inject
  public PlayerSettingsMenu() {
    super(new ArrayList<>());
  }

  public static void showTo(@NonNull final Player player){
    DaggerSpigotComponent.create().playerSettingsMenu().displayTo(player);
  }

  @Override
  protected String[] getInfo() {
    return new String[0];
  }

  @Override
  protected ItemStack convertToItemStack(final UUID uuid) {
    return null;
  }

  @Override
  protected void onPageClick(final Player player, final UUID uuid, final ClickType clickType) {}
}
