package org.mineacademy.punishcontrol.proxy.menus.punish;

import de.exceptionflug.mccommons.inventories.api.CallResult;
import de.exceptionflug.mccommons.inventories.api.ClickType;
import de.exceptionflug.protocolize.inventory.InventoryModule;
import de.exceptionflug.protocolize.items.ItemType;
import java.util.Arrays;
import java.util.UUID;
import javax.inject.Inject;
import lombok.NonNull;
import lombok.val;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NonNls;
import org.mineacademy.burst.item.Item;
import org.mineacademy.burst.menu.AbstractMenu;
import org.mineacademy.punishcontrol.core.group.Groups;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.providers.TextureProvider;
import org.mineacademy.punishcontrol.core.punish.PunishBuilder;
import org.mineacademy.punishcontrol.core.punish.PunishType;
import org.mineacademy.punishcontrol.core.punish.template.PunishTemplate;
import org.mineacademy.punishcontrol.core.setting.Replacer;
import org.mineacademy.punishcontrol.core.settings.ItemSettings;
import org.mineacademy.punishcontrol.core.settings.Settings;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;
import org.mineacademy.punishcontrol.proxy.DaggerProxyComponent;
import org.mineacademy.punishcontrol.proxy.ItemUtil;
import org.mineacademy.punishcontrol.proxy.conversations.PunishReasonConversation;
import org.mineacademy.punishcontrol.proxy.menu.AbstractDurationChooser;
import org.mineacademy.punishcontrol.proxy.menu.browser.AbstractPlayerBrowser;
import org.mineacademy.punishcontrol.proxy.menu.browser.AbstractPunishTypeBrowser;
import org.mineacademy.punishcontrol.proxy.menu.browser.AbstractTemplateBrowser;
import org.mineacademy.punishcontrol.proxy.menus.MainMenu;

public final class PunishCreatorMenu extends AbstractMenu {

  // ----------------------------------------------------------------------------------------------------
  // Localization
  // ----------------------------------------------------------------------------------------------------

  private static final String[] CHOOSE_DURATION_LORE = {
      "&7Choose the",
      "&7duration of the",
      "&7punish"
  };

  @NonNls
  private static final String NO_ACCESS = "You don't have access to the template";
  @NonNls
  private static final String ALREADY_SUPER_SILENT = "Punish is already super-silent";
  @NonNls
  private static final String CHANGE_TYPE = "Change type";
  @NonNls
  private static final String CHOOSE_ACTION = "Choose action";
  @NonNls
  private static final String REASON = "Reason";
  @NonNls
  private static final String CHOOSE_PLAYER = "Choose player";
  @NonNls
  private static final String APPLY = "Apply";
  @NonNls
  private static final String APPLY_PUNISHMENT = "Apply punishment";
  private static final String[] MENU_INFORMATION = {"&7Menu to", "&7create punishes"};
  @NonNls
  private static final String TARGET_IS_UNPUNISHABLE = "Target is unpunishable";
  @NonNls
  private static final String CAN_T_OVERRIDE_PUNISHES = "Can't override punishes";
  @NonNls
  private static final String CREATED_PUNISH = "Created punish";
  @NonNls
  private static final String YOU_WOULD_EXCEED_YOUR_LIMITS = "You would exceed your limits";
  @NonNls
  private static final String MISSING_DURATION = "Missing duration!";
  @NonNls
  private static final String MISSING_REASON = "Missing reason!";
  @NonNls
  private static final String MISSING_CREATOR = "Missing creator!";
  @NonNls
  private static final String MISSING_TARGET = "Missing target!";
  @NonNls
  private static final String MISSING_PUNISH_TYPE = "Missing punish-type!";
  private static final String[] MAKE_SUPER_SILENT_LORE = {"",
      "&7Click to make",
      "&7the punish",
      "&7super silent"};
  private static final String[] MAKE_NOT_SUPER_SILENT_LORE = {"",
      "&7Click to make",
      "&7the punish",
      "&7not silent"};
  private static final String[] MAKE_SILENT_LORE = {"",
      "&7Click to make",
      "&7the punish",
      "&7silent"};
  private static final String[] MAKE_NOT_SILENT_LORE = {"",
      "&7Click to make",
      "&7the punish",
      "&7not silent"};
  private static final String[] CHOOSE_TARGET_LORE = {"&7Choose the",
      "&7player the",
      "&7punish should be",
      "&7applied to"};

