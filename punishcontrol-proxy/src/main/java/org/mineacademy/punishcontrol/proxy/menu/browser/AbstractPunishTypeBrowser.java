package org.mineacademy.punishcontrol.proxy.menu.browser;

import de.exceptionflug.protocolize.items.ItemStack;
import java.util.Arrays;
import java.util.Collections;
import org.mineacademy.burst.item.Item;
import org.mineacademy.burst.menu.AbstractBrowser;
import org.mineacademy.burst.menu.AbstractMenu;
import org.mineacademy.punishcontrol.core.injector.annotations.Localizable;
import org.mineacademy.punishcontrol.core.punish.PunishType;
import org.mineacademy.punishcontrol.proxy.ItemUtil;

@Localizable
public abstract class AbstractPunishTypeBrowser extends AbstractBrowser<PunishType> {

  @Localizable("Parts.Select")
  private static String SELECT = "Select";

  protected AbstractPunishTypeBrowser(final AbstractMenu parent) {
    super("PunishTypes", parent, Arrays.asList(PunishType.values()));
  }

  @Override
  protected ItemStack convertToItemStack(final PunishType item) {
    return Item
        .of(ItemUtil.forPunishType(item))
        .name(item.localized())
        .lore(Collections.singletonList("&7" + SELECT + " " + item.localized()))
        .build();
  }

  @Override
  public void reDisplay() {
    throw new AbstractMethodError("Not implemented");
  }
}
