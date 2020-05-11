package org.mineacademy.punishcontrol.proxy.conversations.template;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.bfo.conversation.SimpleConversation;
import org.mineacademy.bfo.conversation.SimplePrompt;
import org.mineacademy.burst.util.Scheduler;
import org.mineacademy.punishcontrol.proxy.menus.template.PunishTemplateCreatorMenu;

public final class TemplateReasonConversation extends SimpleConversation {

  private final PunishTemplateCreatorMenu menu;

  private TemplateReasonConversation(
      final ProxiedPlayer player,
      final PunishTemplateCreatorMenu menu) {
    super(player);
    this.menu = menu;
  }

  public static TemplateReasonConversation create(
      final ProxiedPlayer player,
      final PunishTemplateCreatorMenu menu) {
    return new TemplateReasonConversation(player, menu);
  }

  @Override
  protected SimplePrompt getFirstPrompt() {
    return ReasonPrompt.create(this, menu);
  }

  private static class ReasonPrompt extends SimplePrompt {

    private final PunishTemplateCreatorMenu menu;

    public ReasonPrompt(final SimpleConversation parent, final PunishTemplateCreatorMenu menu) {
      super(parent);
      this.menu = menu;
    }

    public static ReasonPrompt create(final SimpleConversation parent,
        final PunishTemplateCreatorMenu menu) {
      return new ReasonPrompt(parent, menu);
    }

    @Override
    public String getPrompt() {
      return "&7Type a reason.";
    }

    @Override
    protected @Nullable SimplePrompt acceptValidatedInput(@NotNull final String input) {

      Scheduler.runAsync(() -> {
        menu.punishTemplate().reason(input);
        menu.reDisplay();
      });
      return null;
    }
  }
}
