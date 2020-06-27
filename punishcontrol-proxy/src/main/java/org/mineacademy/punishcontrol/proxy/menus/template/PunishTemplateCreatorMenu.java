package org.mineacademy.punishcontrol.proxy.menus.template;

import de.exceptionflug.mccommons.inventories.api.CallResult;
import de.exceptionflug.mccommons.inventories.api.ClickType;
import de.exceptionflug.protocolize.inventory.InventoryModule;
import de.exceptionflug.protocolize.items.ItemType;
import java.io.File;
import java.util.Arrays;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.val;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NonNls;
import org.mineacademy.bfo.plugin.SimplePlugin;
import org.mineacademy.burst.item.Item;
import org.mineacademy.burst.menu.AbstractMenu;
import org.mineacademy.burst.util.Scheduler;
import org.mineacademy.punishcontrol.core.injector.annotations.Localizable;
import org.mineacademy.punishcontrol.core.punish.PunishDuration;
import org.mineacademy.punishcontrol.core.punish.PunishType;
import org.mineacademy.punishcontrol.core.punish.template.PunishTemplate;
import org.mineacademy.punishcontrol.core.punish.template.PunishTemplates;
import org.mineacademy.punishcontrol.core.settings.ItemSettings;
import org.mineacademy.punishcontrol.core.settings.Settings;
import org.mineacademy.punishcontrol.proxy.DaggerProxyComponent;
import org.mineacademy.punishcontrol.proxy.ItemUtil;
import org.mineacademy.punishcontrol.proxy.conversations.template.TemplatePermissionConversation;
import org.mineacademy.punishcontrol.proxy.conversations.template.TemplateReasonConversation;
import org.mineacademy.punishcontrol.proxy.menu.AbstractDurationChooser;
import org.mineacademy.punishcontrol.proxy.menu.browser.AbstractPunishTypeBrowser;
import org.mineacademy.punishcontrol.proxy.menus.browsers.PunishTemplateBrowser;

@Localizable
@Accessors(fluent = true)
public final class PunishTemplateCreatorMenu extends AbstractMenu {

  // ----------------------------------------------------------------------------------------------------
  // Localization
  // ----------------------------------------------------------------------------------------------------

  @NonNls
  @Localizable("Menu.Proxy.TemplateCreator.Name")
  private static String TEMPLATE_CREATOR = "Template Creator";
  @Localizable("Menu.Proxy.TemplateCreator.Choose_Duration.Lore")
  private static String[] CHOOSE_DURATION_LORE = {
      "&7Choose the",
      "&7duration of the",
      "&7punish"};

  @Localizable("Parts.Duration")
  private static String DURATION = "&6Duration";
  @NonNls
  @Localizable("Parts.Change_Type")
  private static String CHANGE_TYPE = "Change type";
  @Localizable("Parts.Reason")
  private static String REASON = "Reason";
  @NonNls
  @Localizable("Parts.Currently")
  private static String CURRENTLY = "Currently";
  @NonNls
  @Localizable("Menu.Proxy.TemplateCreator.ChoosePermission.Name")
  private static String CHOOSE_PERMISSION = "Choose permission";
  private static String[] MENU_INFORMATION = {
      "&7Menu to",
      "&7edit templates"};
  @Localizable("Menu.Proxy.TemplateCreator.Already.SuperSilent")
  private static String ALREADY_SUPER_SILENT = "&cPunishment is already super-silent";
  @Localizable("Parts.Apply")
  private static String APPLY = "Apply";
  @NonNls
  @Localizable("Parts.Silent")
  private static String SILENT = "Silent";
  @NonNls
  @Localizable("Parts.SuperSilent")
  private static String SUPER_SILENT = "Super-Silent";
  @NonNls
  @Localizable("Menu.Proxy.Template.Make.SuperSilent.Name")
  private static String MAKE_SUPER_SILENT = "Make super silent";
  @NonNls
  @Localizable("Menu.Proxy.Template.Make.Silen.Namet")
  private static String MAKE_SILENT = "Make silent";
  @NonNls
  @Localizable("Parts.Not")
  private static String NOT = "not";
  @Localizable("Menu.Proxy.Template.Make.Not.Silent.Lore")
  private static String[] MAKE_NOT_SILENT_LORE = {
      " ",
      "&7Click to make",
      "&7the punish",
      "&7not silent"};
  @Localizable("Menu.Proxy.Template.Make.Silent.Lore")
  private static String[] MAKE_SILENT_LORE = {
      "",
      "&7Click to make",
      "&7the punish",
      "&7silent"};
  @Localizable("Menu.Proxy.Template.Make.Not.SuperSilent.Lore")
  private static String[] MAKE_NOT_SUPER_SILENT_LORE = {
      "",
      "&7Click to make",
      "&7the punish",
      "&7Not super silent"};
  @Localizable("Menu.Proxy.Template.Make.SuperSilent.Lore")
  private static String[] MAKE_SUPER_SILENT_LORE = {
      "",
      "&7Click to make",
      "&7the punish",
      "&7super silent"};

