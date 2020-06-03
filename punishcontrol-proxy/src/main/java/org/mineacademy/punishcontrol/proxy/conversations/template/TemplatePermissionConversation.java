package org.mineacademy.punishcontrol.proxy.conversations.template;

import de.exceptionflug.protocolize.inventory.InventoryModule;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.bfo.conversation.SimpleConversation;
import org.mineacademy.bfo.conversation.SimplePrompt;
import org.mineacademy.burst.util.Scheduler;
import org.mineacademy.punishcontrol.proxy.menus.template.PunishTemplateCreatorMenu;

public final class TemplatePermissionConversation extends SimpleConversation {

  private TemplatePermissionConversation(
      final ProxiedPlayer player,
      final PunishTemplateCreatorMenu menu) {
    super(player);
    InventoryModule.closeAllInventories(player);
    this.menu = menu;
  }

  private final PunishTemplateCreatorMenu menu;

  public static TemplatePermissionConversation create(
      final ProxiedPlayer player,
      final PunishTemplateCreatorMenu menu) {
    return new TemplatePermissionConversation(player, menu);
  }

  @Override
  protected SimplePrompt getFirstPrompt() {
    return PermissionPrompt.create(this, menu);
  }

  private static class PermissionPrompt extends SimplePrompt {

    private final PunishTemplateCreatorMenu menu;

    public PermissionPrompt(
        final SimpleConversation parent,
        final PunishTemplateCreatorMenu menu) {
      super(parent);
      this.menu = menu;
    }

    public static PermissionPrompt create(
        final SimpleConversation parent,
        final PunishTemplateCreatorMenu menu) {
      return new PermissionPrompt(parent, menu);
    }

    @Override
    public String getPrompt() {
      return "&7Type a permission.";
    }

    @Override
    public @Nullable SimplePrompt acceptValidatedInput(
        @NotNull final String input) {
      Scheduler.runAsync(() -> {
        menu.punishTemplate().permission(input);
        menu.reDisplay();
      });
      return null;
    }
  }
}
