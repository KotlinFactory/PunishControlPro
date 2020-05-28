package org.mineacademy.punishcontrol.spigot.conversations.template;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.conversation.SimpleConversation;
import org.mineacademy.fo.conversation.SimplePrompt;
import org.mineacademy.punishcontrol.spigot.Scheduler;
import org.mineacademy.punishcontrol.spigot.menus.template.PunishTemplateCreatorMenu;

@RequiredArgsConstructor
public class TemplateReasonConversation extends SimpleConversation {

  private final PunishTemplateCreatorMenu menu;

  public static TemplateReasonConversation create(
      final PunishTemplateCreatorMenu menu) {
    return new TemplateReasonConversation(menu);
  }

  @Override
  protected Prompt getFirstPrompt() {
    return ReasonPrompt.create(menu);
  }

  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  private static class ReasonPrompt extends SimplePrompt {

    private final PunishTemplateCreatorMenu menu;

    public static ReasonPrompt create(final PunishTemplateCreatorMenu menu) {
      return new ReasonPrompt(menu);
    }

    @Override
    protected String getPrompt(final ConversationContext ctx) {
      return "&7Type in a reason.";
    }

    @Override
    protected @Nullable Prompt acceptValidatedInput(
        @NotNull final ConversationContext context,
        @NotNull final String input) {

      Scheduler.runAsync(() -> {
        menu.punishTemplate().reason(input);
        menu.displayTo(getPlayer(context));
      });
      return null;
    }
  }
}
