package org.mineacademy.punishcontrol.spigot.menus.browsers;

import lombok.NonNull;
import lombok.val;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NonNls;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.punishcontrol.core.injector.annotations.Localizable;
import org.mineacademy.punishcontrol.core.notification.Notification;
import org.mineacademy.punishcontrol.core.notification.Notifications;
import org.mineacademy.punishcontrol.spigot.DaggerSpigotComponent;
import org.mineacademy.punishcontrol.spigot.Scheduler;
import org.mineacademy.punishcontrol.spigot.menu.AbstractConfirmMenu;
import org.mineacademy.punishcontrol.spigot.menu.browser.AbstractBrowser;

public final class NotificationBrowser extends AbstractBrowser<Notification> {

  // ----------------------------------------------------------------------------------------------------
  // Localization
  // ----------------------------------------------------------------------------------------------------

  @NonNls
  private static final String NOTIFICATIONS = "Notifications";
  @NonNls
  private static final String RIGHT_CLICK_TO_DISMISS = "Right click to dismiss";
  @Localizable("Menu.Proxy.NotificationBrowser.Menu_Information")
  private static String[] MENU_INFORMATION = {
      "&7All current notifications",
      "&7are listed here"
  };

  // ----------------------------------------------------------------------------------------------------
  // Static methods
  // ----------------------------------------------------------------------------------------------------

  public static void showTo(@NonNull final Player player) {
    Scheduler.runAsync(() -> new NotificationBrowser().displayTo(player));
  }

  // ----------------------------------------------------------------------------------------------------
  // Constructors
  // ----------------------------------------------------------------------------------------------------

  private NotificationBrowser() {
    super(
        DaggerSpigotComponent.create().settingsBrowser(),
        Notifications.registeredNotifications());
    setTitle("&8" + NOTIFICATIONS);
  }

  @Override
  protected ItemStack convertToItemStack(final Notification item) {
    final CompMaterial type = item.itemType();
    final val text = Common.colorize(item.text());
    text.add(" ");
    text.add("&6" + RIGHT_CLICK_TO_DISMISS);
    return ItemCreator
        .of(type)
        .name(Common.colorize(item.name()))
        .lores(text)
        .build()
        .make();
  }

  @Override
  protected void onPageClick(
      final Player player, final Notification item,
      final ClickType click) {
    if (!click.isRightClick())
      return;

    new AbstractConfirmMenu(this) {
      @Override
      public void onConfirm() {
        Notifications.unregister(item);
      }

      @Override
      protected void showParent() {
        showTo(player);
      }
    }.displayTo(getViewer());
  }

  @Override
  protected String[] getInfo() {
    return MENU_INFORMATION;
  }
}
