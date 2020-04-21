package org.mineacademy.punishcontrol.spigot.menus.browsers;

import java.util.Arrays;
import javax.inject.Inject;
import lombok.NonNull;
import lombok.val;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.punishcontrol.spigot.DaggerSpigotComponent;
import org.mineacademy.punishcontrol.spigot.Scheduler;
import org.mineacademy.punishcontrol.spigot.conversation.AddTemplateConversation;
import org.mineacademy.punishcontrol.spigot.menu.browser.AbstractTemplateBrowser;
import org.mineacademy.punishcontrol.spigot.menus.MainMenu;

public class PunishTemplateBrowser extends AbstractTemplateBrowser {

  private final Button addTemplateButton;

  public static void showTo(@NonNull final Player player) {
    Scheduler.runAsync(() -> {
      final val browser = DaggerSpigotComponent.create()
          .punishTemplateBrowser();
      browser.displayTo(player, true);
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
  protected String[] getInfo() {
    return new String[]{
        "&7Menu to view templates",
        "&7You can edit",
        "&7the templates, too"
    };
  }
}
