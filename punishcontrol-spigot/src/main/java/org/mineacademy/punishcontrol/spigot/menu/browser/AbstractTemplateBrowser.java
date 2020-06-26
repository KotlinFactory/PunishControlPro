package org.mineacademy.punishcontrol.spigot.menu.browser;

import java.util.Arrays;
import lombok.val;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NonNls;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.punishcontrol.core.injector.annotations.Localizable;
import org.mineacademy.punishcontrol.core.punish.template.PunishTemplate;
import org.mineacademy.punishcontrol.core.punish.template.PunishTemplates;
import org.mineacademy.punishcontrol.core.setting.Replacer;
import org.mineacademy.punishcontrol.spigot.menu.AbstractConfirmMenu;
import org.mineacademy.punishcontrol.spigot.menus.template.PunishTemplateCreatorMenu;
import org.mineacademy.punishcontrol.spigot.util.ItemStacks;

@Localizable
public abstract class AbstractTemplateBrowser
    extends AbstractBrowser<PunishTemplate> {

  // ----------------------------------------------------------------------------------------------------
  // Localization
  // ----------------------------------------------------------------------------------------------------

  @NonNls
  @Localizable("Parts.Yes")
  private static String YES = "Yes";
  @NonNls
  @Localizable("Parts.No")
  private static String NO = "No";
  @Localizable("Menu.Template.Lore")
  private static org.mineacademy.punishcontrol.core.setting.Replacer replacer = Replacer.of(
      "&6Type: &7{type}",
      "&6Duration: &7{duration}",
      "&6Reason: &7{reason}",
      "&6Permission: &7{permission}",
      "&6Silent: {silent}",
      "&6Super-Silent: {super-silent}",
      "&6Access: &7{access}",
      "Right-Click to remove"
                                                                                           );

  // ----------------------------------------------------------------------------------------------------
  // Constructors & Fields
  // ----------------------------------------------------------------------------------------------------

  protected AbstractTemplateBrowser(final Menu parent) {
    super(parent, PunishTemplates.list());
    setTitle("&8Choose template");
  }

  @Override
  protected final ItemStack convertToItemStack(final PunishTemplate item) {
    final val creator = ItemCreator
        .of(ItemStacks.forPunishType(item.punishType()));
    creator.name("&6" + item.name());

    replacer.find(
        "type",
        "reason",
        "duration",
        "permission",
        "silent",
        "super-silent",
        "access");

    replacer.replace(
        item.punishType(),
        item.reason(),
        item.duration(),
        item.permission(),
        item.silent() ? "&a" + YES : "&c" + NO,
        item.superSilent() ? "&a" + YES : "&c" + NO,
        PunishTemplates.hasAccess(getViewer().getUniqueId(), item)
            ? "&a" + YES
            : "&c" + NO
                    );

    creator.lores(Arrays.asList(replacer.replacedMessage()));
    return creator.build().makeMenuTool();
  }

  /**
   * Default implementation might be overridden
   */
  @Override
  protected void onPageClick(
      final Player player,
      final PunishTemplate punishTemplate,
      final ClickType click) {
    if (click.isRightClick()) {

      new AbstractConfirmMenu(this) {
        @Override
        public void onConfirm() {
          //Unregistering
          PunishTemplates.unregister(punishTemplate);
          //Deleting file so it won't be re-registered after a server-restart
          punishTemplate.getFile().delete();
        }
      }.displayTo(getViewer());
      return;
    }

    PunishTemplateCreatorMenu
        .fromExisting(punishTemplate)
        .displayTo(getViewer());
  }
}
