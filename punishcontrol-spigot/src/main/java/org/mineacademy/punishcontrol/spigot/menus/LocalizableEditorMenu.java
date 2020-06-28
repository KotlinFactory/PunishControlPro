package org.mineacademy.punishcontrol.spigot.menus;

import java.util.Arrays;
import javax.inject.Inject;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NonNls;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.punishcontrol.core.localization.Localizable;
import org.mineacademy.punishcontrol.core.localization.LocalizableEditor;
import org.mineacademy.punishcontrol.core.settings.ItemSettings;
import org.mineacademy.punishcontrol.spigot.DaggerSpigotComponent;
import org.mineacademy.punishcontrol.spigot.conversations.LocalizableAddConversation;
import org.mineacademy.punishcontrol.spigot.conversations.LocalizableEditConversation;
import org.mineacademy.punishcontrol.spigot.menus.browsers.LocalizablesBrowser;

@org.mineacademy.punishcontrol.core.injector.annotations.Localizable
public final class LocalizableEditorMenu extends Menu {

  @NonNls private static final String VALUE = "Value";
  @NonNls private static final String CLICK_TO_EDIT = "Click to edit";
  private static final String ADD_A_NEW_ = "Add a new ";

  // ----------------------------------------------------------------------------------------------------
  // Localization
  // ----------------------------------------------------------------------------------------------------

  @org.mineacademy.punishcontrol.core.injector.annotations.Localizable("Menu.Proxy.LocalizableEditorMenu.Edit")
  private static String EDIT_LOCALIZABLE = "Edit localizable";

  @org.mineacademy.punishcontrol.core.injector.annotations.Localizable("Parts.Part")
  private static String PART = "Part";
  @org.mineacademy.punishcontrol.core.injector.annotations.Localizable("Parts.Add")
  private static String ADD = "Add";
  private final Localizable localizable;
  private final LocalizableEditor localizabeEditor;

  // ----------------------------------------------------------------------------------------------------
  // Building & Displaying
  // ----------------------------------------------------------------------------------------------------

  public static LocalizableEditorMenu.Builder builder() {
    return DaggerSpigotComponent.create().localizableEditorMenuBuilder();
  }

  public static void showTo(final Player player, final Localizable localizable) {
    builder().build(localizable).displayTo(player, true);
  }

  // ----------------------------------------------------------------------------------------------------
  // Fields & Constructor's
  // ----------------------------------------------------------------------------------------------------

  private LocalizableEditorMenu(
      @NonNull final LocalizablesBrowser localizablesBrowser,
      @NonNull final Localizable localizable) {
    super(localizablesBrowser);
    setSize(9 * 3);
    this.localizable = localizable;
    this.localizabeEditor = LocalizableEditor.builder().build(localizable);
    setTitle("&8" + EDIT_LOCALIZABLE);
  }

  // ----------------------------------------------------------------------------------------------------
  // Overridden methods from AbstractMenu (Registering ActionHandlers, etc)
  // ----------------------------------------------------------------------------------------------------

  @Override
  public ItemStack getItemAt(int slot) {
    if (slot < localizable.value().size() - 1)
      return ItemCreator
          .of(CompMaterial.PAPER)
          .name("&3" + PART + "&7-" + (slot + 1))
          .lores(Arrays.asList(
              " ",
              "&7" + VALUE + ": " + localizable.value().get(slot),
              " ",
              "&7" + CLICK_TO_EDIT))
          .build()
          .make();

    if (slot == 24 && localizabeEditor.canMultiline())
      return ItemCreator
          .ofString(ItemSettings.ADD_ITEM.itemType())
          .name("&6" + ADD)
          .lore(ADD_A_NEW_)
          .build()
          .make();
    return super.getItemAt(slot);
  }

  @Override
  protected void onMenuClick(
      Player player,
      int slot,
      ItemStack clicked) {

    // Create & start edit conversation
    if (slot < localizable.value().size() - 1)
      LocalizableEditConversation.create(localizabeEditor, slot).start(player);

    // Create & start add conversation
    if (slot == 24)
      LocalizableAddConversation.create(localizabeEditor).start(player);

    super.onMenuClick(player, slot, clicked);
  }

  //  @Override
//  public void reDisplay() {
//    showTo(player, localizable);
//  }

  @Override
  protected String[] getInfo() {
    return new String[]{
        "",
        ""
    };
  }

  // ----------------------------------------------------------------------------------------------------
  // Builder
  // ----------------------------------------------------------------------------------------------------

  public static final class Builder {

    private final LocalizablesBrowser localizablesBrowser;

    @Inject
    public Builder(@NonNull LocalizablesBrowser localizablesBrowser) {
      this.localizablesBrowser = localizablesBrowser;
    }

    public LocalizableEditorMenu build(@NonNull final Localizable localizable) {
      return new LocalizableEditorMenu(localizablesBrowser, localizable);
    }
  }
}
