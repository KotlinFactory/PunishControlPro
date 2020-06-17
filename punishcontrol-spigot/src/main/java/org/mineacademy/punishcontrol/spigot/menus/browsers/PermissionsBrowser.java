package org.mineacademy.punishcontrol.spigot.menus.browsers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.val;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.punishcontrol.core.permission.Permission;
import org.mineacademy.punishcontrol.core.permission.Permissions;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.settings.ItemSettings;
import org.mineacademy.punishcontrol.spigot.Scheduler;
import org.mineacademy.punishcontrol.spigot.menu.browser.AbstractBrowser;
import org.mineacademy.punishcontrol.spigot.menus.settings.PlayerSettingsMenu;

public final class PermissionsBrowser extends AbstractBrowser<Permission> {

  private final PlayerProvider playerProvider;
  private final UUID target;

  public static void showTo(
      final Player player,
      final UUID target,
      final PlayerSettingsMenu parent) {
    //Adding permissions of our commands

    Scheduler.runAsync(() -> {
      final val menu = new PermissionsBrowser(target, parent);
      menu.displayTo(player, true);
    });
  }


  private PermissionsBrowser(
      final UUID target,
      final PlayerSettingsMenu parent) {
    super(parent, Permissions.registeredPermissions());
    this.target = target;
    playerProvider = Providers.playerProvider();
    setTitle("&8Permissions");
  }

  @Override
  protected String[] getInfo() {
    return new String[]{
        "&7Menu to view",
        "&7the permissions",
        "&7a player has",
        "&7changes can't be",
        "&7made on spigot site" // Don't mess around with spigot
    };
  }

  @Override
  protected ItemStack convertToItemStack(final Permission permission) {
    if (playerProvider.hasPermission(target, permission.permission())) {
      final List<String> lore = new ArrayList<>(
          descriptionForPermission(permission));
      lore.addAll(Arrays.asList(" ", "&aHas access"));

      return ItemCreator
          .ofString(ItemSettings.ENABLED.itemType())
          .name("&6Permission: " + permission.permission())
          .lores(lore)
          .build()
          .makeMenuTool();
    }

    final List<String> lore = new ArrayList<>(
        descriptionForPermission(permission));
    lore.addAll(Arrays.asList(" ", "&cHas no access"));

    return ItemCreator
        .ofString(ItemSettings.DISABLED.itemType())
        .name("&6Permission: " + permission.permission())
        .lores(lore)
        .build()
        .makeMenuTool();

  }

  private List<String> descriptionForPermission(final Permission item) {
    return Arrays.asList(
        "",
        "&7Permission to: ",
        "&7" + String.join(" ", item.description()));
  }


  @Override
  protected void onPageClick(
      final Player player,
      final Permission item,
      final ClickType click) {
  }
}
