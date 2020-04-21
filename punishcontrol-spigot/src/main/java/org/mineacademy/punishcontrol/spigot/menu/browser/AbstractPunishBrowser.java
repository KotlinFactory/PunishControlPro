package org.mineacademy.punishcontrol.spigot.menu.browser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import lombok.val;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.model.Replacer;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.punish.Punish;
import org.mineacademy.punishcontrol.core.settings.Settings;
import org.mineacademy.punishcontrol.spigot.menu.AbstractConfirmMenu;
import org.mineacademy.punishcontrol.spigot.util.ItemStacks;

public abstract class AbstractPunishBrowser extends AbstractBrowser<Punish> {

  protected final PlayerProvider playerProvider;

  protected AbstractPunishBrowser(
      final Menu parent,
      final PlayerProvider playerProvider,
      final Collection<Punish> content) {
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
        "&6Creator: &7{creator}",
        "&7Right click to remove"
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

  protected abstract void redrawForPlayer(final Player player);

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
      final Player player, final Punish punish, final ClickType click) {

    if (!click.isRightClick()) {
      return;
    }

    if (punish.isOld()) {
      animateTitle("&cPunish is already removed");
      return;
    }

    new AbstractConfirmMenu(this){
      @Override
      public void onConfirm() {
        Providers.storageProvider().removePunish(punish);
      }

      @Override
      protected void showParent() {
        redrawForPlayer(player);
      }
    }.displayTo(player);
  }
}
