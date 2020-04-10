package org.mineacademy.punishcontrol.spigot.menus.settings;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.punishcontrol.core.group.Group;
import org.mineacademy.punishcontrol.spigot.DaggerSpigotComponent;
import org.mineacademy.punishcontrol.spigot.menu.AbstractBrowser;
import org.mineacademy.punishcontrol.spigot.menus.setting.AbstractSettingsMenu;

public final class PlayerSettingsMenu extends AbstractSettingsMenu {

  private final UUID target;

  public static void showTo(@NonNull final Player player, @NonNull final UUID target){
    new PlayerSettingsMenu(target).displayTo(player);
  }

  private PlayerSettingsMenu(final UUID target) {
    super(DaggerSpigotComponent.create().settingsBrowser());
    this.target = target;
  }
}

// ----------------------------------------------------------------------------------------------------
// Sub-Classes that are only needed for this menu
// ----------------------------------------------------------------------------------------------------

class GroupBrowser extends AbstractBrowser<Group>{

  protected GroupBrowser(
      final PlayerSettingsMenu parent,
      final List<Group> content) {
    super(parent, content);
  }

  @Override
  protected ItemStack convertToItemStack(final Group item) {
    return null;
  }


  @Override
  protected void onPageClick(
      final Player player, final Group item, final ClickType click) {

  }
}

class PermissionsBrowser extends AbstractBrowser<String>{

  protected PermissionsBrowser(final PlayerSettingsMenu parent) {
    super(parent, Arrays.asList("", ""));
  }

  @Override
  protected ItemStack convertToItemStack(final String item) {
    return null;
  }

  @Override
  protected void onPageClick(
      final Player player, final String item, final ClickType click) {
  }
}
