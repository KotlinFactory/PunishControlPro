package org.mineacademy.punishcontrol.proxy.menus;

import de.exceptionflug.protocolize.items.ItemStack;
import java.util.UUID;
import javafx.scene.control.Button;
import lombok.NonNull;
import lombok.val;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.mineacademy.bfo.Players;
import org.mineacademy.bfo.settings.SimpleLocalization.Player;
import org.mineacademy.burst.item.Item;
import org.mineacademy.burst.menu.Menu;
import org.mineacademy.burst.util.Scheduler;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.providers.TextureProvider;
import org.mineacademy.punishcontrol.core.punish.PunishBuilder;
import org.mineacademy.punishcontrol.core.punish.PunishType;
import org.mineacademy.punishcontrol.core.settings.Localization;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;
import org.mineacademy.punishcontrol.proxy.DaggerProxyComponent;
import org.mineacademy.punishcontrol.proxy.ItemUtil;
import org.mineacademy.punishcontrol.proxy.menus.browsers.PlayerPunishBrowser;
import org.mineacademy.punishcontrol.proxy.menus.punish.PunishCreatorMenu;
import org.mineacademy.punishcontrol.proxy.menus.settings.PlayerSettingsMenu;

public final class ChooseActionMenu extends Menu {

  private static final int PLAYER_HEAD_SLOT = 13;

  private final Button punish;
  private final Button listPunishes;
  private final Button settings;
  private final Button kick;
  private final PlayerProvider playerProvider;
  private final TextureProvider textureProvider;
  private final StorageProvider storageProvider;
  private final UUID target;
  private final String targetName;

  public static void showTo(
      @NonNull final ProxiedPlayer player,
      @NonNull final UUID target) {
    Scheduler.runAsync(() -> {
      final val menu = create(target);
      menu.displayTo(player);
    });
  }

  public static ChooseActionMenu create(@NonNull final UUID target) {
    return new ChooseActionMenu(
        DaggerProxyComponent.create().menuMain(),
        Providers.playerProvider(),
        Providers.textureProvider(),
        Providers.storageProvider(),
        target);
  }


  public ChooseActionMenu(
      final MainMenu mainMenu,
      final PlayerProvider playerProvider,
      final TextureProvider textureProvider,
      final StorageProvider storageProvider,
      final UUID target) {
    super("ChooseAction", mainMenu, 9 * 3);
    this.playerProvider = playerProvider;
    this.textureProvider = textureProvider;
    this.storageProvider = storageProvider;
    this.target = target;
    targetName = playerProvider.findNameUnsafe(target);
    setTitle("§8Action for " + targetName);

    punish = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player, final Menu menu, final ClickType click) {

        PunishCreatorMenu.showTo(getViewer(),
            PunishBuilder
                .of(PunishType.BAN)
                .target(target));
      }

      @Override
      public ItemStack getItem() {
        return ItemCreator.of(CompMaterial.ANVIL, "&6Punish", " ",
            "&7Punishment: " + playerProvider.findNameUnsafe(target)).build()
            .makeMenuTool();
      }
    };

    listPunishes = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player, final Menu menu, final ClickType click) {
        PlayerPunishBrowser.showTo(getViewer(), target);
      }

      @Override
      public ItemStack getItem() {
        return ItemCreator
            .of(CompMaterial.CHEST, "&6Punishments", "", "&7View punishments")
            .build()
            .makeMenuTool();
      }
    };

    settings = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player, final Menu menu, final ClickType click) {
        if (!targetOnline()) {
          animateTitle(Localization.TARGET_IS_OFFLINE);
          return;
        }
        PlayerSettingsMenu.showTo(player, target);
      }

      @Override
      public ItemStack getItem() {
        if (!targetOnline()) {
          return ItemCreator
              .of(CompMaterial.COMPARATOR,
                  "&6Settings",
                  " ",
                  "&cDisabled:",
                  "&7Player is offline")
              .build()
              .makeMenuTool();
        }
        return ItemCreator
            .of(CompMaterial.COMPARATOR,
                "&6Settings",
                " ",
                "&7Settings for player")
            .build()
            .makeMenuTool();
      }
    };

    kick = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player, final Menu menu, final ClickType click) {
        if (!targetOnline()) {
          animateTitle(Localization.TARGET_IS_OFFLINE);
          return;
        }
        KickConversation.create(menu, target, targetName).start(player);
      }

      @Override
      public ItemStack getItem() {
        if (!targetOnline()) {
          return ItemCreator
              .of(
                  CompMaterial.BLAZE_POWDER,
                  "&6Kick",
                  "",
                  "&cDisabled:",
                  "&7Player is offline")
              .build().makeMenuTool();
        }

        return ItemCreator
            .of(
                CompMaterial.BLAZE_POWDER,
                "&6Kick",
                "",
                "&7Kick " + targetName)
            .build().makeMenuTool();

      }
    };
  }

  /*
  TODO:
    - Punish
    - Punishes
    - As console view
    - Kick

   */

  // ----------------------------------------------------------------------------------------------------
  // Overridden methods from menu
  // ----------------------------------------------------------------------------------------------------


  @Override
  public ItemStack getItemAt(final int slot) {

    if (slot == 1) {
      return punish.getItem();
    }

    if (slot == 11) {
      return listPunishes.getItem();
    }

    if (slot == 13) {
      return Item
          .ofSkullHash(textureProvider.getSkinTexture(target))
          .name("&7Data for: &6" + (targetOnline() ? "&a" : "&7") + targetName)
          .lores(ItemUtil.loreForPlayer(target, storageProvider))
          .build()
          .makeMenuTool();
    }

    if (slot == 15) {
      return settings.getItem();
    }

    if (slot == 7) {
      return kick.getItem();
    }

    return null;
  }

  private boolean targetOnline() {
    return Players.find(target).isPresent();
  }

  @Override
  protected String[] getInfo() {
    return new String[]{"&7Menu to select an", "&7Action for players"};
  }
}
