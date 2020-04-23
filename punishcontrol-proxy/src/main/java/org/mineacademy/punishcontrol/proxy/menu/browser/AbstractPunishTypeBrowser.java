package org.mineacademy.punishcontrol.proxy.menu.browser;

import de.exceptionflug.protocolize.items.ItemStack;
import java.util.Arrays;
import java.util.Collections;
import org.mineacademy.burst.item.Item;
import org.mineacademy.burst.menu.AbstractBrowser;
import org.mineacademy.burst.menu.Menu;
import org.mineacademy.punishcontrol.core.punish.PunishType;
import org.mineacademy.punishcontrol.proxy.ItemUtil;

public abstract class AbstractPunishTypeBrowser extends AbstractBrowser<PunishType> {

  protected AbstractPunishTypeBrowser(final Menu parent) {
    super("PunishTypes", parent, Arrays.asList(PunishType.values()));
  }

  @Override
  protected ItemStack convertToItemStack(final PunishType item) {
    return Item
        .of(ItemUtil.forPunishType(item))
        .name(item.localized())
        .lore(Collections.singletonList("&7Select " + item.localized()))
        .build();
  }
}
