package org.mineacademy.punishcontrol.spigot.conversation;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.conversations.Prompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.conversation.SimpleConversation;
import org.mineacademy.fo.conversation.SimplePrompt;
import org.mineacademy.punishcontrol.core.settings.Settings;
import org.mineacademy.punishcontrol.spigot.menus.template.PunishTemplateCreatorMenu;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class AddTemplateConversation extends SimpleConversation {

  public static AddTemplateConversation create() {
    return new AddTemplateConversation();
  }

  @Override
  protected ConversationPrefix getPrefix() {
    return conversationContext -> Settings.PLUGIN_PREFIX;
  }

  @Override
  protected Prompt getFirstPrompt() {
    return NamePrompt.create();
  }

  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  private static class NamePrompt extends SimplePrompt {

    public static NamePrompt create() {
      return new NamePrompt();
    }

    @Override
    protected String getPrompt(final ConversationContext ctx) {
      return "&8Please select an name for the punish-template";
    }

    @Override
    protected @Nullable Prompt acceptValidatedInput(
        @NotNull final ConversationContext conversationContext,
        @NotNull final String s) {

      PunishTemplateCreatorMenu
          .showTo(getPlayer(conversationContext), s);
      return null;
    }
  }
}
