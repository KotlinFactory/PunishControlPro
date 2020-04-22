package org.mineacademy.punishcontrol.proxy.menus.browsers;

import de.exceptionflug.mccommons.inventories.api.ClickType;
import de.exceptionflug.protocolize.items.ItemStack;
import java.util.Arrays;
import javafx.scene.control.Button;
import javax.inject.Inject;
import lombok.NonNull;
import lombok.val;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.mineacademy.bfo.settings.SimpleLocalization.Player;
import org.mineacademy.burst.menu.Menu;
import org.mineacademy.burst.util.Scheduler;
import org.mineacademy.punishcontrol.core.punish.template.PunishTemplate;
import org.mineacademy.punishcontrol.proxy.DaggerProxyComponent;
import org.mineacademy.punishcontrol.proxy.menu.browser.AbstractTemplateBrowser;
import org.mineacademy.punishcontrol.proxy.menus.MainMenu;

public class PunishTemplateBrowser extends AbstractTemplateBrowser {

  private final Button addTemplateButton;

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
    addTemplateButton = new Button() {
      @Override
      public void onClickedInMenu(
          final Player player, final Menu menu, final ClickType click) {
        player.closeInventory();
        AddTemplateConversation.create().start(getViewer());
      }

      @Override
      public ItemStack getItem() {
        return ItemCreator
            .of(CompMaterial.EMERALD)
            .name("&aAdd template")
            .lores(Arrays.asList("&7Click here to", "&7Add custom templates"))
            .build()
            .makeMenuTool();
      }
    };
  }

  @Override
  public ItemStack getItemAt(final int slot) {
    final ItemStack result = super.getItemAt(slot);
    if (result != null) {
      return result;
    }
    if (slot == getInfoButtonPosition() + 4) {
      return addTemplateButton.getItem();
    }

    return null;
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
