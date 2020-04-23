package org.mineacademy.punishcontrol.proxy.menus.browsers;

import de.exceptionflug.mccommons.inventories.api.CallResult;
import de.exceptionflug.mccommons.inventories.api.ClickType;
import de.exceptionflug.protocolize.inventory.InventoryModule;
import de.exceptionflug.protocolize.items.ItemType;
import javax.inject.Inject;
import lombok.NonNull;
import lombok.val;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.mineacademy.burst.item.Item;
import org.mineacademy.burst.util.Scheduler;
import org.mineacademy.punishcontrol.core.punish.template.PunishTemplate;
import org.mineacademy.punishcontrol.proxy.DaggerProxyComponent;
import org.mineacademy.punishcontrol.proxy.menu.browser.AbstractTemplateBrowser;
import org.mineacademy.punishcontrol.proxy.menus.MainMenu;

public class PunishTemplateBrowser extends AbstractTemplateBrowser {

  public static void showTo(@NonNull final ProxiedPlayer player) {
    Scheduler.runAsync(() -> {
      final val browser = DaggerProxyComponent.create()
          .punishTemplateBrowser();
      browser.displayTo(player);
    });
  }

  @Inject
  public PunishTemplateBrowser(final MainMenu mainMenu) {
    super(mainMenu);
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
              .of(ItemType.EMERALD)
              .name("&aAdd template")
              .lore("&7Click here to", "&7Add custom templates")
              .slot(getInfoItemSlot() + 4)
              .actionHandler("Add")
      );
    }
  }

  @Override
  public void registerActionHandlers() {
    registerActionHandler("Add", (add -> {

      InventoryModule.closeAllInventories(getViewer());
      //TODO
      //AddTemplateConversation.create().start(getViewer());
      return CallResult.DENY_GRABBING;
    }));
  }

  @Override
  protected void onClick(final ClickType clickType, final PunishTemplate punishTemplate) {

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
