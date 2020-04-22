package org.mineacademy.punishcontrol.proxy.menus;

import de.exceptionflug.protocolize.items.ItemStack;
import java.util.Arrays;
import java.util.Collections;
import javafx.scene.control.Button;
import javax.inject.Inject;
import lombok.NonNull;
import lombok.val;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.mineacademy.bfo.plugin.SimplePlugin;
import org.mineacademy.bfo.settings.SimpleLocalization.Player;
import org.mineacademy.burst.item.ChangingButton;
import org.mineacademy.burst.item.ItemStacks;
import org.mineacademy.burst.menu.ChangingMenu;
import org.mineacademy.burst.menu.Menu;
import org.mineacademy.burst.util.Scheduler;
import org.mineacademy.punishcontrol.core.providers.TextureProvider;
import org.mineacademy.punishcontrol.proxy.DaggerProxyComponent;
import org.mineacademy.punishcontrol.proxy.menus.browsers.AllPunishesBrowser;
import org.mineacademy.punishcontrol.proxy.menus.browsers.PlayerBrowser;
import org.mineacademy.punishcontrol.proxy.menus.browsers.PunishedPlayerBrowser;
import org.mineacademy.punishcontrol.proxy.menus.browsers.SettingsBrowser;
import org.mineacademy.punishcontrol.proxy.menus.punish.PunishCreatorMenu;

public final class MainMenu extends ChangingMenu {

  public static final int PLAYER_BROWSER_SLOT = 9 * 2 + 6;
  private final Button punishesButton;
  private final Button newButton;
  private final Button settingsButton;

  public static void showTo(@NonNull final ProxiedPlayer player) {
    Scheduler.runAsync(() -> {
      final val menu = DaggerProxyComponent.create().menuMain();
      menu.displayTo(player);
    });
  }


  @Inject
  public MainMenu(final TextureProvider textureProvider) {
    super("MainMenu",
        Collections.singletonList(
            ChangingButton
                .fromCustomHashes(
                    textureProvider.listTextures())
                .name("&6Players")
                .slot(24)
                .lore("&7View players", "&7to select", "&7one for more",
                    "&7actions")),
        9 * 5
    );
    setTitle("§3Punish§bControl");

    punishesButton = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player, final Menu menu, final ClickType click) {
        if (click.isRightClick()) {
          PunishedPlayerBrowser.showTo(player);
          return;
        }
        AllPunishesBrowser.showTo(player);
      }

      @Override
      public ItemStack getItem() {
        return ItemCreator
            .of(CompMaterial.CHEST,
                "&6Punishments",
                "",
                "Browse created punishments.",
                "Right click to view",
                "punished players"
            )
            .glow(true).build().make();
      }
    };

    // This also sends the player to another menu but you can do additional stuff on clicking the button
    newButton = new Button() {

      @Override
      public void onClickedInMenu(
          final Player pl,
          final Menu menu,
          final ClickType click) {
        PunishCreatorMenu.showTo(pl);
      }

      @Override
      public ItemStack getItem() {
        return ItemCreator
            .of(ItemStacks.cyanDye())
            .name("&6Create New")
            .lores(Arrays.asList(" ", "Make new punish"))
            .build().make();
      }
    };

    settingsButton = new Button() {

      @Override
      public void onClickedInMenu(final Player pl, final Menu menu,
          final ClickType click) {
        SettingsBrowser.showTo(pl);
      }

      @Override
      public ItemStack getItem() {
        return ItemCreator
            .of(CompMaterial.COMPARATOR,
                "&6Settings",
                "&7View Settings for",
                SimplePlugin.getNamed(),
                ""
            )
            .build().make();
      }
    };
  }

  @Override
  public ItemStack findItem(final int slot) {

    if (Arrays.asList(0, 9, 18, 27, 36, 8, 17, 26, 35, 44, 1, 7, 37, 43)
        .contains(slot)) {
      return ItemCreator.of(ItemStacks.cyanGlassPane())
          .name(" ")
          .build()
          .make();
    }
    if (slot == 9 * 2 + 2) {
      return punishesButton.getItem();
    }

    if (slot == 9 + 4) {
      return newButton.getItem();
    }

    if (slot == 9 * 3 + 4) {
      return settingsButton.getItem();
    }

    return null;
  }

  @Override
  protected void onMenuClick(final Player player, final int slot,
      final ItemStack clicked) {

    if (slot != PLAYER_BROWSER_SLOT) {
      return;
    }

    PlayerBrowser.showTo(player);
  }

  @Override
  protected String[] getInfo() {
    return null;
  }
}
