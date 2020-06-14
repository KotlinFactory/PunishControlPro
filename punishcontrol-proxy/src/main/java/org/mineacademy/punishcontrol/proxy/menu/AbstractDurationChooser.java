package org.mineacademy.punishcontrol.proxy.menu;

import de.exceptionflug.mccommons.inventories.api.CallResult;
import de.exceptionflug.mccommons.inventories.api.ClickType;
import de.exceptionflug.protocolize.items.ItemType;
import java.util.concurrent.TimeUnit;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.mineacademy.burst.item.Item;
import org.mineacademy.burst.menu.AbstractMenu;
import org.mineacademy.burst.menu.BurstMenu;
import org.mineacademy.punishcontrol.core.injector.annotations.Localizable;
import org.mineacademy.punishcontrol.core.settings.ItemSettings;
import org.mineacademy.punishcontrol.core.settings.Localization.Time;
import org.mineacademy.punishcontrol.core.settings.Settings;
import org.mineacademy.punishcontrol.core.util.TimeUtil;
import org.mineacademy.punishcontrol.proxy.conversations.DurationChooseConversation;

@Localizable
@Accessors(fluent = true)
public abstract class AbstractDurationChooser extends AbstractMenu {


  private static final int SIZE = 9 * 5;

  // ----------------------------------------------------------------------------------------------------
  // Button
  // ----------------------------------------------------------------------------------------------------
  private static final int PERMANENT_SLOT = 8;
  private static final int YEAR_SLOT = 0;
  private static final int MONTH_SLOT = 9;
  private static final int DAY_SLOT = 9 * 3;
  private static final int HOUR_SLOT = 9 * 4;
  private static final int CLOCK_SLOT = 26;
  private static final int APPLY_SLOT = 22;
  // ----------------------------------------------------------------------------------------------------
  // Localization
  // ----------------------------------------------------------------------------------------------------
  @Localizable(value = "Menu.Choose_Duration")
  private static String CHOOSE_DURATION = "Choose duration";
  @Setter
  protected long ms;

  public AbstractDurationChooser(@NonNull final BurstMenu parent) {
    this(parent, 0);
  }

  public AbstractDurationChooser(
      @NonNull final BurstMenu parent,
      final long ms) {
    super("DurationChooser", parent, SIZE);
    this.ms = ms;
    setTitle("&8" + CHOOSE_DURATION);

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
    set(
        Item
            .of(ItemSettings.APPLY_ITEM.itemType())
            .name("&aApply")
            .lore("")
            .slot(APPLY_SLOT)
            .actionHandler("Apply")
    );

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
                "&7Left click to add",
                "&7Right click to remove")
            .slot(YEAR_SLOT)
            .actionHandler("Year")
    );

    set(
        Item
            .of(Time.MONTH.hash())
            .name("&6" + Time.MONTH.localized())
            .lore(
                "&7Left click to add",
                "&7Right click to remove")
            .slot(MONTH_SLOT)
            .actionHandler("Month")
    );

    set(
        Item
            .of(Time.DAY.hash())
            .name("&6" + Time.DAY.localized())
            .lore(
                "&7Left click to add",
                "&7Right click to remove")
            .slot(DAY_SLOT)
            .actionHandler("Day")
    );

    set(
        Item
            .of(Time.HOUR.hash())
            .name("&6" + Time.HOUR.localized())
            .lore(
                "&7Left click to add",
                "&7Right click to remove")
            .slot(HOUR_SLOT)
            .actionHandler("Hour")
    );

    set(
        Item
            .of(ItemType.REDSTONE_BLOCK)
            .name("&4Make permanent")
            .lore("")
            .actionHandler("Perma")
            .slot(PERMANENT_SLOT)
    );
  }

  @Override
  public void registerActionHandlers() {

    registerActionHandler("Clock", (clock -> {
      DurationChooseConversation.create(getPlayer(), this).start();
      return CallResult.DENY_GRABBING;
    }));

    registerActionHandler("Apply", (apply -> {
      confirm();
      getParent().reDisplay();
      return CallResult.DENY_GRABBING;
    }));

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
    if (ms < 0 && !isPermanent()) {
      ms = 0;
    }
  }

  private void updateClock() {
    normalizeIfNeeded();
    if (getViewer().isPresent()) {
      build();
    }
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
    if (clickType == de.exceptionflug.mccommons.inventories.api.ClickType.LEFT_CLICK) {
      ms += TimeUnit.DAYS.toMillis(1) * 365;
    } else {
      ms -= TimeUnit.DAYS.toMillis(1) * 365;
    }
    setTitle(TimeUtil.formatMenuDate(ms));
    updateClock();
  }

  private void addOrRemoveMonth(final ClickType clickType) {

    if (clickType == ClickType.LEFT_CLICK) {
      ms += TimeUnit.DAYS.toMillis(1) * 30;
    } else {
      ms -= TimeUnit.DAYS.toMillis(1) * 30;
    }
    setTitle(TimeUtil.formatMenuDate(ms));
    updateClock();
  }

  private void addOrRemoveDay(final ClickType clickType) {
    if (clickType == ClickType.LEFT_CLICK) {
      ms += TimeUnit.DAYS.toMillis(1);
    } else {
      ms -= TimeUnit.DAYS.toMillis(1);
    }
    setTitle(TimeUtil.formatMenuDate(ms));
    updateClock();
  }

  private void addOrRemoveHour(final ClickType clickType) {

    if (clickType == ClickType.LEFT_CLICK) {
      ms += TimeUnit.HOURS.toMillis(1);
    } else {
      ms -= TimeUnit.HOURS.toMillis(1);
    }
    setTitle(TimeUtil.formatMenuDate(ms));
    normalizeIfNeeded();
    updateClock();
  }

  @Override
  public void reDisplay() {
    throw new AbstractMethodError("Not implemented");
  }
}