  private static String[] CHOOSE_REASON_LORE = new String[]{
      "&7Choose the",
      "&7reason of the",
      "&7punish"};

  @NonNls
  private static final String CURRENT = "Current";
  @NonNls
  private static final String CHOOSE_A_DIFFERENT_REASON = "Choose a different reason";
  private static final String[] CHOOSE_TEMPLATE_LORE = {"&7Create a punish",
      "&7from an existing",
      "&7template"};
  @NonNls
  private static final String DURATION = "Duration";
  @NonNls
  private static final String SUPER_SILENT = "super-silent";
  @NonNls
  private static final String NOT_SUPER_SILENT = "not super-silent";
  @NonNls
  private static final String SILENT = "silent";
  @NonNls
  private static final String NOT_SILENT = "not silent";
  private static Replacer TYPE_REPLACER = Replacer
      .of("&7Change the type",
          "&7of the punish",
          "&7" + CURRENT + "ly: {type}");

  private static Replacer duration = Replacer.of(
      "&7" + CURRENT + "ly: ",
      "&7{currently}",
      "&7Punish will end on:",
      "&7{end}",
      "",
      "&7Click to change");

  // ----------------------------------------------------------------------------------------------------
  // Button position
  // ----------------------------------------------------------------------------------------------------

  private static final int SIZE = 9 * 5;
  private static final int CHOOSE_PLAYER_SLOT = 33;
  private static final int CHOOSE_DURATION_SLOT = 29;
  private static final int APPLY_SLOT = 22;
  private static final int CHOOSE_REASON_SLOT = 19;
  private static final int MAKE_SILENT_SLOT = 30;
  private static final int MAKE_SUPER_SILENT_SLOT = 32;
  private static final int CHOOSE_TYPE_SLOT = 4;
  private static final int CHOOSE_TEMPLATE_SLOT = 25;

  // ----------------------------------------------------------------------------------------------------
  // Fields
  // ----------------------------------------------------------------------------------------------------

  private final TextureProvider textureProvider;
  private final PlayerProvider playerProvider;
  private final StorageProvider storageProvider;

  // Silent & Super silent
  private PunishBuilder punishBuilder;
  private PunishTemplate punishTemplate;
  private static final PunishType DEFAULT_PUNISH_TYPE = PunishType.BAN;

  public static void showTo(@NonNull final ProxiedPlayer player) {
    DaggerProxyComponent.create().punishCreatorMenu().displayTo(player);
  }

  public static void showTo(
      @NonNull final ProxiedPlayer player,
      @NonNull final PunishBuilder punishBuilder) {
    final val punishCreatorMenu = DaggerProxyComponent
        .create()
        .punishCreatorMenu();
    punishCreatorMenu.punishBuilder = punishBuilder;
    punishCreatorMenu.displayTo(player);
  }

  public static void showTo(
      @NonNull final ProxiedPlayer player,
      @NonNull final PunishBuilder punishBuilder,
      @NonNull final PunishTemplate punishTemplate) {
    final val punishCreatorMenu = DaggerProxyComponent
        .create()
        .punishCreatorMenu();
    punishCreatorMenu.punishBuilder = punishBuilder;
    punishCreatorMenu.punishTemplate = punishTemplate;
    punishCreatorMenu.displayTo(player);
  }

  @Inject
  public PunishCreatorMenu(
      final TextureProvider textureProvider,
      final PlayerProvider playerProvider,
      final StorageProvider storageProvider,
      final MainMenu mainMenu) {
    super("PunishCreator", mainMenu, SIZE);
    this.textureProvider = textureProvider;
    this.playerProvider = playerProvider;
    this.storageProvider = storageProvider;
    if (punishBuilder().target() != null) {
      setTitle("&8Punish " + playerProvider
          .findNameUnsafe(punishBuilder().target()));
    } else {
      setTitle("&8Create punish");
    }
    punishBuilder();
  }

