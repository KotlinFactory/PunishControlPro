package org.mineacademy.punishcontrol.proxy.menus.browsers;

import de.exceptionflug.mccommons.inventories.api.ClickType;
import de.exceptionflug.protocolize.items.ItemStack;
import java.util.Collection;
import org.mineacademy.burst.menu.AbstractBrowser;
import org.mineacademy.burst.menu.BurstMenu;

public final class LanguageBrowser extends AbstractBrowser<String> {

  protected LanguageBrowser(String name,
      BurstMenu parent,
      Collection<String> content) {
    super(name, parent, content);
  }

  @Override
  protected void onClick(ClickType clickType, String s) {

  }

  @Override
  protected ItemStack convertToItemStack(String s) {
    return null;
  }

  @Override
  public void reDisplay() {

  }
}
