package org.mineacademy.punishcontrol.spigot.menus.browsers;

import lombok.NonNull;
import lombok.val;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.punishcontrol.core.notification.Notification;
import org.mineacademy.punishcontrol.core.notification.Notifications;
import org.mineacademy.punishcontrol.spigot.DaggerSpigotComponent;
import org.mineacademy.punishcontrol.spigot.Scheduler;
import org.mineacademy.punishcontrol.spigot.menu.AbstractConfirmMenu;
import org.mineacademy.punishcontrol.spigot.menu.browser.AbstractBrowser;

public final class NotificationBrowser extends AbstractBrowser<Notification> {

  public static void showTo(@NonNull final Player player) {
    Scheduler.runAsync(() -> new NotificationBrowser().displayTo(player));
  }

  private NotificationBrowser() {
    super(
        DaggerSpigotComponent.create().settingsBrowser(),
        Notifications.registeredNotifications());
    setTitle("&8Notifications");
  }

  @Override
  protected ItemStack convertToItemStack(final Notification item) {
    final CompMaterial type = item.itemType();
    final val text = Common.colorize(item.text());
    text.add(" ");
    text.add("&6Right click to dismiss");
    return ItemCreator
        .of(type)
        .name(Common.colorize(item.name()))
        .lores(text)
        .build()
        .make();
  }

  @Override
  protected void onPageClick(final Player player, final Notification item, final ClickType click) {
    if (!click.isRightClick()) {
      return;
    }

    new AbstractConfirmMenu(this){
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
    return new String[]{
        "&7All current notifications",
        "&7are listed here"
    };
  }
}
