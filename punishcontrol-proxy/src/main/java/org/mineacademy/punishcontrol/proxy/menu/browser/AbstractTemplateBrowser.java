package org.mineacademy.punishcontrol.proxy.menu.browser;

import de.exceptionflug.protocolize.items.ItemStack;
import java.util.Arrays;
import lombok.val;
import org.mineacademy.burst.item.Item;
import org.mineacademy.burst.menu.AbstractBrowser;
import org.mineacademy.burst.menu.BurstMenu;
import org.mineacademy.punishcontrol.core.punish.template.PunishTemplate;
import org.mineacademy.punishcontrol.core.punish.template.PunishTemplates;
import org.mineacademy.punishcontrol.core.settings.Replacer;
import org.mineacademy.punishcontrol.proxy.ItemUtil;

public abstract class AbstractTemplateBrowser
    extends AbstractBrowser<PunishTemplate> {

  protected AbstractTemplateBrowser(final BurstMenu parent) {
    super("TemplateBrowser", parent, PunishTemplates.list());
  }

  @Override
  protected final ItemStack convertToItemStack(final PunishTemplate item) {
    final val creator = Item
        .of(ItemUtil.forPunishType(item.punishType()));
    creator.name("&6" + item.name());

    final Replacer replacer = Replacer.of(
        "&6Type: &7{type}",
        "&6Duration: &7{duration}",
        "&6Reason: &7{reason}",
        "&6Permission: &7{permission}",
        "&6Silent: {silent}",
        "&6Super-Silent: {super-silent}",
        "&6Access: &7{access}",
        "Right-Click to remove"
    );

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
        item.silent() ? "&aYes" : "&cNo",
        item.superSilent() ? "&aYes" : "&cNo",
        PunishTemplates.hasAccess(getPlayer().getUniqueId(), item)
            ? "&aYes"
            : "&cNo"
    );

    creator.lore(Arrays.asList(replacer.replacedMessage()));
    return creator.build();
  }

  @Override
  public void reDisplay() {
    throw new AbstractMethodError("Not implemented");
  }
}
