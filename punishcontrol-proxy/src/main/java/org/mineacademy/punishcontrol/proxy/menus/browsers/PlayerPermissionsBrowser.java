package org.mineacademy.punishcontrol.proxy.menus.browsers;

import de.exceptionflug.mccommons.inventories.api.ClickType;
import de.exceptionflug.protocolize.items.ItemStack;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.val;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NonNls;
import org.mineacademy.bfo.Players;
import org.mineacademy.burst.item.Item;
import org.mineacademy.burst.menu.AbstractBrowser;
import org.mineacademy.burst.util.Scheduler;
import org.mineacademy.punishcontrol.core.injector.annotations.Localizable;
import org.mineacademy.punishcontrol.core.permission.Permission;
import org.mineacademy.punishcontrol.core.permission.Permissions;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.settings.ItemSettings;
import org.mineacademy.punishcontrol.proxy.menus.settings.PlayerSettingsMenu;

@Localizable
public final class PlayerPermissionsBrowser extends AbstractBrowser<Permission> {

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

  public static void showTo(
      final ProxiedPlayer player,
      final UUID target,
      final PlayerSettingsMenu parent) {
    //Adding permissions of our commands

    Scheduler.runAsync(() -> {
      final val menu = new PlayerPermissionsBrowser(target, parent);
      menu.displayTo(player);
    });
  }

  // ----------------------------------------------------------------------------------------------------
  // Fields & Constructors
  // ----------------------------------------------------------------------------------------------------

  private final PlayerProvider playerProvider;
  private final UUID target;
  private final PlayerSettingsMenu parent;

  private PlayerPermissionsBrowser(
      final UUID target,
      final PlayerSettingsMenu parent) {
    super(PERMISSION + "Browser", parent, Permissions.registeredPermissions());
    this.parent = parent;
    this.target = target;
    playerProvider = Providers.playerProvider();
    setTitle("&8" + PERMISSIONS);
  }

//  private PermissionsBrowser(
//      final PlayerSettingsMenu parent,
//      final Collection<Permission> permissions
//  ) {
//    super("PermissionBrowser", parent, permissions);
//    this.parent = parent;
//    this.target = target;
//    playerProvider = Providers.playerProvider();
//    setTitle("&8Permissions");
//  }
//

  // ----------------------------------------------------------------------------------------------------
  // Overridden methods
  // ----------------------------------------------------------------------------------------------------

  @Override
  protected String[] getInfo() {
    return MENU_INFORMATION;
  }

  @Override
  public void reDisplay() {
    async(() -> {
      final PlayerPermissionsBrowser menu = new PlayerPermissionsBrowser(target, parent);
      menu.setCurrentPage(getCurrentPageIndex());
      menu.displayTo(getPlayer());
    });
  }

  @Override
  protected ItemStack convertToItemStack(final Permission permission) {
    if (playerProvider.hasPermission(target, permission.permission())) {
      final List<String> lore = new ArrayList<>(
          descriptionForPermission(permission));
      lore.addAll(Arrays.asList(" ", "&a" + HAS_ACCESS));

      return Item
          .ofString(ItemSettings.ENABLED.itemType())
          .name("&6" + PERMISSION + ": " + permission.permission())
          .lore(lore)
          .build();
    }

    final List<String> lore = new ArrayList<>(
        descriptionForPermission(permission));
    lore.addAll(Arrays.asList(" ", "&c" + HAS_NO_ACCESS));

    return Item
        .ofString(ItemSettings.DISABLED.itemType())
        .name("&6" + PERMISSION + ": " + permission.permission())
        .lore(lore)
        .build();
  }

  private List<String> descriptionForPermission(final Permission item) {
    return Arrays.asList(
        "",
        "&7" + PERMISSION_TO + ": ",
        "&7" + String.join(" ", item.description()));
  }

  @Override
  protected void onClick(final ClickType clickType, final Permission permission) {
    Players.find(target).ifPresent((
        player -> {
          player.setPermission(
              permission.permission(),
              !player.hasPermission(permission.permission()));
          reDisplay();
        }));
  }
}
