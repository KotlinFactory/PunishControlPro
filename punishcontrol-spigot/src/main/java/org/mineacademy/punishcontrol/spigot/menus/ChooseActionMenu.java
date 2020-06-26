package org.mineacademy.punishcontrol.spigot.menus;

import java.util.UUID;
import lombok.NonNull;
import lombok.val;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NonNls;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.punishcontrol.core.injector.annotations.Localizable;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.providers.TextureProvider;
import org.mineacademy.punishcontrol.core.punish.PunishBuilder;
import org.mineacademy.punishcontrol.core.punish.PunishType;
import org.mineacademy.punishcontrol.core.settings.Localization;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;
import org.mineacademy.punishcontrol.spigot.DaggerSpigotComponent;
import org.mineacademy.punishcontrol.spigot.Players;
import org.mineacademy.punishcontrol.spigot.Scheduler;
import org.mineacademy.punishcontrol.spigot.conversations.KickConversation;
import org.mineacademy.punishcontrol.spigot.menus.browsers.PlayerPunishBrowser;
import org.mineacademy.punishcontrol.spigot.menus.punish.PunishCreatorMenu;
import org.mineacademy.punishcontrol.spigot.menus.settings.PlayerSettingsMenu;
import org.mineacademy.punishcontrol.spigot.util.ItemStacks;

@Localizable
public final class ChooseActionMenu extends Menu {
// ----------------------------------------------------------------------------------------------------
  // Localization
  // ----------------------------------------------------------------------------------------------------

  @Localizable("Menu.Proxy.ChooseAction.Lore")
  private static String[] MENU_INFORMATION = {
      "&7Menu to select ",
      "&7Action for players"};

  @NonNls
  @Localizable("Parts.Action_For")
  private static String ACTION_FOR = "Action for";

  @NonNls @Localizable("Parts.Punishments")
  private static String PUNISHMENTS = "Punishments";
  @NonNls @Localizable("Menu.Proxy.ChooseAction.Player_Offline_Lore")
  private static String[] PLAYER_IS_OFFLINE_LORE = {
      " ",
      "&cDisabled:",
      "&7Player is offline"};
  @NonNls @Localizable("Menu.Proxy.ChooseAction.View_Punishments")
  private static String[] VIEW_PUNISHMENT_LORE = {
      "",
      "&7View punishments"};
  @NonNls @Localizable("Parts.Settings")
  private static String SETTINGS = "Settings";
  @NonNls @Localizable("Parts.Kick")
  private static String KICK = "Kick";
  @NonNls @Localizable("Parts.Data_For")
  private static String DATA_FOR = "Data for";
  @NonNls @Localizable("Parts.Punish")
  private static String PUNISH = "Punish";
  @NonNls @Localizable("Menu.Proxy.ChooseAction.Settings_Lore")
  private static String[] SETTINGS_LORE = {
      " ",
      "&7Settings for player"};

  // ----------------------------------------------------------------------------------------------------
  // Button positions
  // ---------------------------------------------------------------------------------------------------
  private static final int LIST_PUNISHES_SLOT = 11;
  private static final int PLAYER_HEAD_SLOT = 13;
  private static final int SETTINGS_SLOT = 15;
  private static final int KICK_SLOT = 7;
  private static final int PUNISH_SLOT = 1;

  // ----------------------------------------------------------------------------------------------------
  // Constructors & Fields
  // ----------------------------------------------------------------------------------------------------

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
      @NonNull final Player player,
      @NonNull final UUID target) {
    Scheduler.runAsync(() -> {
      final val menu = create(target);
      menu.displayTo(player, true);
    });
  }

  public static ChooseActionMenu create(@NonNull final UUID target) {
    return new ChooseActionMenu(
        DaggerSpigotComponent.create().menuMain(),
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
    super(mainMenu);
    this.playerProvider = playerProvider;
    this.textureProvider = textureProvider;
    this.storageProvider = storageProvider;
    this.target = target;
    targetName = playerProvider.findName(target).orElse("unknown");
    setSize(9 * 3);
    setTitle("§8" + ACTION_FOR + " " + targetName);

    punish = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player, final Menu menu, final ClickType click) {

        PunishCreatorMenu.showTo(
            getViewer(),
            PunishBuilder
                .of(PunishType.BAN)
                .target(target));
      }

      @Override
      public ItemStack getItem() {
        return ItemCreator.of(CompMaterial.ANVIL, "&6" + PUNISH, " ",
            "&7Punish: " + playerProvider.findName(target).orElse("unknown")).build()
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
            .of(CompMaterial.CHEST, "&6" + PUNISHMENTS, VIEW_PUNISHMENT_LORE)
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
        if (!targetOnline())
          return ItemCreator
              .of(
                  CompMaterial.COMPARATOR,
                  "&6Settings",
                  PLAYER_IS_OFFLINE_LORE)
              .build()
              .makeMenuTool();
        return ItemCreator
            .of(
                CompMaterial.COMPARATOR,
                "&6Settings",
                PLAYER_IS_OFFLINE_LORE)
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
        if (!targetOnline())
          return ItemCreator
              .of(
                  CompMaterial.BLAZE_POWDER,
                  "&6" + KICK,
                  PLAYER_IS_OFFLINE_LORE)
              .build().makeMenuTool();

        return ItemCreator
            .of(
                CompMaterial.BLAZE_POWDER,
                "&6" + KICK,
                "",
                "&7" + KICK + targetName)
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

    if (slot == PUNISH_SLOT)
      return punish.getItem();

    if (slot == LIST_PUNISHES_SLOT)
      return listPunishes.getItem();

    if (slot == ChooseActionMenu.PLAYER_HEAD_SLOT)
      return ItemCreator
          .ofSkullHash(textureProvider.getSkinTexture(target))
          .name("&7" + DATA_FOR + ": &6" + (targetOnline() ? "&a" : "&7") + targetName)
          .lores(ItemStacks.loreForPlayer(target, storageProvider, playerProvider))
          .build()
          .makeMenuTool();

    if (slot == SETTINGS_SLOT)
      return settings.getItem();

    if (slot == KICK_SLOT)
      return kick.getItem();

    return null;
  }

  private boolean targetOnline() {
    return Players.find(target).isPresent();
  }

  @Override
  protected String[] getInfo() {
    return MENU_INFORMATION;
  }
}
