package org.mineacademy.punishcontrol.spigot.menus.browsers;

import java.util.Arrays;
import javax.inject.Inject;
import lombok.NonNull;
import lombok.val;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NonNls;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.punishcontrol.core.injector.annotations.Localizable;
import org.mineacademy.punishcontrol.core.settings.ItemSettings;
import org.mineacademy.punishcontrol.spigot.DaggerSpigotComponent;
import org.mineacademy.punishcontrol.spigot.Scheduler;
import org.mineacademy.punishcontrol.spigot.conversations.AddTemplateConversation;
import org.mineacademy.punishcontrol.spigot.menu.browser.AbstractTemplateBrowser;

public class PunishTemplateBrowser extends AbstractTemplateBrowser {

  @NonNls
  @Localizable("Menu.Template.Add.Name")
  private static final String ADD_TEMPLATE = "Add template";
  @Localizable("Menu.Template.Add.Lore")
  private static final String[] ADD_LORE = {"&7Click here to", "&7Add custom templates"};
  @Localizable("Menu.Templates.Information")
  private static final String[] MENU_INFORMATION = {
      "&7Menu to view templates",
      "&7You can edit",
      "&7the templates, too"
  };

  @Localizable("Parts.Punish_Templates")
  @NonNls private static final String PUNISH_TEMPLATES = "PunishTemplates";

  // ----------------------------------------------------------------------------------------------------
  // Displaying
  // ----------------------------------------------------------------------------------------------------

  public static void showTo(@NonNull final Player player) {
    Scheduler.runAsync(() -> {
      final val browser = DaggerSpigotComponent.create()
          .punishTemplateBrowser();
      browser.displayTo(player, true);
    });
  }

  // ----------------------------------------------------------------------------------------------------
  // Fields & Constructor's
  // ----------------------------------------------------------------------------------------------------
  private final Button addTemplateButton;

  @Inject
  public PunishTemplateBrowser(final SettingsBrowser parent) {
    super(parent);

    setTitle("&8" + PUNISH_TEMPLATES);
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
            .ofString(ItemSettings.ADD_ITEM.itemType())
            .name("&a" + ADD_TEMPLATE)
            .lores(Arrays.asList(ADD_LORE))
            .build()
            .makeMenuTool();
      }
    };
  }

  @Override
  public ItemStack getItemAt(final int slot) {
    final ItemStack result = super.getItemAt(slot);
    if (result != null)
      return result;
    if (slot == getInfoButtonPosition() + 4)
      return addTemplateButton.getItem();

    return null;
  }

  @Override
  protected String[] getInfo() {
    return MENU_INFORMATION;
  }
}