  // ----------------------------------------------------------------------------------------------------
  // Button positions
  // ----------------------------------------------------------------------------------------------------

  private static final int SIZE = 9 * 6;
  private static final int CHOOSE_REASON_SLOT = 19;
  private static final int CHOOSE_PERMISSION_SLOT = 25;
  public static final int MAKE_SILENT_SLOT = 29;
  public static final int MAKE_SUPER_SILENT_SLOT = 33;
  public static final int CHOOSE_TYPE_SLOT = 4;
  public static final int CHOOSE_DURATION_SLOT = 40;
  public static final int APPLY_SLOT = 22;

  // ----------------------------------------------------------------------------------------------------
  // Showing up
  // ----------------------------------------------------------------------------------------------------

  public static void showTo(
      final ProxiedPlayer player,
      final PunishTemplate punishTemplate) {
    Scheduler.runAsync(() -> fromExisting(punishTemplate).displayTo(player));
  }

  public static void showTo(
      @NonNull final ProxiedPlayer player,
      @NonNull final String name) {
    Scheduler.runAsync(() -> {
      final val menu = create(name);

      menu.displayTo(player);
    });
  }

  public static void fromExistingAndShow(
      @NonNull final ProxiedPlayer player,
      @NonNull final PunishTemplate punishTemplate) {
    Scheduler.runAsync(() -> {
      final val menu = fromExisting(punishTemplate);
      menu.displayTo(player);
    });
  }

  public static PunishTemplateCreatorMenu fromExisting(
      @NonNull final PunishTemplate punishTemplate) {
    return new PunishTemplateCreatorMenu(punishTemplate);
  }

  public static PunishTemplateCreatorMenu create(@NonNull final String name) {
    final File file = new File(
        SimplePlugin.getData() + "/templates/",
        name + ".json");
    //Creating new
    final val template = PunishTemplate.createWithDefaults(file);
    //Registering
    PunishTemplates.register(template);
    return fromExisting(template);
  }

  // ----------------------------------------------------------------------------------------------------
  // Fields an constructors
  // ----------------------------------------------------------------------------------------------------

  @Getter
  private final PunishTemplate punishTemplate;

  private PunishTemplateCreatorMenu(
      @NonNull final PunishTemplate punishTemplate) {
    super(
        "PunishTemplateCreatorMenu",
        DaggerProxyComponent.create().punishTemplateBrowser(),
        SIZE
    );
    this.punishTemplate = punishTemplate;

    setTitle("&8" + TEMPLATE_CREATOR);
  }

  // ----------------------------------------------------------------------------------------------------
  // Overridden methods
  // ----------------------------------------------------------------------------------------------------

