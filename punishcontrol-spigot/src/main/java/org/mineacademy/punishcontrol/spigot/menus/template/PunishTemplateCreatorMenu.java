package org.mineacademy.punishcontrol.spigot.menus.template;

import java.io.File;
import java.util.Arrays;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.val;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.punishcontrol.core.punish.PunishDuration;
import org.mineacademy.punishcontrol.core.punish.PunishType;
import org.mineacademy.punishcontrol.core.punish.template.PunishTemplate;
import org.mineacademy.punishcontrol.core.punish.template.PunishTemplates;
import org.mineacademy.punishcontrol.core.settings.ItemSettings;
import org.mineacademy.punishcontrol.core.settings.Settings;
import org.mineacademy.punishcontrol.spigot.DaggerSpigotComponent;
import org.mineacademy.punishcontrol.spigot.Scheduler;
import org.mineacademy.punishcontrol.spigot.conversations.template.TemplatePermissionConversation;
import org.mineacademy.punishcontrol.spigot.conversations.template.TemplateReasonConversation;
import org.mineacademy.punishcontrol.spigot.menu.AbstractDurationChooser;
import org.mineacademy.punishcontrol.spigot.menu.browser.AbstractPunishTypeBrowser;
import org.mineacademy.punishcontrol.spigot.menus.browsers.PunishTemplateBrowser;
import org.mineacademy.punishcontrol.spigot.util.ItemStacks;

@Accessors(fluent = true)
public final class PunishTemplateCreatorMenu extends Menu {

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
  private final Button applyAndSaveTemplate;
  private final Button chooseDuration;
  private final Button makeSuperSilent;
  private final Button makeSilent;
//  private final Button fromTemplate;


  public static void showTo(
      final Player player,
      final PunishTemplate punishTemplate) {
    Scheduler.runAsync(() -> fromExisting(punishTemplate).displayTo(player));
  }

  public static void showTo(
      @NonNull final Player player,
      @NonNull final String name) {
    Scheduler.runAsync(() -> {
      final val menu = create(name);
      menu.displayTo(player);
    });
  }

  public static void fromExistingAndShow(
      @NonNull final Player player,
      @NonNull final PunishTemplate punishTemplate) {
    Scheduler.runAsync(() -> {
      final val menu = fromExisting(punishTemplate);

      Scheduler.runSync(() -> menu.displayTo(player));
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
    super(DaggerSpigotComponent.create().punishTemplateBrowser());
    this.punishTemplate = punishTemplate;
    setSize(SIZE);

    setTitle("&8Template Creator");
    chooseDuration = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player, final Menu menu, final ClickType click) {
        new AbstractDurationChooser(menu) {
          @Override
          protected void confirm() {
            PunishTemplateCreatorMenu.this.punishTemplate
                .duration(PunishDuration.of(ms));
            PunishTemplateCreatorMenu.this.redisplay();
          }
        }.displayTo(player);
      }

      @Override
      public ItemStack getItem() {
        if (PunishTemplateCreatorMenu.this.punishTemplate.duration() != null) {
          return ItemCreator.of(CompMaterial.CLOCK,
              "&6Duration",
              "&7Currently: ",
              "&7" + PunishTemplateCreatorMenu.this.punishTemplate.duration()
                  .toString(),
              "&7Punishment will end on:",
              "&7" + Settings.Advanced
                  .formatDate(
                      System.currentTimeMillis()
                          + PunishTemplateCreatorMenu.this.punishTemplate
                          .duration()
                          .toMs()),
              "",
              "&7Click to change")
              .build()
              .make();
        }

        return ItemCreator.of(CompMaterial.CLOCK,
            "&6Duration",
            "&7Choose the",
            "&7duration of the",
            "&7punish")
            .build()
            .make();
      }
    };

    applyAndSaveTemplate = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player,
          final Menu menu,
          final ClickType click) {

        PunishTemplateBrowser.showTo(player);
      }

