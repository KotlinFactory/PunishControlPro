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
import org.mineacademy.burst.item.Item;
import org.mineacademy.burst.menu.AbstractMenu;
import org.mineacademy.punishcontrol.core.group.Groups;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.providers.TextureProvider;
import org.mineacademy.punishcontrol.core.punish.PunishBuilder;
import org.mineacademy.punishcontrol.core.punish.PunishType;
import org.mineacademy.punishcontrol.core.punish.template.PunishTemplate;
import org.mineacademy.punishcontrol.core.settings.Settings;
import org.mineacademy.punishcontrol.proxy.DaggerProxyComponent;
import org.mineacademy.punishcontrol.proxy.ItemUtil;
import org.mineacademy.punishcontrol.proxy.conversations.PunishReasonConversation;
import org.mineacademy.punishcontrol.proxy.menu.AbstractDurationChooser;
import org.mineacademy.punishcontrol.proxy.menu.browser.AbstractPlayerBrowser;
import org.mineacademy.punishcontrol.proxy.menu.browser.AbstractPunishTypeBrowser;
import org.mineacademy.punishcontrol.proxy.menu.browser.AbstractTemplateBrowser;
import org.mineacademy.punishcontrol.proxy.menus.MainMenu;

public final class PunishCreatorMenu extends AbstractMenu {

  public static final int SIZE = 9 * 5;
  public static final int CHOOSE_PLAYER_SLOT = 33;
  public static final int CHOOSE_DURATION_SLOT = 29;
  public static final int APPLY_SLOT = 22;
  public static final int CHOOSE_REASON_SLOT = 19;
  public static final int MAKE_SILENT_SLOT = 30;
  public static final int MAKE_SUPER_SILENT_SLOT = 32;
  public static final int CHOOSE_TYPE_SLOT = 4;
  public static final int CHOOSE_TEMPLATE_SLOT = 25;

  private final TextureProvider textureProvider;
  private final PlayerProvider playerProvider;

  //Silent & Super silent

