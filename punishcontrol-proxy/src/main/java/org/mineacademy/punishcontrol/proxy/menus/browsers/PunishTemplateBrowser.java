package org.mineacademy.punishcontrol.proxy.menus.browsers;

import de.exceptionflug.mccommons.inventories.api.CallResult;
import de.exceptionflug.protocolize.inventory.InventoryModule;
import javax.inject.Inject;
import lombok.NonNull;
import lombok.val;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.mineacademy.burst.item.Item;
import org.mineacademy.burst.util.Scheduler;
import org.mineacademy.punishcontrol.core.settings.ItemSettings;
import org.mineacademy.punishcontrol.proxy.DaggerProxyComponent;
import org.mineacademy.punishcontrol.proxy.conversations.AddTemplateConversation;
import org.mineacademy.punishcontrol.proxy.menu.browser.AbstractTemplateBrowser;

public class PunishTemplateBrowser extends AbstractTemplateBrowser {

  public static void showTo(@NonNull final ProxiedPlayer player) {
    Scheduler.runAsync(() -> {
      final val browser = DaggerProxyComponent.create()
          .punishTemplateBrowser();
      browser.displayTo(player);
    });
  }

  @Inject
  public PunishTemplateBrowser(final SettingsBrowser settingsBrowser) {
    super(settingsBrowser);
    setTitle("&8PunishTemplates");
  }

  // ----------------------------------------------------------------------------------------------------
  // Overridden methods
  // ----------------------------------------------------------------------------------------------------

  @Override
  public void updateInventory() {
    super.updateInventory();

    // Add | "Add"
    {
      set(
          Item
              .ofString(ItemSettings.ADD_ITEM.itemType())
              .name("&aAdd template")
              .lore("&7Click here to", "&7Add custom templates")
              .slot(getMaxSize() - 5)
              .actionHandler("Add")
      );
    }
  }

  @Override
  public void reDisplay() {
    showTo(getPlayer());
  }

  @Override
  public void registerActionHandlers() {
    registerActionHandler("Add", (add -> {

      InventoryModule.closeAllInventories(getPlayer());
      AddTemplateConversation.create(getPlayer()).start();
      return CallResult.DENY_GRABBING;
    }));
  }

  @Override
  protected String[] getInfo() {
    return new String[]{
        "&7Menu to view templates",
        "&7You can edit",
        "&7the templates, too"
    };
  }
}
