package org.mineacademy.punishcontrol.spigot.menus.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.val;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.model.Replacer;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.punishcontrol.core.group.Group;
import org.mineacademy.punishcontrol.core.group.Groups;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.spigot.DaggerSpigotComponent;
import org.mineacademy.punishcontrol.spigot.Scheduler;
import org.mineacademy.punishcontrol.spigot.command.AbstractSimplePunishControlCommand;
import org.mineacademy.punishcontrol.spigot.menu.AbstractBrowser;
import org.mineacademy.punishcontrol.spigot.menus.setting.AbstractSettingsMenu;
import org.mineacademy.punishcontrol.spigot.util.ItemStacks;

public final class PlayerSettingsMenu extends AbstractSettingsMenu {

  private final Button groupBrowser;
  private final Button permissionBrowser;

  /*
  TODO:
   Make unpunishable
   */

  private final UUID target;

  public static void showTo(@NonNull final Player player,
      @NonNull final UUID target) {
    new PlayerSettingsMenu(target).displayTo(player);
  }

  private PlayerSettingsMenu(final UUID target) {
    super(DaggerSpigotComponent.create().settingsBrowser());
    this.target = target;

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
          PermissionsBrowser.showTo(player, target, PlayerSettingsMenu.this);
      }

      @Override
      public ItemStack getItem() {
        return ItemCreator
            .of(CompMaterial.ICE,
                "&7Permissions",
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
    if (slot == 0) {
      return permissionBrowser.getItem();
    }
    if (slot == 1) {
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
    Scheduler.runAsync(() -> new GroupBrowser(parent, target).displayTo(player));
  }

  protected GroupBrowser(final PlayerSettingsMenu parent, final UUID target) {
    super(parent, Groups.list(target));
  }

  @Override
  protected ItemStack convertToItemStack(final Group group) {

    final Replacer replacer = Replacer.of(
        "&6Priority: &7{priority}",
        "&6Ban-Limit: &7{ban-limit}",
        "&6Mute-Limit: &7{mute-limit}",
        "&6Warn-Limit: &7{warn-limit}",
        ""
    );

    replacer.find("priority", "ban-limit", "mute-limit", "warn-limit");
    replacer.replace(
        group.priority(),
        group.banLimit().toString(),
        group.muteLimit().toString(),
        group.warnLimit().toString());

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
  protected void onPageClick(
      final Player player, final Group item, final ClickType click) {

  }
}

class PermissionsBrowser extends AbstractBrowser<String> {

  private final PlayerProvider playerProvider;
  private final UUID target;

  public static void showTo(
      final Player player,
      final UUID target,
      final PlayerSettingsMenu parent) {
    //Adding permissions of our commands
    final List<String> content = AbstractSimplePunishControlCommand
        .registeredCommands()
        .stream()
        .map(SimpleCommand::getPermission).collect(Collectors.toList());

    Scheduler.runAsync(() -> {
      final val menu = new PermissionsBrowser(target, content, parent);
      menu.displayTo(player);
    });
  }


  private PermissionsBrowser(
      final UUID target, final List<String> content,
      final PlayerSettingsMenu parent) {
    super(parent, content);
    this.target = target;
    playerProvider = Providers.playerProvider();
  }

  @Override
  protected ItemStack convertToItemStack(final String permission) {
    if (playerProvider.hasPermission(target, permission)) {
      final List<String> lore = new ArrayList<>(
          descriptionForPermission(permission));
      lore.addAll(Arrays.asList(" ", "&aHas access"));

      return ItemCreator
          .of(ItemStacks.greenPane())
          .name("&6Permission: " + permission)
          .lores(lore)
          .build()
          .makeMenuTool();
    }

    final List<String> lore = new ArrayList<>(
        descriptionForPermission(permission));
    lore.addAll(Arrays.asList(" ", "&cHas no access"));

    return ItemCreator
        .of(ItemStacks.redPane())
        .name("&6Permission: " + permission)
        .lores(lore)
        .build()
        .makeMenuTool();

  }

  private List<String> descriptionForPermission(final String item) {
    final val optionalCommand = AbstractSimplePunishControlCommand
        .registeredCommands()
        .stream()
        .filter((cmd) -> item.equalsIgnoreCase(cmd.getPermission()))
        .findFirst();

    //TODO add menu permissions!
    return optionalCommand
        .map(abstractSimplePunishControlCommand -> Arrays.asList(
            "",
            "&7Permission to ",
            "&7" + abstractSimplePunishControlCommand.getDescription()))
        .orElseGet(ArrayList::new);
  }


  @Override
  protected void onPageClick(
      final Player player, final String item, final ClickType click) {
  }
}
