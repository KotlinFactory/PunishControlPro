package org.mineacademy.punishcontrol.spigot.menu.browser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import lombok.val;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.punishcontrol.core.injector.annotations.Localizable;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.punish.Punish;
import org.mineacademy.punishcontrol.core.setting.Replacer;
import org.mineacademy.punishcontrol.core.settings.Settings;
import org.mineacademy.punishcontrol.spigot.menu.AbstractConfirmMenu;
import org.mineacademy.punishcontrol.spigot.util.ItemStacks;

@Localizable
public abstract class AbstractPunishBrowser extends AbstractBrowser<Punish> {

  // ----------------------------------------------------------------------------------------------------
  // Localization
  // ----------------------------------------------------------------------------------------------------

  @Localizable("Menu.Proxy.Menu.Punish.Menu_Information")
  private static String[] MENU_INFORMATION = {"View punishes"};
  @NonNls
  @Localizable("Menu.Proxy.Punish.Title")
  private static String BROWSE_PUNISHES = "Browse Punishes";
  @NonNls
  @Localizable("Menu.Proxy.Punish.Remove")
  private static String RIGHT_CLICK_TO_REMOVE = "Right-Click to remove";
  @NonNls @Localizable("Parts.Removed")
  private static String REMOVED = "Removed";
  @Localizable("Menu.Proxy.Punish.Already_Removed")
  private static String PUNISH_IS_ALREADY_REMOVED = "Punish is already removed";

  @Localizable("Menu.Proxy.Punish.Lore")
  private static Replacer LORE_REPLACER = Replacer
      .of(
          "",
          "&6Target: &7{target}",
          "&6Reason: &7{reason}",
          "&6Creation: &7{creation}",
          "&6Duration: &7{duration}",
          "&6End: &7{end}",
          "&6Creator: &7{creator}",
          "&7Right click to remove");

  // ----------------------------------------------------------------------------------------------------
  // Fields & constructor
  // ----------------------------------------------------------------------------------------------------

  protected final PlayerProvider playerProvider;

  protected AbstractPunishBrowser(
      final Menu parent,
      final PlayerProvider playerProvider,
      final Collection<Punish> content) {
    super(parent, content);
    this.playerProvider = playerProvider;
    setTitle("&7" + BROWSE_PUNISHES);
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
            ? "&c" + REMOVED
            : Settings.Advanced.formatDate(punish.getEndTime());

    LORE_REPLACER.find("target", "reason", "creation", "duration", "end", "creator");
    LORE_REPLACER.replace(
        playerProvider.findName(punish.target()).orElse("unknown"),
        punish.reason(),
        Settings.Advanced.formatDate(punish.creation()),
        punish.punishDuration().toString(),
        end,
        playerProvider.findName(punish.creator()).orElse("unknown"));

    final List<String> lores = new ArrayList<>(
        Arrays.asList(LORE_REPLACER.replacedMessage()));

    if (loresToAdd(punish) != null)
      lores.addAll(loresToAdd(punish));
    builder.lores(lores);

    if (!punish.isOld())
      lores.add("&6" + RIGHT_CLICK_TO_REMOVE);

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
    return MENU_INFORMATION;
  }

  @Override
  protected final void onPageClick(
      final Player player,
      final Punish punish,
      final ClickType click) {

    if (!click.isRightClick())
      return;

    if (punish.isOld()) {
      animateTitle("&c" + PUNISH_IS_ALREADY_REMOVED);
      return;
    }

    new AbstractConfirmMenu(this) {
      @Override
      public void onConfirm() {
        async(() -> Providers.storageProvider().removePunish(punish));
      }

      @Override
      protected void showParent() {
        redrawForPlayer(player);
      }
    }.displayTo(player);
  }
}
