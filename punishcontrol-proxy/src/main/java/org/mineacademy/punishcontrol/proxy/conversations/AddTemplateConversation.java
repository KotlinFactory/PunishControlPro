package org.mineacademy.punishcontrol.proxy.conversations;

import de.exceptionflug.protocolize.inventory.InventoryModule;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.bfo.conversation.SimpleConversation;
import org.mineacademy.bfo.conversation.SimplePrompt;
import org.mineacademy.punishcontrol.proxy.menus.template.PunishTemplateCreatorMenu;

public final class AddTemplateConversation extends SimpleConversation {

  public AddTemplateConversation(final ProxiedPlayer player) {
    super(player);
    InventoryModule.closeAllInventories(player);
  }

  public static AddTemplateConversation create(final ProxiedPlayer player) {
    return new AddTemplateConversation(player);
  }

  @Override
  protected SimplePrompt getFirstPrompt() {
    return NamePrompt.create(this);
  }

  private static class NamePrompt extends SimplePrompt {

    private NamePrompt(final SimpleConversation parent) {
      super(parent);
    }

    public static NamePrompt create(final SimpleConversation conv) {
      return new NamePrompt(conv);
    }

    @Override
    public String getPrompt() {
      return "&8Please select a name for the punish template";
    }

    @Override
    protected @Nullable SimplePrompt acceptValidatedInput(@NotNull final String s) {

      PunishTemplateCreatorMenu
          .showTo(getParent().getPlayer(), s);
      return null;
    }
  }
}