  // ----------------------------------------------------------------------------------------------------
  // Overridden methods
  // ----------------------------------------------------------------------------------------------------

  @Override
  public void updateInventory() {
    super.updateInventory();

    ;
    //Duration | "Duration"
    {
      if (punishBuilder().duration() != null) {

        duration
            .replaceAll(
                "currently", punishBuilder().duration().toString(),
                "end", Settings.Advanced
                    .formatDate(
                        System.currentTimeMillis() + punishBuilder().duration().toMs()));

        set(Item.of(
            ItemType.CLOCK,
            "&6" + DURATION,
            duration.replacedMessage())
            .actionHandler("Duration")
            .slot(CHOOSE_DURATION_SLOT)
        );

      } else {
        set(
            Item
                .of(ItemType.CLOCK,
                    "&6" + DURATION,
                    CHOOSE_DURATION_LORE)
                .actionHandler("Duration")
                .slot(CHOOSE_DURATION_SLOT)
        );
      }
    }

    //Type | "Type"
    {
      TYPE_REPLACER
          .find("type")
          .replace(punishBuilder.punishType().localized());

      set(
          Item
              .of(ItemUtil.forPunishType(punishBuilder().punishType()))
              .name("&6" + CHANGE_TYPE)
              .lore(Arrays.asList(
                  TYPE_REPLACER.replacedMessage()
              ))
              .slot(CHOOSE_TYPE_SLOT)
              .actionHandler("Type")
      );
    }

    //Template | "Template"
    {
      if (punishTemplate != null) {
        set(
            Item
                .of(ItemUtil.forPunishType(punishBuilder().punishType()))
                .name("&6#" + CHOOSE_ACTION)
                .lore(CHOOSE_TEMPLATE_LORE)
                .addLore("&7" + CURRENT + ": &6" + punishTemplate.name())
                .slot(CHOOSE_TEMPLATE_SLOT)
                .actionHandler("Template")
        );
      } else {
        set(
            Item
                .of(ItemUtil.forPunishType(punishBuilder().punishType()))
                .name("&6" + CHOOSE_ACTION)
                .lore(
                    CHOOSE_TEMPLATE_LORE)
                .slot(CHOOSE_TEMPLATE_SLOT)
                .actionHandler("Template")
        );
      }
    }

    //Reason | "Reason"
    {
      if (punishBuilder().reason() != null) {
        set(Item.of(ItemSettings.REASON_ITEM.itemType(),
            "&6" + REASON,
            "&7" + CHOOSE_A_DIFFERENT_REASON,
            "&7" + CURRENT + ": " + punishBuilder.reason())
            .slot(CHOOSE_REASON_SLOT)
            .actionHandler(REASON)
        );
      } else {
        set(
            Item.of(ItemSettings.REASON_ITEM.itemType(),
                "&6" + REASON,
                CHOOSE_REASON_LORE)
                .slot(CHOOSE_REASON_SLOT)
                .actionHandler(REASON)
        );
      }
    }

    //Player | "Player"
    {
      final UUID target = punishBuilder().target();

      if (target != null) {
        set(
            Item
                .of(textureProvider.getSkinTexture(target))
                .name("&6" + CHOOSE_PLAYER)
                .lore("&7" + CURRENT + ": " + playerProvider.findName(target).orElse("unknown"))
                .slot(CHOOSE_PLAYER_SLOT)
                .actionHandler("Player")
        );
      } else {
        set(
            Item
                .of(ItemType.PLAYER_HEAD,
                    "&6" + CHOOSE_PLAYER,
                    CHOOSE_TARGET_LORE)
                .slot(CHOOSE_PLAYER_SLOT)
                .actionHandler("Player")
        );
      }
    }

    // Silent | "Silent"
    {
      if (punishBuilder.silent()) {
        set(
            Item
                .ofString(ItemSettings.ENABLED.itemType())
                .name("&6Silent")
                .lore(
                    MAKE_NOT_SILENT_LORE
                )
                .slot(MAKE_SILENT_SLOT)
                .actionHandler("Silent")
        );
      } else {
        set(
            Item
                .ofString(ItemSettings.DISABLED.itemType())
                .name("&6Make Silent")
                .lore(
                    MAKE_SILENT_LORE
                )
                .slot(MAKE_SILENT_SLOT)
                .actionHandler("Silent")
        );
      }
    }

    // Super silent | "SuperSilent"
    {
      if (punishBuilder.superSilent()) {
        set(
            Item
                .ofString(ItemSettings.ENABLED.itemType())
                .name("&6Super Silent")
                .lore(
                    MAKE_NOT_SUPER_SILENT_LORE
                )
                .slot(MAKE_SUPER_SILENT_SLOT)
                .actionHandler("SuperSilent")
        );
      } else {
        set(
            Item
                .ofString(ItemSettings.DISABLED.itemType())
                .name("&6Make Super Silent")
                .lore(
                    MAKE_SUPER_SILENT_LORE
                )
                .slot(MAKE_SUPER_SILENT_SLOT)
                .actionHandler("SuperSilent")
        );
      }
    }

    //Apply | "Apply"
    {
      set(
          Item
              .of(ItemSettings.APPLY_ITEM.itemType(),
                  "&a" + APPLY,
                  "&7" + APPLY_PUNISHMENT)
              .slot(APPLY_SLOT)
              .actionHandler(APPLY)
      );
    }
  }

