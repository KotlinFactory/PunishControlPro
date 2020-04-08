package org.mineacademy.punishcontrol.spigot.menus;

import java.util.Arrays;
import javax.inject.Inject;
import lombok.NonNull;
import lombok.val;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.debug.LagCatcher;
import org.mineacademy.fo.menu.MenuPagged;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.model.Replacer;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.punish.Punish;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;
import org.mineacademy.punishcontrol.spigot.DaggerSpigotComponent;
import org.mineacademy.punishcontrol.spigot.Scheduler;
import org.mineacademy.punishcontrol.spigot.settings.Settings;
import org.mineacademy.punishcontrol.spigot.util.ItemStacks;

// TODO Punishes from Type/Player/This/That
public final class PunishBrowserMenu extends MenuPagged<Punish> {

  private final StorageProvider storageProvider;
  private final PlayerProvider playerProvider;

  @Inject
  public PunishBrowserMenu
      (final MainMenu parent,
      final PlayerProvider playerProvider,
      final StorageProvider storageProvider) {
    super(9 * 5, parent, storageProvider.listPunishes());
    this.storageProvider = storageProvider;
    this.playerProvider = playerProvider;
    setTitle("&7Browse Punishes");
  }

  //Showing up while saving performance:)
  public static void showTo(@NonNull final Player player) {
    Scheduler.runAsync(() -> {
      LagCatcher.start("async-show-up-punish-browser");
      final val browser = DaggerSpigotComponent.create().punishBrowserMenu();
      Scheduler.runSync(() -> browser.displayTo(player));
      LagCatcher.end("async-show-up-punish-browser");
    });
  }

  @Override
  protected String[] getInfo() {
    return new String[0];
  }

  @Override
  protected ItemStack convertToItemStack(final Punish punish) {
    CompMaterial material = null;

    switch (punish.punishType()) {
      case BAN:
        material = CompMaterial.OAK_DOOR;
        break;
      case MUTE:
        material = CompMaterial.PAPER;
        break;
      case WARN:
        material = CompMaterial.ORANGE_DYE;
        break;
    }

    final val builder = ItemCreator.of(material, "&8"+ punish.punishType().localized());
    //Due to the, we have to set the item for the Warn manually.
    if (material == CompMaterial.ORANGE_DYE) {
      builder.item(ItemStacks.yellowDye());
    }

    final String end =
        punish.removed() ? "&cRemoved" : Settings.Advanced.formatDate(punish.getEndTime());
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
        playerProvider.getName(punish.target()),
        punish.reason(),
        Settings.Advanced.formatDate(punish.creation()),
        punish.punishDuration().toString(),
        end,
        playerProvider.getName(punish.creator()));

    builder.lores(Arrays.asList(lore.getReplacedMessage()));
    return builder.build().make();
  }

  /**
   * Called automatically when an item is clicked
   *
   * @param player the player who clicked
   * @param item   the clicked item
   * @param click
   */
  @Override
  protected void onPageClick(
      final Player player, final Punish item, final ClickType click) {
  }
}
