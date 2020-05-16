package org.mineacademy.punishcontrol.proxy.menus.browsers;

import de.exceptionflug.mccommons.inventories.api.ClickType;
import de.exceptionflug.protocolize.items.ItemStack;
import de.exceptionflug.protocolize.items.ItemType;
import lombok.NonNull;
import lombok.val;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.mineacademy.bfo.Common;
import org.mineacademy.burst.item.Item;
import org.mineacademy.burst.menu.AbstractBrowser;
import org.mineacademy.burst.util.Scheduler;
import org.mineacademy.punishcontrol.core.notification.Notification;
import org.mineacademy.punishcontrol.core.notification.Notifications;
import org.mineacademy.punishcontrol.proxy.DaggerProxyComponent;
import org.mineacademy.punishcontrol.proxy.menu.AbstractConfirmMenu;


public final class NotificationBrowser extends AbstractBrowser<Notification> {

  public static void showTo(@NonNull final ProxiedPlayer player) {
    Scheduler.runAsync(() -> new NotificationBrowser().displayTo(player));
  }

  private NotificationBrowser() {
    super(
        "NotificationBrowser",
        DaggerProxyComponent.create().settingsBrowser(),
        Notifications.registeredNotifications());
    setTitle("&8Notifications");
  }

  @Override
  protected void onClick(final ClickType clickType, final Notification notification) {
    if (clickType != ClickType.RIGHT_CLICK){
      return;
    }
    new AbstractConfirmMenu(this) {
      @Override
      public void onConfirm() {
        Notifications.unregister(notification);
      }
    }.displayTo(getPlayer());
  }

  @Override
  public ItemStack convertToItemStack(final Notification item) {
    final ItemType type = item.itemType();
    final val text = Common.colorize(item.text());
    text.add(" ");
    text.add("&6Right click to dismiss");
    return Item
        .of(type)
        .name(Common.colorize(item.name()))
        .lore(text)
        .build();
  }

  @Override
  public void reDisplay() {
    showTo(getPlayer());
  }

  @Override
  protected String[] getInfo() {
    return new String[]{
        "&7All current notifications",
        "&7are listed here"
    };
  }
}