  @Override
  public void updateInventory() {
    super.updateInventory();

    if (cancelled) {
      return;
    }
    // Duration | "Duration"
    {
      if (punishTemplate().duration() != null) {
        set(
            Item.of(
                ItemType.CLOCK,
                "&6" + DURATION,
                "&7" + CURRENTLY + ": ",
                "&7" + punishTemplate.duration()
                    .toString(),
                "&7Punishment will end on:",
                "&7" + Settings.Advanced
                    .formatDate(
                        System.currentTimeMillis()
                        + punishTemplate
                            .duration()
                            .toMs()),
                "",
                "&7Click to change")
                .slot(CHOOSE_DURATION_SLOT)
                .actionHandler("Duration")
        );
      } else {
        set(
            Item.of(
                ItemType.CLOCK,
                "&6" + DURATION,
                CHOOSE_DURATION_LORE)
                .slot(CHOOSE_DURATION_SLOT)
                .actionHandler("Duration")
        );
      }
    }

    // Type | "Type"
    {
      set(
          Item.of(ItemUtil.forPunishType(punishTemplate.punishType()))
              .name("&6" + CHANGE_TYPE)
              .lore(Arrays.asList(
                  "&7Change the type",
                  "&7of the punish",
                  "&7" + CURRENTLY + ": " + punishTemplate.punishType().localized()))
              .slot(CHOOSE_TYPE_SLOT)
              .actionHandler("Type")
      );
    }

    // Reason | "Reason"
    {
      if (punishTemplate().reason() != null) {
        set(
            Item
                .of(
                    ItemSettings.REASON_ITEM.itemType(),
                    "&6" + REASON,
                    "&7Choose different reason",
                    "&7Current: " + punishTemplate.reason())
                .slot(CHOOSE_REASON_SLOT)
                .actionHandler("Reason")
        );

      } else {
        set(
            Item
                .of(ItemSettings.REASON_ITEM.itemType())
                .name("&6" + REASON)
                .lore(
                    "&7Choose the",
                    "&7reason of the",
                    "&7punish")
                .slot(CHOOSE_REASON_SLOT)
                .actionHandler("Reason")
        );
      }
    }

    // Permission | "Permission"
    {
      set(
          Item
              .of(
                  ItemType.PAPER,
                  "&6" + CHOOSE_PERMISSION,
                  " ",
                  CURRENTLY + ": " + punishTemplate().permission())
              .slot(CHOOSE_PERMISSION_SLOT)
              .actionHandler("Permission")
      );
    }

    // Silent | "Silent"
    {
      if (punishTemplate().silent()) {
        set(
            Item
                .ofString(ItemSettings.ENABLED.itemType())
                .name("&6" + SILENT)
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
                .name("&6" + MAKE_SILENT)
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
      if (punishTemplate().superSilent()) {
        set(
            Item
                .ofString(ItemSettings.ENABLED.itemType())
                .name("&6" + SUPER_SILENT)
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
                .name("&6" + MAKE_SUPER_SILENT)
                .lore(
                    MAKE_SUPER_SILENT_LORE
                )
                .slot(MAKE_SUPER_SILENT_SLOT)
                .actionHandler("SuperSilent")
        );
      }
    }

    // Apply | "Apply"
    {
      set(
          Item
              .of(
                  ItemSettings.APPLY_ITEM.itemType(),
                  "&a" + APPLY,
                  "&7" + APPLY + "template")
              .slot(APPLY_SLOT)
              .actionHandler("Apply")
      );
    }
  }

  @Override
  public void reDisplay() {
    showTo(getPlayer(), punishTemplate());
  }

  @Override
  public void registerActionHandlers() {

    registerActionHandler("Apply", apply -> {
      PunishTemplateBrowser.showTo(player);
      return CallResult.DENY_GRABBING;
    });

    // Duration
    registerActionHandler("Duration", (
        duration -> {

          new AbstractDurationChooser(PunishTemplateCreatorMenu.this) {
            @Override
            protected void confirm() {
              punishTemplate.duration(PunishDuration.of(ms));
            }
          }.displayTo(player);

          return CallResult.DENY_GRABBING;
        }));

    // Type
    registerActionHandler("Type", (
        type -> {
          new AbstractPunishTypeBrowser(PunishTemplateCreatorMenu.this) {
            @Override
            protected void onClick(
                final ClickType clickType,
                final PunishType punishType) {
              punishTemplate.punishType(punishType);
              punishTemplate.forceReload();

              punishTemplate.punishType();
              PunishTemplateCreatorMenu.this.displayTo(player);
            }
          }.displayTo(player);

          return CallResult.DENY_GRABBING;
        }));

    // Reason
    registerActionHandler("Reason", (
        reason -> {

          InventoryModule.closeAllInventories(getPlayer());
          TemplateReasonConversation.create(getPlayer(), this).start();
          return CallResult.DENY_GRABBING;
        }));

    // Permission
    registerActionHandler("Permission", (
        permission -> {
          InventoryModule.closeAllInventories(getPlayer());
          TemplatePermissionConversation.create(getPlayer(), this).start();
          return CallResult.DENY_GRABBING;
        }));

    // Silent
    registerActionHandler("Silent", (
        silent -> {
          if (punishTemplate.superSilent()) {
            animateTitle(ALREADY_SUPER_SILENT);
            return CallResult.DENY_GRABBING;
          }

          punishTemplate.silent(!punishTemplate.silent());
          animateTitle(punishTemplate.silent()
              ? "&8silent"
              : "&8" + NOT + " silent");
          build();

          return CallResult.DENY_GRABBING;
        }));

    // Super Silent

    registerActionHandler("SuperSilent", (
        superSilent -> {

          if (punishTemplate.silent()) {
            punishTemplate.silent(false);
          }
          punishTemplate.superSilent(!punishTemplate.superSilent());
          animateTitle(punishTemplate.superSilent()
              ? "&8" + SUPER_SILENT
              : "&c" + NOT + " " + SUPER_SILENT.toLowerCase());
          build();
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

  public void reason(final String reason) {
    punishTemplate.reason(reason);
  }
}
