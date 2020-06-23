package org.mineacademy.punishcontrol.proxy.conversations;

import de.exceptionflug.protocolize.inventory.InventoryModule;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.bfo.conversation.SimpleConversation;
import org.mineacademy.bfo.conversation.SimplePrompt;
import org.mineacademy.punishcontrol.proxy.menus.punish.PunishCreatorMenu;

public final class PunishReasonConversation extends SimpleConversation {

  private final PunishCreatorMenu menu;

  public PunishReasonConversation(
      final ProxiedPlayer player,
      final PunishCreatorMenu menu) {
    super(player);
    InventoryModule.closeAllInventories(player);
    this.menu = menu;
  }

  public static PunishReasonConversation create(
      final ProxiedPlayer player,
      final PunishCreatorMenu menu) {
    return new PunishReasonConversation(player, menu);
  }

  @Override
  protected SimplePrompt getFirstPrompt() {
    return ReasonPrompt.create(this, menu);
  }

  private static final class ReasonPrompt extends SimplePrompt {

    private final PunishCreatorMenu menu;

    public ReasonPrompt(
        final org.mineacademy.bfo.conversation.SimpleConversation parent,
        final PunishCreatorMenu menu) {
      super(parent);
      this.menu = menu;
    }

    public static ReasonPrompt create(
        final PunishReasonConversation parent,
        final PunishCreatorMenu menu) {
      return new ReasonPrompt(parent, menu);
    }

    @Override
    public String getPrompt() {
      return "&7Please choose a punishment reason";
    }

    @Override
    public @Nullable SimplePrompt acceptValidatedInput(@NotNull final String s) {
      if (s.contains("--")) {
        tell("Reason must not contain --");
        return null;
      }
      menu.setReason(s);
      menu.reDisplay();
      return null;
    }

  }
}
