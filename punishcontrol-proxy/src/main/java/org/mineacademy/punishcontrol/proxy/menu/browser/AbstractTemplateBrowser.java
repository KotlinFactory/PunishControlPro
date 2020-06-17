package org.mineacademy.punishcontrol.proxy.menu.browser;

import de.exceptionflug.mccommons.inventories.api.ClickType;
import de.exceptionflug.protocolize.items.ItemStack;
import java.util.Arrays;
import lombok.val;
import org.jetbrains.annotations.NonNls;
import org.mineacademy.burst.item.Item;
import org.mineacademy.burst.menu.AbstractBrowser;
import org.mineacademy.burst.menu.BurstMenu;
import org.mineacademy.punishcontrol.core.injector.annotations.Localizable;
import org.mineacademy.punishcontrol.core.punish.template.PunishTemplate;
import org.mineacademy.punishcontrol.core.punish.template.PunishTemplates;
import org.mineacademy.punishcontrol.core.setting.Replacer;
import org.mineacademy.punishcontrol.proxy.ItemUtil;
import org.mineacademy.punishcontrol.proxy.menu.AbstractConfirmMenu;
import org.mineacademy.punishcontrol.proxy.menus.template.PunishTemplateCreatorMenu;

@Localizable
public abstract class AbstractTemplateBrowser
    extends AbstractBrowser<PunishTemplate> {

  @NonNls
  @Localizable("Parts.Yes")
  private static String YES = "Yes";
  @NonNls
  @Localizable("Parts.No")
  private static String NO = "No";
  @Localizable("Menu.Template.Lore")
  private static Replacer replacer = Replacer.of(
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

  protected AbstractTemplateBrowser(final BurstMenu parent) {
    super("TemplateBrowser", parent, PunishTemplates.list());
    setTitle("&8Punish templates");
  }

  @Override
  protected final ItemStack convertToItemStack(final PunishTemplate item) {
    final val creator = Item
        .of(ItemUtil.forPunishType(item.punishType()));
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
        item.duration().toString(),
        item.permission(),
        item.silent()
            ? "&a" + YES
            : "&c" + NO,
        item.superSilent()
            ? "&a" + YES
            : "&c" + NO,
        PunishTemplates.hasAccess(getPlayer().getUniqueId(), item)
            ? "&a" + YES
            : "&c" + NO
    );

    creator.lore(Arrays.asList(replacer.replacedMessage()));
    return creator.build();
  }

  @Override
  protected void onClick(final ClickType clickType,
      final PunishTemplate punishTemplate) {
    if (clickType == ClickType.RIGHT_CLICK) {
      new AbstractConfirmMenu(this) {
        @Override
        public void onConfirm() {
          // Unregistering
          PunishTemplates.unregister(punishTemplate);
          // Deleting file so it won't be re-registered after a server-restart
          punishTemplate.getFile().delete();
        }
      }.displayTo(getPlayer());
      return;
    }

    PunishTemplateCreatorMenu
        .fromExisting(punishTemplate)
        .displayTo(getPlayer());
  }

  @Override
  public void reDisplay() {
    throw new AbstractMethodError("Not implemented");
  }
}
