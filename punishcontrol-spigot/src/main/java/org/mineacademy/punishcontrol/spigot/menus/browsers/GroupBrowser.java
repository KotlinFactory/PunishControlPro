package org.mineacademy.punishcontrol.spigot.menus.browsers;

import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NonNls;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.punishcontrol.core.group.Group;
import org.mineacademy.punishcontrol.core.group.Groups;
import org.mineacademy.punishcontrol.core.injector.annotations.Localizable;
import org.mineacademy.punishcontrol.core.setting.Replacer;
import org.mineacademy.punishcontrol.spigot.Scheduler;
import org.mineacademy.punishcontrol.spigot.menu.browser.AbstractBrowser;
import org.mineacademy.punishcontrol.spigot.menus.settings.PlayerSettingsMenu;

@Localizable
public final class GroupBrowser extends AbstractBrowser<Group> {

  // ----------------------------------------------------------------------------------------------------
  // Localization
  // ----------------------------------------------------------------------------------------------------

  @Localizable("Menu.Proxy.GroupBrowser.Information")
  public static String[] MENU_INFORMATION = {
      "&7Menu to view",
      "&7the groups",
      "&7a player has",
      "&7changes can't",
      "&7be yet made"
  };
  @NonNls
  @Localizable("Parts.Yes")
  private static String YES = "yes";
  @NonNls
  @Localizable("Parts.No")
  private static String NO = "no";
  @NonNls
  @Localizable("Parts.Groups")
  private static String GROUPS = "Groups";
  @Localizable("Menu.Proxy.GroupBrowser.Lore")
  private static Replacer GROUP_REPLACER = Replacer
      .of(
          "&6Priority: &7{priority}",
          "&6Ban-Limit: &7{ban-limit}",
          "&6Mute-Limit: &7{mute-limit}",
          "&6Warn-Limit: &7{warn-limit}",
          "&6Override-Punishes: &7{override}",
          "&6Template only: &7{template_only}"
         );

  // ----------------------------------------------------------------------------------------------------
  // Displaying
  // ----------------------------------------------------------------------------------------------------

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
    setTitle("&8" + GROUPS);
  }

  @Override
  protected ItemStack convertToItemStack(final Group group) {

    GROUP_REPLACER.find(
        "priority",
        "ban-limit",
        "mute-limit",
        "warn-limit",
        "override",
        "template_only");

    GROUP_REPLACER.replace(
        group.priority(),
        group.banLimit().toString(),
        group.muteLimit().toString(),
        group.warnLimit().toString(),
        group.overridePunishes() ? "&" + YES : "&c" + NO,
        group.templateOnly() ? "&a" + YES : "&c" + NO);

    //TODO CHECK FOR ERRORS: WHAT IF THE MATERIAL IS INVALID
    final CompMaterial compMaterial = CompMaterial.fromString(group.item());

    return ItemCreator
        .of(
            compMaterial,
            "&7" + group.name(),
            GROUP_REPLACER.replacedMessage())
        .build()
        .makeMenuTool();
  }

  @Override
  protected String[] getInfo() {
    return MENU_INFORMATION;
  }

  @Override
  protected void onPageClick(
      final Player player, final Group item, final ClickType click) {

  }
}
