package org.mineacademy.punishcontrol.proxy.menus.template;


import de.exceptionflug.mccommons.inventories.api.CallResult;
import de.exceptionflug.mccommons.inventories.api.ClickType;
import de.exceptionflug.protocolize.items.ItemType;
import java.io.File;
import java.util.Arrays;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.val;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.mineacademy.bfo.plugin.SimplePlugin;
import org.mineacademy.burst.item.Item;
import org.mineacademy.burst.menu.AbstractMenu;
import org.mineacademy.burst.util.Scheduler;
import org.mineacademy.punishcontrol.core.punish.PunishDuration;
import org.mineacademy.punishcontrol.core.punish.PunishType;
import org.mineacademy.punishcontrol.core.punish.template.PunishTemplate;
import org.mineacademy.punishcontrol.core.punish.template.PunishTemplates;
import org.mineacademy.punishcontrol.core.settings.Settings;
import org.mineacademy.punishcontrol.proxy.DaggerProxyComponent;
import org.mineacademy.punishcontrol.proxy.ItemUtil;
import org.mineacademy.punishcontrol.proxy.menu.AbstractDurationChooser;
import org.mineacademy.punishcontrol.proxy.menu.browser.AbstractPunishTypeBrowser;

@Accessors(fluent = true)
public final class PunishTemplateCreatorMenu extends AbstractMenu {

  private static final int SIZE = 9 * 6;
  private static final int CHOOSE_REASON_SLOT = 19;
  private static final int CHOOSE_PERMISSION_SLOT = 25;
  public static final int MAKE_SILENT_SLOT = 29;
  public static final int MAKE_SUPER_SILENT_SLOT = 33;
  public static final int CHOOSE_TYPE_SLOT = 4;
  public static final int CHOOSE_DURATION_SLOT = 40;
  public static final int APPLY_SLOT = 22;

  @Getter
  private final PunishTemplate punishTemplate;

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

  private PunishTemplateCreatorMenu(
      @NonNull final PunishTemplate punishTemplate) {
    super(
        "PunishTemplateCreatorMenu",
        DaggerProxyComponent.create().punishTemplateBrowser(),
        SIZE
    );
    this.punishTemplate = punishTemplate;

    setTitle("&8Template Creator");
  }

  // ----------------------------------------------------------------------------------------------------
  // Overridden methods
  // ----------------------------------------------------------------------------------------------------


  @Override
  public void updateInventory() {
    super.updateInventory();

    // Duration | "Duration"
    {
      if (punishTemplate.duration() != null) {
        set(
            Item.of(ItemType.CLOCK,
                "&6Duration",
                "&7Currently: ",
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
            Item.of(ItemType.CLOCK,
                "&6Duration",
                "&7Choose the",
                "&7duration of the",
                "&7punish")
                .slot(CHOOSE_DURATION_SLOT)
                .actionHandler("Duration")
        );
      }
    }

    // Type | "Type"
    {
      set(
          Item.of(ItemUtil.forPunishType(punishTemplate.punishType()))
              .name("&6Change type")
              .lore(Arrays.asList(
                  "&7Change the type",
                  "&7of the punish",
                  "&7Currently: " + punishTemplate.punishType().localized()))
              .slot(CHOOSE_TYPE_SLOT)
              .actionHandler("Type")
      );
    }

    // Reason | "Reason"
    {
      if (punishTemplate().reason() != null) {
        set(
            Item
                .of(ItemType.BOOK,
                    "&6Reason",
                    "&7Choose different reason",
                    "&7Current: " + punishTemplate.reason())
                .slot(CHOOSE_REASON_SLOT)
                .actionHandler("Reason")
        );

      } else {
        set(
            Item
                .of(ItemType.BOOK)
                .name("&6Reason")
                .lore("&6Reason",
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
              .of(ItemType.PAPER,
                  "&6Choose permission",
                  " ",
                  "Currently: " + punishTemplate().permission())
              .slot(CHOOSE_PERMISSION_SLOT)
              .actionHandler("Permission")
      );
    }

    // Silent | "Silent"
    {
      if (punishTemplate().silent()) {
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
      if (punishTemplate().superSilent()) {
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

    // Apply | "Apply"
    {
      set(
          Item
              .of(
                  ItemType.EMERALD_BLOCK,
                  "&aApply",
                  "&7Apply template")
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
    // Duration
    registerActionHandler("Duration", (duration -> {

      new AbstractDurationChooser(PunishTemplateCreatorMenu.this) {
        @Override
        protected void confirm() {
          punishTemplate.duration(PunishDuration.of(ms));
        }
      }.displayTo(player);

      return CallResult.DENY_GRABBING;
    }));

    // Type
    registerActionHandler("Type", (type -> {
      new AbstractPunishTypeBrowser(PunishTemplateCreatorMenu.this) {
        @Override
        protected void onClick(final ClickType clickType, final PunishType punishType) {
          punishTemplate.punishType(punishType);
          punishTemplate.forceReload();

          punishTemplate.punishType();
          PunishTemplateCreatorMenu.this.displayTo(player);
        }
      }.displayTo(player);

      return CallResult.DENY_GRABBING;
    }));

    // Reason
    registerActionHandler("Reason", (reason -> {

      //TODO
      //TemplateReasonConversation.create(this).start(getPlayer());
      return CallResult.DENY_GRABBING;
    }));

    // Permission
    registerActionHandler("Permission", (permission -> {
      //TODO
      //TemplatePermissionConversation.create(this).start(getPlayer());
      return CallResult.DENY_GRABBING;
    }));

    // Silent
    registerActionHandler("Silent", (silent -> {
      if (punishTemplate.superSilent()) {
        animateTitle("&cPunishment is already super-silent");
        return CallResult.DENY_GRABBING;
      }

      punishTemplate.silent(!punishTemplate.silent());
      animateTitle(punishTemplate.silent()
          ? "&8silent"
          : "&8not silent");
      build();

      return CallResult.DENY_GRABBING;
    }));

    // Super Silent

    registerActionHandler("SuperSilent", (superSilent -> {

      if (punishTemplate.silent()) {
        punishTemplate.silent(false);
      }
      punishTemplate.superSilent(!punishTemplate.superSilent());
      animateTitle(punishTemplate.superSilent()
          ? "&8asuper-silent"
          : "&cnot super-silent");
      build();
      return CallResult.DENY_GRABBING;
    }));
  }

  @Override
  protected String[] getInfo() {
    return new String[]{"&7Menu to", "&7edit templates"};
  }

  // ----------------------------------------------------------------------------------------------------
  // Internal helper-methods
  // ----------------------------------------------------------------------------------------------------

  public void reason(final String reason) {
    punishTemplate.reason(reason);
  }
}
