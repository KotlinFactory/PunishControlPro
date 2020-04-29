package org.mineacademy.punishcontrol.proxy.menus.settings;

import de.exceptionflug.mccommons.inventories.api.CallResult;
import de.exceptionflug.mccommons.inventories.api.ClickType;
import de.exceptionflug.protocolize.items.ItemStack;
import de.exceptionflug.protocolize.items.ItemType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import lombok.val;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.mineacademy.bfo.Players;
import org.mineacademy.burst.item.Item;
import org.mineacademy.burst.menu.AbstractBrowser;
import org.mineacademy.burst.util.Scheduler;
import org.mineacademy.punishcontrol.core.group.Group;
import org.mineacademy.punishcontrol.core.group.Groups;
import org.mineacademy.punishcontrol.core.permission.Permission;
import org.mineacademy.punishcontrol.core.permission.Permissions;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.settings.Replacer;
import org.mineacademy.punishcontrol.proxy.DaggerProxyComponent;
import org.mineacademy.punishcontrol.proxy.menus.setting.AbstractSettingsMenu;

public final class PlayerSettingsMenu extends AbstractSettingsMenu {

  public static final int PERMISSION_BROWSER_SLOT = 0;
  public static final int GROUP_BROWSER_SLOT = 1;

  private final boolean targetOnline;
  private final UUID target;

  /*
  TODO:
   Make unpunishable
   */

  public static void showTo(
      @NonNull final ProxiedPlayer player,
      @NonNull final UUID target) {
    new PlayerSettingsMenu(target).displayTo(player);
  }

  private PlayerSettingsMenu(final UUID target) {
    super(DaggerProxyComponent.create().settingsBrowser(), 9 * 2);
    this.target = target;
    targetOnline = Players.find(target).isPresent();
    setTitle("&8Settings for player");
  }

  @Override
  public void updateInventory() {
    super.updateInventory();

    // Groups | "Groups"
    {
      set(
          Item
              .of(ItemType.OBSIDIAN,
                  "&6Groups",
                  " ",
                  "&7View groups",
                  "&7the player has",
                  "&7See your settings.yml",
                  "&7to edit them")
              .slot(GROUP_BROWSER_SLOT)
              .actionHandler("Groups")
      );
    }

    // Permissions | "Permissions"
    {
      if (!targetOnline) {
        Item
            .of(ItemType.ICE,
                "&6Permissions",
                "",
                "&7Cant be used:",
                "&7target is offline")
            .slot(PERMISSION_BROWSER_SLOT)
            .actionHandler("Permissions");
      } else {
        set(
            Item
                .of(ItemType.ICE,
                    "&6Permissions",
                    "",
                    "&7View which",
                    "&7actions the player",
                    "&7is allowed to take")
                .slot(PERMISSION_BROWSER_SLOT)
                .actionHandler("Permissions")
        );
      }
    }
  }

  @Override
  public void reDisplay() {
    showTo(getPlayer(), target);
  }

  @Override
  public void registerActionHandlers() {
    registerActionHandler("Groups", (groups -> {
      GroupBrowser.showTo(getPlayer(), target, this);
      return CallResult.DENY_GRABBING;
    }));

    registerActionHandler("Permissions", (permissions -> {
      PermissionsBrowser.showTo(getPlayer(), target, this);
      return CallResult.DENY_GRABBING;
    }));
  }
}

// ----------------------------------------------------------------------------------------------------
// Sub-Classes that are only needed for this menu
// ----------------------------------------------------------------------------------------------------

final class GroupBrowser extends AbstractBrowser<Group> {

  private final PlayerSettingsMenu parent;
  private final UUID target;

  public static void showTo(
      final ProxiedPlayer player,
      final UUID target,
      final PlayerSettingsMenu parent) {
    Scheduler.runAsync(
        () -> new GroupBrowser(parent, target).displayTo(player)
    );
  }

  protected GroupBrowser(final PlayerSettingsMenu parent, final UUID target) {
    super("GroupBrowser", parent, Groups.list(target));
    this.parent = parent;
    this.target = target;
    setTitle("&8Groups");
  }

  @Override
  protected void onClick(final ClickType clickType, final Group group) {

  }

  @Override
  protected ItemStack convertToItemStack(final Group group) {
    final Replacer replacer = Replacer.of(
        "&6Priority: &7{priority}",
        "&6Ban-Limit: &7{ban-limit}",
        "&6Mute-Limit: &7{mute-limit}",
        "&6Warn-Limit: &7{warn-limit}",
        "&6Override-Punishes: &7{override}",
        "&6Template only: &7{template_only}"
    );

    replacer.find("priority",
        "ban-limit",
        "mute-limit",
        "warn-limit",
        "override",
        "template_only");

    replacer.replace(
        group.priority(),
        group.banLimit().toString(),
        group.muteLimit().toString(),
        group.warnLimit().toString(),
        group.overridePunishes() ? "&ayes" : "&cno",
        group.templateOnly() ? "&ayes" : "&cno"

    );

    //TODO CHECK FOR ERRORS: WHAT IF THE MATERIAL IS INVALID
    final ItemType material = ItemType.valueOf(group.item());

    return Item
        .of(material,
            "&7" + group.name(),
            replacer.replacedMessage())
        .build();
  }

  @Override
  protected String[] getInfo() {
    return new String[]{
        "&7Menu to view",
        "&7the groups",
        "&7a player has"
    };
  }

  @Override
  public void reDisplay() {
    showTo(getPlayer(), target, parent);
  }
}

class PermissionsBrowser extends AbstractBrowser<Permission> {

  private final PlayerProvider playerProvider;
  private final UUID target;
  private final PlayerSettingsMenu parent;

  public static void showTo(
      final ProxiedPlayer player,
      final UUID target,
      final PlayerSettingsMenu parent) {
    //Adding permissions of our commands

    Scheduler.runAsync(() -> {
      final val menu = new PermissionsBrowser(target, parent);
      menu.displayTo(player);
    });
  }

  private PermissionsBrowser(
      final UUID target,
      final PlayerSettingsMenu parent) {
    super("PermissionBrowser", parent, Permissions.registeredPermissions());
    this.parent = parent;
    this.target = target;
    playerProvider = Providers.playerProvider();
    setTitle("&8Permissions");
  }

  @Override
  protected String[] getInfo() {
    return new String[]{
        "&7Menu to view",
        "&7the permissions",
        "&7a player has"
    };
  }

  @Override
  public void reDisplay() {
    showTo(getPlayer(), target, parent);
  }

  @Override
  protected ItemStack convertToItemStack(final Permission permission) {
    if (playerProvider.hasPermission(target, permission.permission())) {
      final List<String> lore = new ArrayList<>(
          descriptionForPermission(permission));
      lore.addAll(Arrays.asList(" ", "&aHas access"));

      return Item
          .of(ItemType.GREEN_STAINED_GLASS_PANE)
          .name("&6Permission: " + permission.permission())
          .lore(lore)
          .build();
    }

    final List<String> lore = new ArrayList<>(
        descriptionForPermission(permission));
    lore.addAll(Arrays.asList(" ", "&cHas no access"));

    return Item
        .of(ItemType.RED_STAINED_GLASS_PANE)
        .name("&6Permission: " + permission.permission())
        .lore(lore)
        .build();
  }

  private List<String> descriptionForPermission(final Permission item) {
    return Arrays.asList(
        "",
        "&7Permission to: ",
        "&7" + String.join(" ", item.description()));
  }

  @Override
  protected void onClick(final ClickType clickType, final Permission permission) {
    // Do nothing
  }
}
