package org.mineacademy.punishcontrol.spigot.conversation;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.conversation.SimpleConversation;
import org.mineacademy.fo.conversation.SimplePrompt;
import org.mineacademy.punishcontrol.spigot.menus.punish.PunishCreatorMenu;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class AddReasonConversation extends SimpleConversation {

  private final PunishCreatorMenu menu;

  public static AddReasonConversation create(final PunishCreatorMenu menu) {
    return new AddReasonConversation(menu);
  }

  @Override
  protected Prompt getFirstPrompt() {
    return ReasonPrompt.create(menu);
  }

  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  private static final class ReasonPrompt extends SimplePrompt {
    private final PunishCreatorMenu menu;

    public static ReasonPrompt create(final PunishCreatorMenu menu) {
      return new ReasonPrompt(menu);
    }

    @Override
    protected String getPrompt(final ConversationContext ctx) {
      return "&7Please choose a punish-reason";
    }

    @Override
    protected @Nullable Prompt acceptValidatedInput(
        @NotNull final ConversationContext conversationContext,
        @NotNull final String s) {
      menu.setReason(s);
      menu.displayTo(getPlayer(conversationContext));
      return null;
    }
  }
}
