package org.mineacademy.punishcontrol.spigot.menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.NonNull;
import lombok.val;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.debug.Debugger;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.model.Replacer;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.punish.Punish;
import org.mineacademy.punishcontrol.core.settings.Settings;
import org.mineacademy.punishcontrol.spigot.util.ItemStacks;
import org.mineacademy.punishcontrol.spigot.util.Schedulable;

public class AbstractPunishBrowser extends AbstractBrowser<Punish> {

  protected final PlayerProvider playerProvider;

  protected AbstractPunishBrowser(
      final Menu parent,
      final PlayerProvider playerProvider,
      final List<Punish> content) {
    super(parent, content);
    this.playerProvider = playerProvider;
    setTitle("&7Browse Punishes");
  }

  // ----------------------------------------------------------------------------------------------------
  // Overridden methods from Menu
  // ----------------------------------------------------------------------------------------------------

  @Override
  protected final ItemStack convertToItemStack(final Punish punish) {

    final val builder = ItemCreator
        .of(ItemStacks.forPunishType(punish.punishType()));
    builder.name("&8" + punish.punishType().localized());

    final String end =
        punish.isOld()
            ? "&cRemoved"
            : Settings.Advanced.formatDate(punish.getEndTime());
    final Replacer lore = Replacer.of(
        "",
        "&6Target: &7{target}",
        "&6Reason: &7{reason}",
        "&6Creation: &7{creation}",
        "&6Duration: &7{duration}",
        "&6End: &7{end}",
        "&6Creator: &7{creator}"
    );

    lore.find("target", "reason", "creation", "duration", "end", "creator");
    lore.replace(
        playerProvider.findNameUnsafe(punish.target()),
        punish.reason(),
        Settings.Advanced.formatDate(punish.creation()),
        punish.punishDuration().toString(),
        end,
        playerProvider.findNameUnsafe(punish.creator()));

    final List<String> lores = new ArrayList<>(
        Arrays.asList(lore.getReplacedMessage()));

    if (loresToAdd(punish) != null) {
      lores.addAll(loresToAdd(punish));
    }
    builder.lores(lores);

    if (!punish.isOld()) {
      lores.add("&6Right-Click to remove");
    }

    return builder.build().make();
  }

  // ----------------------------------------------------------------------------------------------------
  // Methods that might be overridden
  // ----------------------------------------------------------------------------------------------------

  @Nullable
  protected List<String> loresToAdd(final Punish punish) {
    return null;
  }

  @Override
  protected String[] getInfo() {
    return new String[]{"View punishes"};
  }

  @Override
  protected final void onPageClick(
      final Player player, final Punish item, final ClickType click) {

    if (!click.isRightClick()) {
      return;
    }

    if (item.isOld()) {
      animateTitle("&cPunish is already removed");
      return;
    }

    ConfirmMenu.create(this, item).displayTo(player);
  }

  static final class ConfirmMenu extends Menu implements Schedulable {

    public static final int CONFIRM_BUTTON_SLOT = 4;
    private final Punish punish;

    public static ConfirmMenu create(
        @NonNull final Menu parent,
        @NonNull final Punish punish) {
      return new ConfirmMenu(parent, punish);
    }

    ConfirmMenu(final Menu parent, final Punish punish) {
      super(parent);
      this.punish = punish;
      setSize(9 * 2);
      setTitle("&8Confirm removal");
    }


    @Override
    public ItemStack getItemAt(final int slot) {
      if (slot == CONFIRM_BUTTON_SLOT) {
        return ItemCreator
            .of(CompMaterial.EMERALD_BLOCK)
            .name("&aConfirm")
            .lores(Arrays.asList(
                "",
                "&7Confirm removal"
            ))
            .build()
            .makeMenuTool();
      }

      return null;
    }


    @Override
    protected void onMenuClick(
        final Player player,
        final int slot,
        final ItemStack clicked) {

      if (slot != CONFIRM_BUTTON_SLOT) {
        return;
      }
      //TODO inject!
      async(() -> {
        try {
          Providers.storageProvider().removePunish(punish);
        } catch (final Throwable throwable) {
          sync(() -> Debugger.saveError(
              throwable,
              "ConfirmMenu: Exception while removing punish",
              "Have you altered the data?"));
        }
        getParent().displayTo(getViewer());
        getParent().restartMenu();
      });
    }

    @Override
    protected String[] getInfo() {
      return new String[]{"Confirm removal", "of punish"};
    }
  }
}
