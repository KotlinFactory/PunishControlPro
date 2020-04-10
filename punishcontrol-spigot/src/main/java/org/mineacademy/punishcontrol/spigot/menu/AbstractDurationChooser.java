package org.mineacademy.punishcontrol.spigot.menu;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.punishcontrol.core.settings.Localization.Time;
import org.mineacademy.punishcontrol.core.settings.Settings;
import org.mineacademy.punishcontrol.core.util.TimeUtil;

public abstract class AbstractDurationChooser extends Menu {


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
    setTitle("&8Choose an duration");
    this.ms = ms;

    year = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player, final Menu menu, final ClickType click) {

      }

      @Override
      public ItemStack getItem() {
        return ItemCreator
            .fromCustomHash(Time.YEAR.hash())
            .name("&6" + Time.YEAR.localized())
            .lores(Arrays.asList("&7Left-Click to add", "&7Right-Click to remove"))
            .build()
            .makeMenuTool();
      }
    };

    month = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player, final Menu menu, final ClickType click) {

      }

      @Override
      public ItemStack getItem() {
        return ItemCreator
            .fromCustomHash(Time.MONTH.hash())
            .name("&6" + Time.MONTH.localized())
            .lores(Arrays.asList("&7Left-Click to add", "&7Right-Click to remove"))
            .build()
            .makeMenuTool();
      }
    };

    day = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player, final Menu menu, final ClickType click) {

      }

      @Override
      public ItemStack getItem() {
        return ItemCreator
            .fromCustomHash(Time.DAY.hash())
            .name("&6" + Time.DAY.localized())
            .lores(Arrays.asList("&7Left-Click to add", "&7Right-Click to remove"))
            .build()
            .makeMenuTool();
      }
    };

    hour = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player, final Menu menu, final ClickType click) {

      }

      @Override
      public ItemStack getItem() {
        return ItemCreator
            .fromCustomHash(Time.HOUR.hash())
            .name("&6" + Time.HOUR.localized())
            .lores(Arrays.asList("&7Left-Click to add", "&7Right-Click to remove"))
            .build()
            .makeMenuTool();
      }
    };

    makePermanent = ItemCreator
        .of(CompMaterial.REDSTONE_BLOCK, "&4Make permanent", "")
        .build()
        .makeButton();

    confirm = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player, final Menu menu, final ClickType click) {
//        confirm();
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

  protected final void addOrRemoveYear(final ClickType clickType) {
    if (clickType.isLeftClick()) {
      ms += TimeUnit.DAYS.toMillis(1) * 365;
      animateTitle("&8Added 1 year");
    } else {
      ms -= TimeUnit.DAYS.toMillis(1) * 365;
      animateTitle("&8Removed 1 year");

    }
  }

  protected final void makePermanent() {
    ms = -1;
    updateTitle();
    animateTitle("&7Made the punishment permanent");
  }

  private void updateTitle() {
    if (ms == -1) {
      setTitle("&cPermanent");
    } else {
      setTitle(TimeUtil.formatTimeDays(TimeUnit.MILLISECONDS.toSeconds(ms)));
    }
    updateClock();
  }

  protected final void addOrRemoveMonth(final ClickType clickType) {
    if (clickType.isLeftClick()) {
      ms += TimeUnit.DAYS.toMillis(1) * 30;
      updateTitle();
      animateTitle("&8Added 1 month");

    } else {
      ms -= TimeUnit.DAYS.toMillis(1) * 30;
      updateTitle();
      animateTitle("&8Removed 1 month");

    }
  }

  protected final void addOrRemoveDay(final ClickType clickType) {
    if (clickType.isLeftClick()) {
      ms += TimeUnit.DAYS.toMillis(1);
      updateTitle();

      animateTitle("&8Added 1 day");

    } else {
      ms -= TimeUnit.DAYS.toMillis(1);
      updateTitle();
      animateTitle("&8Removed 1 day");
    }
  }

  protected final void addOrRemoveHour(final ClickType clickType) {
    if (clickType.isLeftClick()) {
      ms += TimeUnit.HOURS.toMillis(1);
      updateTitle();
      animateTitle("&8Added 1 hour");
    } else {
      ms -= TimeUnit.HOURS.toMillis(1);
      updateTitle();
      animateTitle("&8Removed 1 hour");
    }
  }

  protected void updateClock() {
    expirationClock = ItemCreator
        .of(CompMaterial.CLOCK,
            "&6Expiration",
            "&7This punish will",
            "&7expire on", "&7" + Settings.Advanced
                .formatDate(System.currentTimeMillis() + ms))
        .build()
        .make();
    if (getViewer() != null) {
      restartMenu();
    }
  }


  protected abstract void confirm();


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

  @Override
  protected final void onMenuClick(final Player player, final int slot,
      final ItemStack clicked) {

  }
}
