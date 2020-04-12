package org.mineacademy.punishcontrol.spigot.menu;

import java.util.Arrays;
import lombok.val;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.model.Replacer;
import org.mineacademy.punishcontrol.core.punish.template.PunishTemplate;
import org.mineacademy.punishcontrol.core.punish.template.PunishTemplates;
import org.mineacademy.punishcontrol.spigot.util.ItemStacks;

public abstract class AbstractTemplateBrowser extends
    AbstractBrowser<PunishTemplate> {

  protected AbstractTemplateBrowser(final Menu parent) {
    super(parent, PunishTemplates.list());
  }

  @Override
  protected final ItemStack convertToItemStack(final PunishTemplate item) {
    final val creator = ItemCreator
        .of(ItemStacks.forPunishType(item.punishType()));
    creator.name("&6" + item.name());

    final Replacer replacer = Replacer.of(
        "&6Type: &7{type}",
        "&6Duration: &7{duration}",
        "&6Reason: &7{reason}",
        "&6Permission: &7{permission}",
        "&6Silent: {silent}",
        "&6Super-Silent: {super-silent}",
        "&6Access: &7{access}"
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
        PunishTemplates.hasAccess(getViewer().getUniqueId(), item)
            ? "&aYes"
            : "&cNo"
    );

    creator.lores(Arrays.asList(replacer.getReplacedMessage()));
    return creator.build().makeMenuTool();
  }

  @Override
  protected final void onPageClick(
      final Player player,
      final PunishTemplate punishTemplate,
      final ClickType click) {
    onClick(punishTemplate);
  }

  protected abstract void onClick(final PunishTemplate punishTemplate);
}
