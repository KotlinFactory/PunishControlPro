package org.mineacademy.punishcontrol.proxy.conversations;

import lombok.NonNull;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.bfo.conversation.SimpleConversation;
import org.mineacademy.bfo.conversation.SimplePrompt;
import org.mineacademy.punishcontrol.core.punish.PunishDuration;
import org.mineacademy.punishcontrol.proxy.menu.AbstractDurationChooser;

public class DurationChooseConversation extends SimpleConversation {

  private final AbstractDurationChooser menu;

  public static DurationChooseConversation create(
      @NonNull final ProxiedPlayer player,
      @NonNull final AbstractDurationChooser menu) {
    return new DurationChooseConversation(player, menu);
  }

  private DurationChooseConversation(
      @NonNull final ProxiedPlayer player,
      @NonNull final AbstractDurationChooser menu) {
    super(player);
    this.menu = menu;
  }

  @Override
  protected SimplePrompt getFirstPrompt() {
    return new NamePrompt(this);
  }

  private class NamePrompt extends SimplePrompt {

    private NamePrompt(final SimpleConversation parent) {
      super(parent);
    }

    @Override
    public String getPrompt() {
      return "&8Please type in a duration";
    }

    @Override
    protected @Nullable SimplePrompt acceptValidatedInput(@NotNull final String s) {

      menu.ms(PunishDuration.of(s).toMs());
      // TODO: TEST!
      menu.reopen();
      return null;
    }
  }

}
