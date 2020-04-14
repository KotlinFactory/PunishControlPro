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
import org.mineacademy.punishcontrol.core.settings.Settings;
import org.mineacademy.punishcontrol.spigot.DaggerSpigotComponent;
import org.mineacademy.punishcontrol.spigot.Scheduler;
import org.mineacademy.punishcontrol.spigot.conversation.template.TemplatePermissionConversation;
import org.mineacademy.punishcontrol.spigot.conversation.template.TemplateReasonConversation;
import org.mineacademy.punishcontrol.spigot.menu.AbstractDurationChooser;
import org.mineacademy.punishcontrol.spigot.menu.AbstractPunishTypeBrowser;
import org.mineacademy.punishcontrol.spigot.menus.browsers.PunishTemplateBrowser;
import org.mineacademy.punishcontrol.spigot.util.ItemStacks;

@Accessors(fluent = true)
public final class PunishTemplateCreatorMenu extends Menu {

  private static final int SIZE = 9 * 6;
  private static final int CHOOSE_REASON_SLOT = 19;
  private static final int CHOOSE_PERMISSION_SLOT = 25;

  @Getter
  private final PunishTemplate template;
  private final Button applyAndSaveTemplate;
  private final Button chooseDuration;
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
    template = punishTemplate;
    setSize(SIZE);

    chooseDuration = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player, final Menu menu, final ClickType click) {
        new AbstractDurationChooser(menu) {
          @Override
          protected void confirm() {
            template.duration(PunishDuration.of(ms));
            PunishTemplateCreatorMenu.this.redraw();
          }
        }.displayTo(player);
      }

      @Override
      public ItemStack getItem() {
        if (template.duration() != null) {
          return ItemCreator.of(CompMaterial.CLOCK,
              "&6Duration",
              "&7Currently: ",
              "&7" + template.duration().toString(),
              "&7Punish will end on:",
              "&7" + Settings.Advanced
                  .formatDate(
                      System.currentTimeMillis() + template.duration()
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
            .of(CompMaterial.EMERALD_BLOCK,
                "&aApply",
                "&7Apply template")
            .build()
            .makeMenuTool();
      }
    };
  }


  @Override
  protected String[] getInfo() {
    return new String[]{"&7Menu to", "&7edit templates"};
  }

  public void setReason(final String reason) {
    template.reason(reason);
  }

  @Override
  public ItemStack getItemAt(final int slot) {

    if (slot == 4) {
      return ItemCreator
          .of(ItemStacks.forPunishType(template.punishType()))
          .name("&6Change type")
          .lores(Arrays.asList(
              "&7Change the type",
              "&7of the punish",
              "&7Currently: " + template.punishType().localized()))
          .build()
          .makeMenuTool();
    }

    if (slot == CHOOSE_REASON_SLOT) {
      if (template.reason() != null) {
        return ItemCreator.of(CompMaterial.BOOK,
            "&6Reason",
            "&7Choose different reason",
            "&7Current: " + template.reason())
            .build()
            .make();
      }

      return ItemCreator.of(CompMaterial.BOOK,
          "&6Reason",
          "&7Choose the",
          "&7reason of the",
          "&7punish")
          .build()
          .make();
    }

    if (slot == 22) {
      return applyAndSaveTemplate.getItem();
    }

    if (slot == CHOOSE_PERMISSION_SLOT) {
      return ItemCreator
          .of(CompMaterial.PAPER,
              "&6Choose permission",
              " ",
              "Currently: " + template().permission())
          .build()
          .makeMenuTool();
    }

    if (slot == 40) {
      return chooseDuration.getItem();
    }

    return null;
  }

  @Override
  protected void onMenuClick(
      final Player player,
      final int slot,
      final ItemStack clicked) {

    if (slot == 4) {
      new AbstractPunishTypeBrowser(this) {
        @Override
        protected void onClick(final PunishType punishType) {
          template.punishType(punishType);
          template.forceReload();

          template.punishType();
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
