package org.mineacademy.punishcontrol.proxy.menu;

import de.exceptionflug.mccommons.inventories.api.CallResult;
import de.exceptionflug.mccommons.inventories.api.ClickType;
import de.exceptionflug.protocolize.items.ItemStack;
import de.exceptionflug.protocolize.items.ItemType;
import java.util.concurrent.TimeUnit;
import lombok.NonNull;
import org.mineacademy.burst.item.Item;
import org.mineacademy.burst.menu.Menu;
import org.mineacademy.punishcontrol.core.settings.Localization.Time;
import org.mineacademy.punishcontrol.core.settings.Settings;
import org.mineacademy.punishcontrol.core.util.TimeUtil;

public abstract class AbstractDurationChooser extends Menu {


  public static final int SIZE = 9 * 5;
  public static final int YEAR_SLOT = 0;
  public static final int PERMANENT_SLOT = 8;
  public static final int MONTH_SLOT = 9;
  public static final int DAY_SLOT = 9 * 3;
  public static final int HOUR_SLOT = 9 * 4;
  public static final int CLOCK_SLOT = 26;

  private ItemStack expirationClock;

  protected long ms;


  public AbstractDurationChooser(@NonNull final Menu parent) {
    this(parent, 0);
  }

  public AbstractDurationChooser(
      @NonNull final Menu parent,
      final long ms) {
    super("DurationChooser", parent, SIZE);
    this.ms = ms;
    setTitle(TimeUtil.formatMenuDate(ms));

    //Initial update
    updateClock();
  }

  protected abstract void confirm();

  // ----------------------------------------------------------------------------------------------------
  // Overridden methods from OnePageProxyInventoryWrapper
  // ----------------------------------------------------------------------------------------------------

  @Override
  public final void updateInventory() {
    super.updateInventory();
    set(CLOCK_SLOT, expirationClock, "noAction");

    set(
        Item
            .of(ItemType.CLOCK)
            .name("&6Expiration")
            .lore(
                "&7This punish will",
                "&7Expire on",
                "&7" + Settings.Advanced.formatDate(System.currentTimeMillis() + ms))
            .slot(CLOCK_SLOT)
            .actionHandler("Clock")
    );

    set(
        Item
            .of(Time.YEAR.hash())
            .name("&6" + Time.YEAR.localized())
            .lore(
                "&7Left-Click to add",
                "&7Right-Click to remove")
            .slot(YEAR_SLOT)
            .actionHandler("Year")
    );

    set(
        Item
            .of(Time.MONTH.hash())
            .name("&6" + Time.MONTH.localized())
            .lore(
                "&7Left-Click to add",
                "&7Right-Click to remove")
            .slot(MONTH_SLOT)
            .actionHandler("Month")
    );

    set(
        Item
            .of(Time.DAY.hash())
            .name("&6" + Time.DAY.localized())
            .lore(
                "&7Left-Click to add",
                "&7Right-Click to remove")
            .slot(DAY_SLOT)
            .actionHandler("Day")
    );

    set(
        Item
            .of(Time.HOUR.hash())
            .name("&6" + Time.HOUR.localized())
            .lore(
                "&7Left-Click to add",
                "&7Right-Click to remove")
            .slot(HOUR_SLOT)
            .actionHandler("Hour")
    );

    set(
        Item
            .of(ItemType.REDSTONE_BLOCK)
            .name("&4Make permanent")
            .lore("")
            .actionHandler("Perma")
    );
  }

  @Override
  public void registerActionHandlers() {
    registerActionHandler("Perma", (perma) -> {
      makePermanent();
      return CallResult.DENY_GRABBING;
    });

    registerActionHandler("Year", (year) -> {
      addOrRemoveYear(year.getClickType());
      return CallResult.DENY_GRABBING;
    });

    registerActionHandler("Month", (month) -> {
      addOrRemoveMonth(month.getClickType());
      return CallResult.DENY_GRABBING;
    });

    registerActionHandler("Day", (day) -> {
      addOrRemoveDay(day.getClickType());
      return CallResult.DENY_GRABBING;
    });

    registerActionHandler("Hour", (hour) -> {
      addOrRemoveHour(hour.getClickType());
      return CallResult.DENY_GRABBING;
    });
  }

  // ----------------------------------------------------------------------------------------------------
  // Internal methods
  // ----------------------------------------------------------------------------------------------------

  private boolean isPermanent() {
    return ms == -1;
  }

  private void makePermanent() {
    if (isPermanent()) {
      ms = 1000;
      animateTitle("&8Reset duration");
      return;
    }

    ms = -1;
    setTitle("&cPermanent");
    animateTitle("&7Made the punishment permanent");
    updateTitle();
  }

  private void normalizeIfNeeded() {
    if (!isPermanent()) {
      return;
    }
    ms = 0;
  }

  private void updateClock() {
    laterAsync(this::build, 20);
  }

  private void updateTitle() {
    if (ms == -1) {
      setTitle("&cPermanent");
    } else {
      setTitle(TimeUtil.formatMenuDate(ms));
    }
    updateClock();
  }

  private void addOrRemoveYear(final ClickType clickType) {
    normalizeIfNeeded();
    if (clickType == de.exceptionflug.mccommons.inventories.api.ClickType.LEFT_CLICK) {
      ms += TimeUnit.DAYS.toMillis(1) * 365;
      setTitle(TimeUtil.formatMenuDate(ms));
      animateTitle("&8Added 1 year");
    } else {
      ms -= TimeUnit.DAYS.toMillis(1) * 365;
      setTitle(TimeUtil.formatMenuDate(ms));
      animateTitle("&8Removed 1 year");
    }
    updateClock();
  }

  private void addOrRemoveMonth(final ClickType clickType) {
    normalizeIfNeeded();

    if (clickType == ClickType.LEFT_CLICK) {
      ms += TimeUnit.DAYS.toMillis(1) * 30;
      setTitle(TimeUtil.formatMenuDate(ms));
      animateTitle("&8Added 1 month");
    } else {
      ms -= TimeUnit.DAYS.toMillis(1) * 30;
      setTitle(TimeUtil.formatMenuDate(ms));
      animateTitle("&8Removed 1 month");
    }
    updateClock();
  }

  private void addOrRemoveDay(final ClickType clickType) {
    normalizeIfNeeded();

    if (clickType == ClickType.LEFT_CLICK) {
      ms += TimeUnit.DAYS.toMillis(1);
      setTitle(TimeUtil.formatMenuDate(ms));
      animateTitle("&8Added 1 day");
    } else {
      ms -= TimeUnit.DAYS.toMillis(1);
      setTitle(TimeUtil.formatMenuDate(ms));
      animateTitle("&8Removed 1 day");
    }
    updateClock();
  }

  private void addOrRemoveHour(final ClickType clickType) {
    normalizeIfNeeded();

    if (clickType == ClickType.LEFT_CLICK) {
      ms += TimeUnit.HOURS.toMillis(1);
      setTitle(TimeUtil.formatMenuDate(ms));
      animateTitle("&8Added 1 hour");
    } else {
      ms -= TimeUnit.HOURS.toMillis(1);
      setTitle(TimeUtil.formatMenuDate(ms));
      animateTitle("&8Removed 1 hour");
    }
    updateClock();
  }
}

