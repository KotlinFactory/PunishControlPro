package org.mineacademy.punishcontrol.spigot.menu;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.PlayerUtil;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.InventoryDrawer;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.punishcontrol.core.settings.Localization.Time;
import org.mineacademy.punishcontrol.core.settings.Settings;
import org.mineacademy.punishcontrol.core.util.TimeUtil;
import org.mineacademy.punishcontrol.spigot.conversations.DurationChooseConversation;
import org.mineacademy.punishcontrol.spigot.util.Schedulable;

@Accessors(fluent = true)
public abstract class AbstractDurationChooser
    extends Menu
    implements Schedulable {


  public static final int SIZE = 9 * 5;
  public static final int EXPIRATION_CLOCK_SLOT = 26;
  public static final int YEAR_SLOT = 0;
  public static final int PERMANENT_SLOT = 8;
  public static final int MONTH_SLOT = 9;
  public static final int DAY_SLOT = 9 * 3;
  public static final int HOUR_SLOT = 9 * 4;
  public static final int CONFIRM_SLOT = 22;
  public static final int MAKE_PERMA_SLOT = 8;

  private final Button year, month, day, hour;

  private final Button makePermanent;

  private final Button confirm;
  private ItemStack expirationClock;

  @Setter
  protected long ms;


  public AbstractDurationChooser(@NonNull final Menu parent) {
    this(parent, 0);
  }

  public AbstractDurationChooser(
      @NonNull final Menu parent,
      final long ms) {
    super(parent);
    setSize(SIZE);
    this.ms = ms;
    changeTitle("&8Choose duration");

    expirationClock = ItemCreator
        .of(CompMaterial.CLOCK,
            "&6Expiration",
            "&7This punish will",
            "&7expire on", "&7" + Settings.Advanced
                .formatDate(System.currentTimeMillis() + ms))
        .build()
        .make();

    year = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player, final Menu menu, final ClickType click) {
        addOrRemoveYear(click);
      }

      @Override
      public ItemStack getItem() {
        return ItemCreator
            .ofSkullHash(Time.YEAR.hash())
            .name("&6" + Time.YEAR.localized())
            .lores(
                Arrays.asList("&7Left-Click to add", "&7Right-Click to remove"))
            .build()
            .makeMenuTool();
      }
    };

    month = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player, final Menu menu, final ClickType click) {

        addOrRemoveMonth(click);
      }

      @Override
      public ItemStack getItem() {
        return ItemCreator
            .ofSkullHash(Time.MONTH.hash())
            .name("&6" + Time.MONTH.localized())
            .lores(
                Arrays.asList("&7Left-Click to add", "&7Right-Click to remove"))
            .build()
            .makeMenuTool();
      }
    };

    day = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player, final Menu menu, final ClickType click) {
        addOrRemoveDay(click);
      }

      @Override
      public ItemStack getItem() {
        return ItemCreator
            .ofSkullHash(Time.DAY.hash())
            .name("&6" + Time.DAY.localized())
            .lores(
                Arrays.asList("&7Left-Click to add", "&7Right-Click to remove"))
            .build()
            .makeMenuTool();
      }
    };

    hour = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player, final Menu menu, final ClickType click) {
        addOrRemoveHour(click);
      }

      @Override
      public ItemStack getItem() {
        return ItemCreator
            .ofSkullHash(Time.HOUR.hash())
            .name("&6" + Time.HOUR.localized())
            .lores(
                Arrays.asList("&7Left-Click to add", "&7Right-Click to remove"))
            .build()
            .makeMenuTool();
      }
    };

    makePermanent = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player, final Menu menu, final ClickType click) {
        makePermanent();
      }

      @Override
      public ItemStack getItem() {
        return ItemCreator
            .of(CompMaterial.REDSTONE_BLOCK, "&4Make permanent", "")
            .build()
            .makeMenuTool();
      }
    };

    confirm = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player, final Menu menu, final ClickType click) {
        confirm();
      }

      @Override
      public ItemStack getItem() {
        return ItemCreator
            .of(CompMaterial.EMERALD,
                "&aConfirm",
                "&7Confirm the duration")
            .build()
            .makeMenuTool();
      }
    };
  }


  protected abstract void confirm();

  // ----------------------------------------------------------------------------------------------------
  // Overridden methods from Menu
  // ----------------------------------------------------------------------------------------------------


  @Override
  protected void onDisplay(final InventoryDrawer drawer) {
    updateClock();
    changeTitle("&8Select duration");
  }

  @Override
  public final ItemStack getItemAt(final int slot) {
    if (slot == AbstractDurationChooser.YEAR_SLOT) {
      return year.getItem();
    }
    if (slot == AbstractDurationChooser.MONTH_SLOT) {
      return month.getItem();
    }

    if (slot == AbstractDurationChooser.DAY_SLOT) {
      return day.getItem();
    }

    if (slot == AbstractDurationChooser.HOUR_SLOT) {
      return hour.getItem();
    }

    if (slot == MAKE_PERMA_SLOT) {
      return makePermanent.getItem();
    }

    if (slot == CONFIRM_SLOT) {
      return confirm.getItem();
    }

    if (slot == EXPIRATION_CLOCK_SLOT) {
      return expirationClock;
    }
    return null;
  }

  //Manually handle the clicks since custom skins are not working probably
  @Override
  protected void onMenuClick(final Player player, final int slot,
      final InventoryAction action, final ClickType click,
      final ItemStack cursor, final ItemStack clicked,
      final boolean cancelled) {
    super.onMenuClick(player, slot, action, click, cursor, clicked, cancelled);

    if (slot == EXPIRATION_CLOCK_SLOT) {
      DurationChooseConversation.create(this).start(getViewer());
    }

    if (slot == YEAR_SLOT) {
      addOrRemoveYear(click);
    }

    if (slot == PERMANENT_SLOT) {
      makePermanent();
    }

    if (slot == MONTH_SLOT) {
      addOrRemoveMonth(click);
    }

    if (slot == DAY_SLOT) {
      addOrRemoveDay(click);
    }

    if (slot == HOUR_SLOT) {
      addOrRemoveHour(click);
    }
  }

  // ----------------------------------------------------------------------------------------------------
  // Convenience methods
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
    changeTitle("&cPermanent");
    animateTitle("&7Made the punishment permanent");
    updateTitle();
  }

  private void normalizeIfNeeded() {
    if (ms < 0 && !isPermanent()) {
      ms = 0;
    }
  }


  public void updateClock() {
    laterAsync(() -> {
      final String expiration = isPermanent()
          ? "&cnever &7- permanent"
          : "&7on " + Settings.Advanced
              .formatDate(System.currentTimeMillis() + ms);

      expirationClock = ItemCreator
          .of(CompMaterial.CLOCK,
              "&6Expiration",
              "&7This punish ",
              "&7will expire on",
              "&7" + expiration)
          .build()
          .make();
      if (getViewer() != null) {
        later(this::redraw, 1);
      }
    }, 20);
  }

  private void updateTitle() {
    if (ms == -1) {
      changeTitle("&cPermanent");
    } else {
      changeTitle(TimeUtil.formatMenuDate(ms));
    }
    updateClock();
  }

  private void addOrRemoveYear(final ClickType clickType) {
    normalizeIfNeeded();
    if (clickType.isLeftClick()) {
      ms += TimeUnit.DAYS.toMillis(1) * 365;
    } else {
      ms -= TimeUnit.DAYS.toMillis(1) * 365;
    }
    changeTitle(TimeUtil.formatMenuDate(ms));

    updateClock();
  }

  private void addOrRemoveMonth(final ClickType clickType) {
    normalizeIfNeeded();

    if (clickType.isLeftClick()) {
      ms += TimeUnit.DAYS.toMillis(1) * 30;
    } else {
      ms -= TimeUnit.DAYS.toMillis(1) * 30;
    }
    changeTitle(TimeUtil.formatMenuDate(ms));
    updateClock();
  }

  private void addOrRemoveDay(final ClickType clickType) {
    normalizeIfNeeded();

    if (clickType.isLeftClick()) {
      ms += TimeUnit.DAYS.toMillis(1);
    } else {
      ms -= TimeUnit.DAYS.toMillis(1);
    }
    changeTitle(TimeUtil.formatMenuDate(ms));
    updateClock();
  }

  private void addOrRemoveHour(final ClickType clickType) {
    normalizeIfNeeded();

    if (clickType.isLeftClick()) {
      ms += TimeUnit.HOURS.toMillis(1);
      //      animateTitle("&8Added 1 hour");
    } else {
      ms -= TimeUnit.HOURS.toMillis(1);
      //      animateTitle("&8Removed 1 hour");
    }
    changeTitle(TimeUtil.formatMenuDate(ms));
    updateClock();
  }

  private void changeTitle(final String title) {
    setTitle(title);
    if (getViewer() != null) {
      PlayerUtil.updateInventoryTitle(getViewer(), title);
    }
  }
}

