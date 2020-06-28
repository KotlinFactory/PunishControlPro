package org.mineacademy.punishcontrol.spigot.conversations;

import lombok.NonNull;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.conversation.SimpleConversation;
import org.mineacademy.fo.conversation.SimplePrompt;
import org.mineacademy.punishcontrol.core.localization.LocalizableEditor;
import org.mineacademy.punishcontrol.spigot.menus.LocalizableEditorMenu;

public final class LocalizableEditConversation extends SimpleConversation {

  private final LocalizableEditor localizableEditor;
  private final int index;

  private LocalizableEditConversation(
      @NonNull final LocalizableEditor localizableEditor,
      final int slot) {
    this.localizableEditor = localizableEditor;
    this.index = slot;
  }

  public static LocalizableEditConversation create(
      @NonNull final LocalizableEditor localizableEditor,
      final int slot) {
    return new LocalizableEditConversation(localizableEditor, slot);
  }

  @Override
  protected SimplePrompt getFirstPrompt() {
    return new LocalizableEditPrompt();
  }

  private class LocalizableEditPrompt extends SimplePrompt {

    @Override
    protected String getPrompt(ConversationContext ctx) {
      return "&7Please enter a value";
    }

    @Override
    protected @Nullable Prompt acceptValidatedInput(
        @NotNull ConversationContext conversationContext, @NotNull String input) {
      localizableEditor.set(index, input);
      localizableEditor.save();
      LocalizableEditorMenu.showTo(getPlayer(conversationContext), localizableEditor.localizable());
      return null;
    }
  }
}