  private PunishBuilder punishBuilder;
  private PunishTemplate punishTemplate;
  private final PunishType punishType = PunishType.BAN;

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
      final MainMenu mainMenu) {
    super("PunishCreator", mainMenu, SIZE);
    this.textureProvider = textureProvider;
    this.playerProvider = playerProvider;
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

    //Duration | "Duration"
    {
      if (punishBuilder().duration() != null) {
        set(Item.of(
            ItemType.CLOCK,
            "&6Duration",
            "&7Currently: ",
            "&7" + punishBuilder().duration().toString(),
            "&7Punish will end on:",
            "&7" + Settings.Advanced
                .formatDate(
                    System.currentTimeMillis() + punishBuilder().duration()
                        .toMs()),
            "",
            "&7Click to change")
            .actionHandler("Duration")
            .slot(CHOOSE_DURATION_SLOT)
        );

      } else {
        set(
            Item
                .of(ItemType.CLOCK,
                    "&6Duration",
                    "&7Choose the",
                    "&7duration of the",
                    "&7punish")
                .actionHandler("Duration")
                .slot(CHOOSE_DURATION_SLOT)
        );
      }
    }

    //Type | "Type"
    {
      set(
          Item
              .of(ItemUtil.forPunishType(punishBuilder().punishType()))
              .name("&6Change type")
              .lore(Arrays.asList(
                  "&7Change the type",
                  "&7of the punish",
                  "&7Currently: " + punishBuilder.punishType().localized()))
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
                .name("&6Choose action")
                .lore(
                    "&7Create an punish",
                    "&7from an existing",
                    "&7template",
                    "&7Current: &6" + punishTemplate.name())
                .slot(CHOOSE_TEMPLATE_SLOT)
                .actionHandler("Template")
        );
      } else {
        set(
            Item
                .of(ItemUtil.forPunishType(punishBuilder().punishType()))
                .name("&6Choose action")
                .lore(
                    "&7Create an punish",
                    "&7from an existing",
                    "&7template")
                .slot(CHOOSE_TEMPLATE_SLOT)
                .actionHandler("Template")
        );
      }
    }

    //Reason | "Reason"
    {
      if (punishBuilder().reason() != null) {
        set(Item.of(ItemType.BOOK,
            "&6Reason",
            "&7Choose different reason",
            "&7Current: " + punishBuilder.reason())
            .slot(CHOOSE_REASON_SLOT)
            .actionHandler("Reason")
        );
      } else {
        set(
            Item.of(ItemType.BOOK,
                "&6Reason",
                "&7Choose the",
                "&7reason of the",
                "&7punish")
                .slot(CHOOSE_REASON_SLOT)
                .actionHandler("Reason")
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
                .name("&6Choose player")
                .lore("&7Current : " + playerProvider.findNameUnsafe(target))
                .slot(CHOOSE_PLAYER_SLOT)
                .actionHandler("Player")
        );
      } else {
        set(
            Item
                .of(ItemType.PLAYER_HEAD,
                    "&6Choose player",
                    "&7Choose the",
                    "&7player the",
                    "&7punish should be",
                    "&7applied to")
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
                .of(ItemType.GREEN_STAINED_GLASS_PANE)
                .name("&6Silent")
                .lore(
                    "",
                    "&7Click to make",
                    "&7the punish",
                    "&7not silent"
                )
                .slot(MAKE_SILENT_SLOT)
                .actionHandler("Silent")
        );
      } else {

        set(
            Item
                .of(ItemType.RED_STAINED_GLASS_PANE)
                .name("&6Make Silent")
                .lore(
                    "",
                    "&7Click to make",
                    "&7the punish",
                    "&7silent"
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
                .of(ItemType.GREEN_STAINED_GLASS_PANE)
                .name("&6Super-Silent")
                .lore(
                    "",
                    "&7Click to make",
                    "&7the punish",
                    "&7not silent"
                )
                .slot(MAKE_SUPER_SILENT_SLOT)
                .actionHandler("SuperSilent")
        );
      } else {
        set(
            Item
                .of(ItemType.RED_STAINED_GLASS_PANE)
                .name("&6Make Super-Silent")
                .lore(
                    "",
                    "&7Click to make",
                    "&7the punish",
                    "&7silent"
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
              .of(ItemType.EMERALD_BLOCK,
                  "&aApply",
                  "&7Apply punish")
              .slot(APPLY_SLOT)
              .actionHandler("Apply")
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
            animateTitle("&cYou don't have access to the template");
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
    registerActionHandler("Reason", (reason -> {
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
        animateTitle("&cPunish is already super-silent!");
        return CallResult.DENY_GRABBING;
      }
      punishBuilder.silent(!punishBuilder.silent());
      animateTitle(punishBuilder.silent()
          ? "&asilent"
          : "&cnot silent");

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
          ? "&asuper-silent"
          : "&cnot super-silent");

      build();

      return CallResult.DENY_GRABBING;
    }));

    //Apply
    registerActionHandler("Apply", (apply -> {

      punishBuilder()
          .creator(getPlayer().getUniqueId())
          .creation(System.currentTimeMillis());

      if (punishBuilder().punishType() == null) {
        animateTitle("&cMissing punish-type!");
        return CallResult.DENY_GRABBING;
      }
      if (punishBuilder().target() == null) {
        animateTitle("&cMissing target!");
        return CallResult.DENY_GRABBING;
      }
      if (punishBuilder().creator() == null) {
        animateTitle("&cMissing creator!");
        return CallResult.DENY_GRABBING;
      }

      if (punishBuilder().reason() == null) {
        animateTitle("&cMissing reason!");
        return CallResult.DENY_GRABBING;
      }

      //Checking access

      //Not from template
      if (punishTemplate == null) {
        if (!Groups.hasAccess(
            getPlayer().getUniqueId(),
            punishBuilder.punishType(),
            punishBuilder.duration())) {
          animateTitle("&cYou would exceed your limits");
          return CallResult.DENY_GRABBING;
        }
      } else {
        if (!Groups.hasAccess(
            getPlayer().getUniqueId(),
            punishTemplate)) {
          animateTitle("&cYou would exceed your limits");
          return CallResult.DENY_GRABBING;
        }
      }

      animateTitle("&7Created punish");
      async(() -> punishBuilder().build().create());
      if (getParentMenu() != null) {
        getParentMenu().displayTo(getPlayer());
      }

      return CallResult.DENY_GRABBING;
    }));
  }

  @Override
  protected String[] getInfo() {
    return new String[]{"&7Menu to", "&7create punishes"};
  }

  // ----------------------------------------------------------------------------------------------------
  // Internal helper-methods
  // ----------------------------------------------------------------------------------------------------

  //Lazy getter for the builder
  private PunishBuilder punishBuilder() {
    if (punishBuilder != null) {
      return punishBuilder;
    }

    return punishBuilder = PunishBuilder.of(punishType);
  }

  public void setReason(final String reason) {
    punishBuilder.reason(reason);
  }
}