      @Override
      public ItemStack getItem() {
        return ItemCreator
            .of(ItemSettings.APPLY_ITEM.itemType(),
                "&aApply",
                "&7Apply template")
            .build()
            .makeMenuTool();
      }
    };

    makeSilent = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player, final Menu menu, final ClickType click) {
        if (punishTemplate.superSilent()) {
          animateTitle("&cPunishment is already super-silent");
          return;
        }

        punishTemplate.silent(!punishTemplate.silent());
        restartMenu(punishTemplate.silent()
            ? "&8silent"
            : "&8not silent");
      }

      @Override
      public ItemStack getItem() {
        if (PunishTemplateCreatorMenu.this.punishTemplate.silent()) {
          return ItemCreator
              .of(ItemStacks.greenPane())
              .name("&6Silent")
              .lores(Arrays.asList(
                  "",
                  "&7Click to make",
                  "&7the punishment",
                  "&7not silent"
              ))
              .build()
              .makeMenuTool();
        }
        return ItemCreator
            .of(ItemStacks.redPane())
            .name("&6Make Silent")
            .lores(Arrays.asList(
                "",
                "&7Click to make",
                "&7the punishment",
                "&7silent"
            ))
            .build()
            .makeMenuTool();
      }
    };

    makeSuperSilent = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player, final Menu menu, final ClickType click) {

        if (punishTemplate.silent()) {
          punishTemplate.silent(false);
        }
        punishTemplate.superSilent(!punishTemplate.superSilent());
        restartMenu(punishTemplate.superSilent()
            ? "&8super-silent"
            : "&cnot super-silent");
      }

      @Override
      public ItemStack getItem() {
        if (punishTemplate.superSilent()) {
          return ItemCreator
              .of(ItemStacks.greenPane())
              .name("&6Super-Silent")
              .lores(Arrays.asList(
                  "",
                  "&7Click to make",
                  "&7the punishment",
                  "&7not silent"
              ))
              .build()
              .makeMenuTool();
        }
        return ItemCreator
            .of(ItemStacks.redPane())
            .name("&6Make Silent")
            .lores(Arrays.asList(
                "",
                "&7Click to make",
                "&7the punish",
                "&7super-silent"
            ))
            .build()
            .makeMenuTool();
      }
    };
  }


  public void redisplay() {
    showTo(getViewer(), punishTemplate);
  }

  @Override
  protected String[] getInfo() {
    return new String[]{"&7Menu to", "&7edit templates"};
  }

  public void reason(final String reason) {
    punishTemplate.reason(reason);
  }

  @Override
  public ItemStack getItemAt(final int slot) {

    if (slot == CHOOSE_TYPE_SLOT) {
      return ItemCreator
          .of(ItemStacks.forPunishType(punishTemplate.punishType()))
          .name("&6Change type")
          .lores(Arrays.asList(
              "&7Change the type",
              "&7of the punish",
              "&7Currently: " + punishTemplate.punishType().localized()))
          .build()
          .makeMenuTool();
    }

    if (slot == CHOOSE_REASON_SLOT) {
      if (punishTemplate.reason() != null) {
        return ItemCreator.of(ItemSettings.REASON_ITEM.itemType(),
            "&6Reason",
            "&7Choose different reason",
            "&7Current: " + punishTemplate.reason())
            .build()
            .make();
      }

      return ItemCreator.of(ItemSettings.REASON_ITEM.itemType(),
          "&6Reason",
          "&7Choose the",
          "&7reason of the",
          "&7punish")
          .build()
          .make();
    }

    if (slot == APPLY_SLOT) {
      return applyAndSaveTemplate.getItem();
    }

    if (slot == CHOOSE_PERMISSION_SLOT) {
      return ItemCreator
          .of(CompMaterial.PAPER,
              "&6Choose permission",
              " ",
              "Currently: " + punishTemplate().permission())
          .build()
          .makeMenuTool();
    }

    if (slot == MAKE_SILENT_SLOT) {
      return makeSilent.getItem();
    }

    if (slot == MAKE_SUPER_SILENT_SLOT) {
      return makeSuperSilent.getItem();
    }

    if (slot == CHOOSE_DURATION_SLOT) {
      return chooseDuration.getItem();
    }

    return null;
  }

  @Override
  protected void onMenuClick(
      final Player player,
      final int slot,
      final ItemStack clicked) {

    if (slot == CHOOSE_TYPE_SLOT) {
      new AbstractPunishTypeBrowser(this) {
        @Override
        protected void onClick(final PunishType punishType) {
          punishTemplate.punishType(punishType);
          punishTemplate.forceReload();

          punishTemplate.punishType();
          PunishTemplateCreatorMenu.this.displayTo(player);
        }
      }.displayTo(player);
    }

    if (slot == CHOOSE_REASON_SLOT) {
      getViewer().closeInventory();
      TemplateReasonConversation.create(this).start(getViewer());
    }

    if (slot == CHOOSE_PERMISSION_SLOT) {
      getViewer().closeInventory();
      TemplatePermissionConversation.create(this).start(getViewer());
    }
  }
}
