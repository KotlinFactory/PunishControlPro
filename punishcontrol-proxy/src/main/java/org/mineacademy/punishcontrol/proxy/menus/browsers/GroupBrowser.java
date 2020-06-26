package org.mineacademy.punishcontrol.proxy.menus.browsers;

import de.exceptionflug.mccommons.inventories.api.ClickType;
import de.exceptionflug.protocolize.items.ItemStack;
import de.exceptionflug.protocolize.items.ItemType;
import java.util.Collection;
import java.util.UUID;
import lombok.NonNull;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NonNls;
import org.mineacademy.burst.item.Item;
import org.mineacademy.burst.menu.AbstractBrowser;
import org.mineacademy.burst.util.Scheduler;
import org.mineacademy.punishcontrol.core.group.Group;
import org.mineacademy.punishcontrol.core.group.Groups;
import org.mineacademy.punishcontrol.core.injector.annotations.Localizable;
import org.mineacademy.punishcontrol.core.setting.Replacer;
import org.mineacademy.punishcontrol.proxy.menus.settings.PlayerSettingsMenu;

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
  private static Replacer GROUP_REPLACER = Replacer.of(
      "&6Priority: &7{priority}",
      "&6Ban-Limit: &7{ban-limit}",
      "&6Mute-Limit: &7{mute-limit}",
      "&6Warn-Limit: &7{warn-limit}",
      "&6Override-Punishes: &7{override}",
      "&6Template only: &7{template_only}");

  // ----------------------------------------------------------------------------------------------------
  // Displaying
  // ----------------------------------------------------------------------------------------------------

  public static void showTo(
      final ProxiedPlayer player,
      final UUID target,
      final PlayerSettingsMenu parent) {
    Scheduler.runAsync(
        () -> new GroupBrowser(parent, target).displayTo(player)
                      );
  }

  public static void showTo(
      @NonNull final ProxiedPlayer player,
      @NonNull final PlayerSettingsMenu parent) {
    Scheduler.runAsync(
        () -> new GroupBrowser(parent).displayTo(player)
                      );
  }

  // ----------------------------------------------------------------------------------------------------
  // Fields & Constructors
  // ----------------------------------------------------------------------------------------------------

  private final PlayerSettingsMenu parent;
  private UUID target;

  private GroupBrowser(final PlayerSettingsMenu parent, final UUID target) {
    super("GroupBrowser", parent, Groups.list(target));
    this.parent = parent;
    this.target = target;
    setTitle("&8" + GROUPS);
  }

  private GroupBrowser(final PlayerSettingsMenu parent, final Collection<Group> groups) {
    super("GroupBrowser", parent, groups);
    this.parent = parent;
    setTitle("&8" + GROUPS);
  }

  private GroupBrowser(final PlayerSettingsMenu parent) {
    super("GroupBrowser", parent, Groups.registeredGroups());
    this.parent = parent;
    setTitle("&8" + GROUPS);
  }

  // ----------------------------------------------------------------------------------------------------
  // Overridden methods
  // ----------------------------------------------------------------------------------------------------

  @Override
  protected void onClick(final ClickType clickType, final Group group) {
    // Nothing
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
        group.overridePunishes() ? "&a" + YES : "&c" + NO,
        group.templateOnly() ? "&a" + YES : "&c" + NO

                          );

    //TODO CHECK FOR ERRORS: WHAT IF THE MATERIAL IS INVALID
    final ItemType material = ItemType.valueOf(group.item());

    return Item
        .of(
            material,
            "&7" + group.name(),
            GROUP_REPLACER.replacedMessage())
        .build();
  }

  @Override
  protected String[] getInfo() {
    return MENU_INFORMATION;
  }

  @Override
  public void reDisplay() {
    if (target == null)
      showTo(player, parent);
    else
      showTo(getPlayer(), target, parent);
  }
}
