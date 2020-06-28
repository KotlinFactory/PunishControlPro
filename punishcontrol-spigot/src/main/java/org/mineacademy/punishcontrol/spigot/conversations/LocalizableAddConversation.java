package org.mineacademy.punishcontrol.spigot.conversations;

import lombok.NonNull;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.conversation.SimpleConversation;
import org.mineacademy.fo.conversation.SimplePrompt;
import org.mineacademy.punishcontrol.core.localization.LocalizableEditor;

public final class LocalizableAddConversation extends SimpleConversation {

  private final LocalizableEditor localizableEditor;

  private LocalizableAddConversation(
      @NonNull final LocalizableEditor localizableEditor) {
    super();
    this.localizableEditor = localizableEditor;
  }

  public static LocalizableAddConversation create(
      @NonNull final LocalizableEditor localizableEditor) {
    return new LocalizableAddConversation(localizableEditor);
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
