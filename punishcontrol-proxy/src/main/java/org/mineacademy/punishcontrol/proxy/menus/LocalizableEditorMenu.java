package org.mineacademy.punishcontrol.proxy.menus;

import de.exceptionflug.mccommons.inventories.api.CallResult;
import de.exceptionflug.protocolize.items.ItemType;
import javax.inject.Inject;
import lombok.NonNull;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NonNls;
import org.mineacademy.burst.item.Item;
import org.mineacademy.burst.menu.AbstractMenu;
import org.mineacademy.punishcontrol.core.localization.Localizable;
import org.mineacademy.punishcontrol.core.localization.LocalizableEditor;
import org.mineacademy.punishcontrol.core.settings.ItemSettings;
import org.mineacademy.punishcontrol.proxy.DaggerProxyComponent;
import org.mineacademy.punishcontrol.proxy.conversations.LocalizableAddConversation;
import org.mineacademy.punishcontrol.proxy.conversations.LocalizableEditConversation;
import org.mineacademy.punishcontrol.proxy.menus.browsers.LocalizablesBrowser;

@org.mineacademy.punishcontrol.core.injector.annotations.Localizable
public final class LocalizableEditorMenu extends AbstractMenu {

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

  private LocalizableEditorMenu(
      @NonNull final LocalizablesBrowser localizablesBrowser,
      @NonNull final Localizable localizable) {
    super("LocalizableEditorMenu", localizablesBrowser, 9 * 3);
    this.localizable = localizable;
    this.localizabeEditor = LocalizableEditor.builder().build(localizable);
    setTitle("&8" + EDIT_LOCALIZABLE);
  }

  // ----------------------------------------------------------------------------------------------------
  // Building & Displaying
  // ----------------------------------------------------------------------------------------------------

  public static LocalizableEditorMenu.Builder builder() {
    return DaggerProxyComponent.create().localizableEditorMenuBuilder();
  }

  public static void showTo(final ProxiedPlayer player, final Localizable localizable) {
    builder().build(localizable).displayTo(player);
  }

  // ----------------------------------------------------------------------------------------------------
  // Overridden methods from AbstractMenu (Registering ActionHandlers, etc)
  // ----------------------------------------------------------------------------------------------------

  @Override
  public void updateInventory() {
    super.updateInventory();

    for (int i = 0; i < localizable.value().size(); i++) {
      set(
          Item
              .of(ItemType.PAPER)
              .name("&3" + PART + "&7-" + (i + 1))
              .lore(
                  " ",
                  "&7" + VALUE + ": " + localizable.value().get(i),
                  " ",
                  "&7" + CLICK_TO_EDIT
              )
              .slot(i)
              .actionHandler("Edit")
      );
    }

    if (!localizabeEditor.canMultiline()) {
      return;
    }

    set(
        Item
            .ofString(ItemSettings.ADD_ITEM.itemType())
            .name("&6" + ADD)
            .lore(ADD_A_NEW_)
            .slot(22)
            .actionHandler("Add")
    );
  }

  @Override
  public void registerActionHandlers() {
    super.registerActionHandlers();

    registerActionHandler("Edit", (edit) -> {
      // Open conversation etc...
      LocalizableEditConversation.
          create(player, localizabeEditor, edit.getSlot())
          .start();

      return CallResult.DENY_GRABBING;
    });

    registerActionHandler("Add", (edit) -> {
      // Open conversation etc...
      LocalizableAddConversation
          .create(player, localizabeEditor)
          .start();

      return CallResult.DENY_GRABBING;
    });
  }

  @Override
  public void reDisplay() {
    showTo(player, localizable);
  }

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
