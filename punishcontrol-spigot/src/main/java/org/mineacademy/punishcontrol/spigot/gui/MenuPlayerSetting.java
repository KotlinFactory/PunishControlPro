package org.mineacademy.punishcontrol.spigot.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.MenuPagged;

import java.util.UUID;

public final class MenuPlayerSetting extends MenuPagged<UUID> {

  protected MenuPlayerSetting(final Iterable<UUID> pages) {
    super(pages);
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
