package org.mineacademy.punishcontrol.spigot.menus.browsers;

import java.util.*;
import lombok.val;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NonNls;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.punishcontrol.core.injector.annotations.Localizable;
import org.mineacademy.punishcontrol.core.permission.Permission;
import org.mineacademy.punishcontrol.core.permission.Permissions;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.settings.ItemSettings;
import org.mineacademy.punishcontrol.spigot.Scheduler;
import org.mineacademy.punishcontrol.spigot.menu.browser.AbstractBrowser;
import org.mineacademy.punishcontrol.spigot.menus.settings.PlayerSettingsMenu;

@Localizable
public final class PlayerPermissionBrowser extends AbstractBrowser<Permission> {

  // ----------------------------------------------------------------------------------------------------
  // Localization
  // ----------------------------------------------------------------------------------------------------

  @NonNls
  @Localizable("Parts.Permission")
  private static String PERMISSION = "Permission";

    @NonNls
    @Localizable("Parts.Permissions")
    private static String PERMISSIONS = "Permissions";
    @NonNls
    @Localizable("Parts.Has_Access")
    private static String HAS_ACCESS = "Has access";
    @NonNls
    @Localizable("Parts.Has_No_Access")
    private static String HAS_NO_ACCESS = "Has no access";
    @NonNls
    @Localizable("Parts.Permission_To")
    private static String PERMISSION_TO = "Permission to";

    @Localizable("Menu.Proxy.PlayerPermissionBrowser.Lore")
    private static String[] MENU_INFORMATION = {
        "&7Menu to view",
        "&7the permissions",
        "&7a player has",
        "&7changes will ",
        "&7be temporary"
    };

    // ----------------------------------------------------------------------------------------------------
    // Displaying
    // ----------------------------------------------------------------------------------------------------

    private final PlayerProvider playerProvider;
  private final UUID target;

  public static void showTo(
      final Player player,
      final UUID target,
      final PlayerSettingsMenu parent) {
    //Adding permissions of our commands

    Scheduler.runAsync(() -> {
      final val menu = new PlayerPermissionBrowser(target, parent);
      menu.displayTo(player, true);
    });
  }

  private PlayerPermissionBrowser(
      final UUID target,
      final PlayerSettingsMenu parent) {
    super(parent, Permissions.registeredPermissions());
    this.target = target;
    playerProvider = Providers.playerProvider();
    setTitle("&8" + PERMISSIONS);
  }

  @Override
  protected String[] getInfo() {
    return MENU_INFORMATION;
  }

  @Override
  protected ItemStack convertToItemStack(final Permission permission) {
    if (playerProvider.hasPermission(target, permission.permission())) {
      final List<String> lore = new ArrayList<>(
          descriptionForPermission(permission));
      lore.addAll(Collections.singletonList(HAS_ACCESS));

      return ItemCreator
          .ofString(ItemSettings.ENABLED.itemType())
          .name("&6" + PERMISSION + ": " + permission.permission())
          .lores(lore)
          .build()
          .makeMenuTool();
    }

    final List<String> lore = new ArrayList<>(
        descriptionForPermission(permission));
    lore.addAll(Collections.singletonList(HAS_NO_ACCESS));

    return ItemCreator
        .ofString(ItemSettings.DISABLED.itemType())
        .name("&6" + PERMISSION + ": " + permission.permission())
        .lores(lore)
        .build()
        .makeMenuTool();

  }

  private List<String> descriptionForPermission(final Permission item) {
    return Arrays.asList(
        "",
        "&7" + PERMISSION + " to: ",
        "&7" + String.join(" ", item.description()));
  }

  @Override
  protected void onPageClick(
      final Player player,
      final Permission item,
      final ClickType click) {
  }
}
