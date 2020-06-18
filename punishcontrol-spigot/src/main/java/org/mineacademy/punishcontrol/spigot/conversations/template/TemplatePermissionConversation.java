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

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class TemplatePermissionConversation extends SimpleConversation {

  private final PunishTemplateCreatorMenu menu;

  public static TemplatePermissionConversation create(
      final PunishTemplateCreatorMenu menu) {
    return new TemplatePermissionConversation(menu);
  }

  @Override
  protected Prompt getFirstPrompt() {
    return PermissionPrompt.create(menu);
  }

  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  private static class PermissionPrompt extends SimplePrompt {

    private final PunishTemplateCreatorMenu menu;

    public static PermissionPrompt create(
        final PunishTemplateCreatorMenu menu) {
      return new PermissionPrompt(menu);
    }

    @Override
    protected String getPrompt(final ConversationContext ctx) {
      return "&7Type in a permission.";
    }

    @Override
    protected @Nullable Prompt acceptValidatedInput(
        @NotNull final ConversationContext context,
        @NotNull final String input) {
      Scheduler.runAsync(() -> {
        menu.punishTemplate().permission(input);
        menu.displayTo(getPlayer(context));
      });
      return null;
    }
  }
}