  @Override
  public void reDisplay() {
    if (punishTemplate == null) {
      showTo(getPlayer(), punishBuilder());
    } else {
      showTo(getPlayer(), punishBuilder(), punishTemplate);
    }
  }

  @Override
  public void registerActionHandlers() {
    //Duration
    registerActionHandler("Duration", (duration -> {
      punishTemplate = null;
      new AbstractDurationChooser(PunishCreatorMenu.this) {

        @Override
        protected void confirm() {
          punishBuilder().duration(ms);
          PunishCreatorMenu.this.displayTo(player);
        }
      }.displayTo(player);

      return CallResult.DENY_GRABBING;
    }));

    //Type
    registerActionHandler("Type", (type -> {
      punishTemplate = null;
      new AbstractPunishTypeBrowser(this) {
        @Override
        protected void onClick(final ClickType clickType, final PunishType punishType) {
          showTo(player, punishBuilder().punishType(punishType));

        }
      }.displayTo(player);
      return CallResult.DENY_GRABBING;
    }));

    //Template
    registerActionHandler("Template", (template -> {

      new AbstractTemplateBrowser(PunishCreatorMenu.this) {

        @Override
        protected void onClick(final ClickType clickType,
            final PunishTemplate punishTemplate) {
          //Applying new setting & showing up
          if (!Groups.hasAccess(player.getUniqueId(), punishTemplate)) {
            animateTitle("&c" + NO_ACCESS);
            return;
          }

          //Reinitialisation of our punishBuilder since this is easier & more safe to
          //apply. (To many values are changed)
          showTo(
              player,
              punishTemplate.toPunishBuilder().target(punishBuilder.target()),
              punishTemplate);
        }
      }.displayTo(player);
      return CallResult.DENY_GRABBING;
    }));

    //Reason
    registerActionHandler(REASON, (reason -> {
      punishTemplate = null;
      InventoryModule.closeAllInventories(getPlayer());
      PunishReasonConversation.create(getPlayer(), this).start();
      return CallResult.DENY_GRABBING;
    }));

    //Player
    registerActionHandler("Player", (player -> {
      punishTemplate = null;
      new AbstractPlayerBrowser(
          Providers.playerProvider(),
          Providers.textureProvider(),
          PunishCreatorMenu.this) {

        @Override
        protected void onClick(final ClickType clickType, final UUID uuid) {
          PunishCreatorMenu.showTo(player, punishBuilder().target(uuid));
        }
      }.displayTo(getPlayer());
      return CallResult.DENY_GRABBING;
    }));

    // Silent
    registerActionHandler("Silent", (silent -> {
      if (punishBuilder.superSilent()) {
        animateTitle("&c" + ALREADY_SUPER_SILENT);
        return CallResult.DENY_GRABBING;
      }
      punishBuilder.silent(!punishBuilder.silent());
      animateTitle(punishBuilder.silent()
          ? "&a" + SILENT
          : "&c" + NOT_SILENT);

      build();
      return CallResult.DENY_GRABBING;
    }));

    //Super-Silent
    registerActionHandler("SuperSilent", (superSilent -> {
      if (punishBuilder.silent()) {
        punishBuilder.silent(false);
      }
      punishBuilder.superSilent(!punishBuilder.superSilent());
      punishBuilder.silent(!punishBuilder.silent());
      animateTitle(punishBuilder.silent()
          ? "&a" + SUPER_SILENT
          : "&c" + NOT_SUPER_SILENT);

      build();

      return CallResult.DENY_GRABBING;
    }));

    //Apply
    registerActionHandler(APPLY, (apply -> {

      punishBuilder()
          .creator(getPlayer().getUniqueId())
          .creation(System.currentTimeMillis());

      if (punishBuilder().punishType() == null) {
        animateTitle("&c" + MISSING_PUNISH_TYPE);
        return CallResult.DENY_GRABBING;
      }
      if (punishBuilder().target() == null) {
        animateTitle("&c" + MISSING_TARGET);
        return CallResult.DENY_GRABBING;
      }
      if (punishBuilder().creator() == null) {
        animateTitle("&c" + MISSING_CREATOR);
        return CallResult.DENY_GRABBING;
      }

      if (punishBuilder().reason() == null) {
        animateTitle("&c" + MISSING_REASON);
        return CallResult.DENY_GRABBING;
      }

      if (punishBuilder().duration() == null) {
        animateTitle("&c" + MISSING_DURATION);
        return CallResult.DENY_GRABBING;
      }

      //Checking access

      //Not from template
      if (punishTemplate == null) {
        if (!Groups.hasAccess(
            getPlayer().getUniqueId(),
            punishBuilder.punishType(),
            punishBuilder.duration())) {
          animateTitle("&c" + YOU_WOULD_EXCEED_YOUR_LIMITS);
          return CallResult.DENY_GRABBING;
        }
      } else {
        if (!Groups.hasAccess(
            getPlayer().getUniqueId(),
            punishTemplate)) {
          animateTitle("&c" + YOU_WOULD_EXCEED_YOUR_LIMITS);
          return CallResult.DENY_GRABBING;
        }
      }

      animateTitle("&7" + CREATED_PUNISH);
      async(() -> {

        if (storageProvider.isPunished(punishBuilder.target(), punishBuilder.punishType())
            && !Groups
            .canOverride(getPlayer().getUniqueId())) {
          animateTitle("&c" + CAN_T_OVERRIDE_PUNISHES);
          return;
        }

        if (!playerProvider.punishable(punishBuilder.target())) {
          animateTitle("&c" + TARGET_IS_UNPUNISHABLE);
          return;
        }

        punishBuilder().build().create();
        if (getParentMenu() != null) {
          getParentMenu().displayTo(getPlayer());
        }

      });

      return CallResult.DENY_GRABBING;
    }));
  }

  @Override
  protected String[] getInfo() {
    return MENU_INFORMATION;
  }

  // ----------------------------------------------------------------------------------------------------
  // Internal helper-methods
  // ----------------------------------------------------------------------------------------------------

  //Lazy getter for the builder
  private PunishBuilder punishBuilder() {
    if (punishBuilder != null) {
      return punishBuilder;
    }

    return punishBuilder = PunishBuilder.of(DEFAULT_PUNISH_TYPE);
  }

  public void setReason(final String reason) {
    punishBuilder.reason(reason);
  }
}