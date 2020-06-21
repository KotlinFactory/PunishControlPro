package org.mineacademy.punishcontrol.proxy.conversations;

import de.exceptionflug.protocolize.inventory.InventoryModule;
import lombok.NonNull;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.bfo.conversation.SimpleConversation;
import org.mineacademy.bfo.conversation.SimplePrompt;
import org.mineacademy.punishcontrol.core.localization.LocalizableEditor;

public final class LocalizableAddConversation extends SimpleConversation {

  private final LocalizableEditor localizableEditor;

  private LocalizableAddConversation(
      @NonNull final ProxiedPlayer player,
      @NonNull final LocalizableEditor localizableEditor) {
    super(player);
    this.localizableEditor = localizableEditor;
  }

  public static LocalizableAddConversation create(
      @NonNull final ProxiedPlayer player,
      @NonNull final LocalizableEditor localizableEditor) {
    InventoryModule.closeAllInventories(player);
    return new LocalizableAddConversation(player, localizableEditor);
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
      if (!localizableEditor.canMultiline()) {
        tell("&cCan't add value to non multi line String!");
        return null;
      }

      localizableEditor.add(input);
      localizableEditor.save();
      return null;
    }
  }
}
