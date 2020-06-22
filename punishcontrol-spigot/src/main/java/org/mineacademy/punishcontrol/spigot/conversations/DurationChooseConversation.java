package org.mineacademy.punishcontrol.spigot.conversations;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.conversation.SimpleConversation;
import org.mineacademy.fo.conversation.SimplePrompt;
import org.mineacademy.punishcontrol.core.punish.PunishDuration;
import org.mineacademy.punishcontrol.spigot.menu.AbstractDurationChooser;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class DurationChooseConversation extends SimpleConversation {

  private final AbstractDurationChooser menu;

  public static DurationChooseConversation create(final AbstractDurationChooser menu) {
    return new DurationChooseConversation(menu);
  }

  @Override
  protected Prompt getFirstPrompt() {
    return new NamePrompt();
  }

  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  private class NamePrompt extends SimplePrompt {

    @Override
    protected String getPrompt(final ConversationContext ctx) {
      return "&8Please type in a duration";
    }

    @Override
    protected boolean isInputValid(
        final ConversationContext context,
        final String input) {
      return PunishDuration.of(input).toMs() != Long.MIN_VALUE;
    }

    @Override
    public void onConversationEnd(
        final SimpleConversation conversation,
        final ConversationAbandonedEvent event) {
      menu.displayTo(getPlayer(event.getContext()), true);
    }

    @Override
    protected @Nullable Prompt acceptValidatedInput(
        @NotNull final ConversationContext conversationContext,
        @NotNull final String s) {
      menu.ms(PunishDuration.of(s).toMs());
      return null;
    }
  }
}
