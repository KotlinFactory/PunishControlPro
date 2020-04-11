package org.mineacademy.punishcontrol.spigot.menu;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.punishcontrol.core.settings.Localization.Time;
import org.mineacademy.punishcontrol.core.settings.Settings;
import org.mineacademy.punishcontrol.core.util.TimeUtil;
import org.mineacademy.punishcontrol.spigot.util.Schedulable;

public abstract class AbstractDurationChooser
    extends Menu
    implements Schedulable {


  public static final int SIZE = 9 * 5;

  private final Button year, month, day, hour;

  private final Button makePermanent;

  private final Button confirm;
  private ItemStack expirationClock;

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
    setTitle(TimeUtil.formatMenuDate(ms));

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
            .fromCustomHash(Time.YEAR.hash())
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
            .fromCustomHash(Time.MONTH.hash())
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
            .fromCustomHash(Time.DAY.hash())
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
            .fromCustomHash(Time.HOUR.hash())
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
        System.out.println("permanent!!");
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
        System.out.println("confirmed!");
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

    //Initial update
    updateClock();

  }


  protected abstract void confirm();

  // ----------------------------------------------------------------------------------------------------
  // Overridden methods from Menu
  // ----------------------------------------------------------------------------------------------------

  @Override
  public final ItemStack getItemAt(final int slot) {
    if (slot == 0) {
      return year.getItem();
    }
    if (slot == 9) {
      return month.getItem();
    }

    if (slot == 9 * 3) {
      return day.getItem();
    }

    if (slot == 9 * 4) {
      return hour.getItem();
    }

    if (slot == 8) {
      return makePermanent.getItem();
    }

    if (slot == 22) {
      return confirm.getItem();
    }

    if (slot == 26) {
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
    if (slot == 0) {
      addOrRemoveYear(click);
    }

    if (slot == 8) {
      System.out.println("Made permanent?");
      makePermanent();
    }

    if (slot == 9) {
      addOrRemoveMonth(click);
    }

    if (slot == 9 * 3) {
      addOrRemoveDay(click);
    }

    if (slot == 9 * 4) {
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
    laterAsync(() -> {
      final String expiration = isPermanent()
          ? "&cnever &7- permanent"
          : "&7on " + Settings.Advanced
              .formatDate(System.currentTimeMillis() + ms);

      expirationClock = ItemCreator
          .of(CompMaterial.CLOCK,
              "&6Expiration",
              "&7This punish ",
              "&7will expire",
              expiration)
          .build()
          .make();
      if (getViewer() != null) {
        redraw();
      }
    }, 20);
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
    if (clickType.isLeftClick()) {
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

    if (clickType.isLeftClick()) {
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

    if (clickType.isLeftClick()) {
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

    if (clickType.isLeftClick()) {
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

