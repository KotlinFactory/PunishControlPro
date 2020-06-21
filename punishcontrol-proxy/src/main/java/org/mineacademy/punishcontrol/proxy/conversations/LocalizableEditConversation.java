package org.mineacademy.punishcontrol.proxy.conversations;

import de.exceptionflug.protocolize.inventory.InventoryModule;
import lombok.NonNull;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.bfo.conversation.SimpleConversation;
import org.mineacademy.bfo.conversation.SimplePrompt;
import org.mineacademy.punishcontrol.core.localization.LocalizableEditor;
import org.mineacademy.punishcontrol.proxy.menus.LocalizableEditorMenu;

public final class LocalizableEditConversation extends SimpleConversation {

  private final LocalizableEditor localizableEditor;
  private final int index;

  private LocalizableEditConversation(
      @NonNull final ProxiedPlayer player,
      @NonNull final LocalizableEditor localizableEditor,
      final int slot) {
    super(player);
    this.localizableEditor = localizableEditor;
    this.index = slot;
  }

  public static LocalizableEditConversation create(
      @NonNull final ProxiedPlayer player,
      @NonNull final LocalizableEditor localizableEditor,
      final int slot) {
    InventoryModule.closeAllInventories(player);
    return new LocalizableEditConversation(player, localizableEditor, slot);
  }

  @Override
  protected SimplePrompt getFirstPrompt() {
    return new LocalizableEditPrompt(this);
  }

  private class LocalizableEditPrompt extends SimplePrompt {

    public LocalizableEditPrompt(SimpleConversation parent) {
      super(parent);
    }

    @Override
    public @NotNull String getPrompt() {
      return "&7Please enter a value";
    }

    @Override
    protected @Nullable SimplePrompt acceptValidatedInput(String input) {
      localizableEditor.set(index, input);
//      System.out.println(localizableEditor.value());
      localizableEditor.save();
      LocalizableEditorMenu.showTo(getPlayer(), localizableEditor.localizable());
      return null;
    }
  }
}
