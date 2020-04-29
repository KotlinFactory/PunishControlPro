package org.mineacademy.punishcontrol.proxy.conversations;

import de.exceptionflug.mccommons.inventories.proxy.utils.Schedulable;
import java.util.UUID;
import lombok.val;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.bfo.Common;
import org.mineacademy.bfo.Players;
import org.mineacademy.bfo.conversation.SimpleConversation;
import org.mineacademy.bfo.conversation.SimplePrompt;
import org.mineacademy.burst.menu.AbstractMenu;
import org.mineacademy.burst.util.Scheduler;

public final class KickConversation extends SimpleConversation implements Schedulable {

  private final AbstractMenu menu;
  private final UUID target;
  private final String targetName;

  public KickConversation(
      final ProxiedPlayer player,
      final AbstractMenu menu,
      final UUID target,
      final String targetName) {
    super(player);
    this.menu = menu;
    this.target = target;
    this.targetName = targetName;
  }

  public static KickConversation create(
      final ProxiedPlayer player,
      final AbstractMenu menu,
      final UUID target,
      final String targetName) {
    return new KickConversation(player, menu, target, targetName);
  }

  @Override
  protected SimplePrompt getFirstPrompt() {
    return KickPrompt.create(this, menu, target, targetName);
  }

  private final static class KickPrompt extends SimplePrompt {

    private final AbstractMenu menu;
    private final UUID target;
    private final String targetName;

    public KickPrompt(
        final SimpleConversation parent,
        final AbstractMenu menu,
        final UUID target,
        final String targetName) {
      super(parent);
      this.menu = menu;
      this.target = target;
      this.targetName = targetName;
    }

    public static KickPrompt create(
        final SimpleConversation parent,
        final AbstractMenu menu,
        final UUID target,
        final String targetName) {
      return new KickPrompt(parent, menu, target, targetName);
    }

    @Override
    public String getPrompt() {
      return "&7Type in the reason to kick &6" + targetName;
    }

    @Override
    protected @Nullable SimplePrompt acceptValidatedInput(
        @NotNull final String input) {

      final ProxiedPlayer viewer = getPlayer();
      final val optionalPlayer = Players.find(target);

      //TODO reformat
      optionalPlayer.ifPresent((player -> player.disconnect(input)));

      Common.tell(
          viewer,
          "&7Successfully kicked {target}".replace("{target}", targetName));

      Scheduler.runLater(20, menu::reDisplay);
      return null;
    }
  }
}
