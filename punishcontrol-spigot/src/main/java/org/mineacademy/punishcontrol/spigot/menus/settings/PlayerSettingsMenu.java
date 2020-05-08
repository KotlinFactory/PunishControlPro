package org.mineacademy.punishcontrol.spigot.menus.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import lombok.val;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Players;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.model.Replacer;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.punishcontrol.core.group.Group;
import org.mineacademy.punishcontrol.core.group.Groups;
import org.mineacademy.punishcontrol.core.permission.Permission;
import org.mineacademy.punishcontrol.core.permission.Permissions;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.spigot.DaggerSpigotComponent;
import org.mineacademy.punishcontrol.spigot.Scheduler;
import org.mineacademy.punishcontrol.spigot.menu.browser.AbstractBrowser;
import org.mineacademy.punishcontrol.spigot.menus.setting.AbstractSettingsMenu;
import org.mineacademy.punishcontrol.spigot.util.ItemStacks;

public final class PlayerSettingsMenu extends AbstractSettingsMenu {

  public static final int PERMISSION_BROWSER_SLOT = 0;
  public static final int GROUP_BROWSER_SLOT = 1;
  private final Button groupBrowser;
  private final Button permissionBrowser;
  private final boolean targetOnline;
  private final UUID target;

  /*
  TODO:
   Make unpunishable
   */


  public static void showTo(
      @NonNull final Player player,
      @NonNull final UUID target) {
    new PlayerSettingsMenu(target).displayTo(player, true);
  }

  private PlayerSettingsMenu(final UUID target) {
    super(DaggerSpigotComponent.create().settingsBrowser());
    this.target = target;
    targetOnline = Players.find(target).isPresent();
    setTitle("&8Settings for player");

    groupBrowser = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player, final Menu menu, final ClickType click) {
        GroupBrowser.showTo(player, target, PlayerSettingsMenu.this);
      }

      @Override
      public ItemStack getItem() {
        return ItemCreator
            .of(CompMaterial.OBSIDIAN,
                "&6Groups",
                " ",
                "&7View groups",
                "&7the player has",
                "&7See your settings.yml",
                "&7to edit them")
            .build()
            .makeMenuTool();
      }
    };

    permissionBrowser = new Button() {

      @Override
      public void onClickedInMenu(
          final Player player, final Menu menu, final ClickType click) {
        if (!targetOnline) {
          animateTitle("&cTarget is offline");
          return;
        }
        PermissionsBrowser.showTo(player, target, PlayerSettingsMenu.this);
      }

      @Override
      public ItemStack getItem() {
        if (!targetOnline) {
          return ItemCreator
              .of(CompMaterial.ICE,
                  "&6Permissions",
                  "",
                  "&7Cant be used:",
                  "&7target is offline")
              .build()
              .makeMenuTool();
        }

        return ItemCreator
            .of(CompMaterial.ICE,
                "&6Permissions",
                "",
                "&7View which",
                "&7actions the player",
                "&7is allowed to take")
            .build()
            .makeMenuTool();
      }
    };
  }

  @Override
  public ItemStack getItemAt(final int slot) {
    if (slot == PERMISSION_BROWSER_SLOT) {
      return permissionBrowser.getItem();
    }
    if (slot == GROUP_BROWSER_SLOT) {
      return groupBrowser.getItem();
    }

    return null;
  }
}

// ----------------------------------------------------------------------------------------------------
// Sub-Classes that are only needed for this menu
// ----------------------------------------------------------------------------------------------------

final class GroupBrowser extends AbstractBrowser<Group> {

  public static void showTo(
      final Player player,
      final UUID target,
      final PlayerSettingsMenu parent) {
    Scheduler.runAsync(
        () -> new GroupBrowser(parent, target).displayTo(player, true)
    );
  }

  protected GroupBrowser(final PlayerSettingsMenu parent, final UUID target) {
    super(parent, Groups.list(target));
    setTitle("&8Groups");
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
    final CompMaterial compMaterial = CompMaterial.fromString(group.item());

    return ItemCreator
        .of(compMaterial,
            "&7" + group.name(),
            replacer.getReplacedMessage())
        .build()
        .makeMenuTool();
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
  protected void onPageClick(
      final Player player, final Group item, final ClickType click) {

  }
}

class PermissionsBrowser extends AbstractBrowser<Permission> {

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
        "&7a player has"
    };
  }

  @Override
  protected ItemStack convertToItemStack(final Permission permission) {
    if (playerProvider.hasPermission(target, permission.permission())) {
      final List<String> lore = new ArrayList<>(
          descriptionForPermission(permission));
      lore.addAll(Arrays.asList(" ", "&aHas access"));

      return ItemCreator
          .of(ItemStacks.greenPane())
          .name("&6Permission: " + permission.permission())
          .lores(lore)
          .build()
          .makeMenuTool();
    }

    final List<String> lore = new ArrayList<>(
        descriptionForPermission(permission));
    lore.addAll(Arrays.asList(" ", "&cHas no access"));

    return ItemCreator
        .of(ItemStacks.redPane())
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
      final Player player, final Permission item, final ClickType click) {
  }
}
