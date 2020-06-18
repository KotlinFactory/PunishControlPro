package org.mineacademy.punishcontrol.spigot.conversations;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.conversation.SimpleConversation;
import org.mineacademy.fo.conversation.SimplePrompt;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.punishcontrol.spigot.Players;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class KickConversation extends SimpleConversation {

  private final Menu menu;
  private final UUID target;
  private final String targetName;

  public static KickConversation create(
      final Menu menu,
      final UUID target,
      final String targetName) {
    return new KickConversation(menu, target, targetName);
  }

  @Override
  protected Prompt getFirstPrompt() {
    return KickPrompt.create(menu, target, targetName);
  }

  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  private final static class KickPrompt extends SimplePrompt {

    private final Menu menu;
    private final UUID target;
    private final String targetName;

    public static KickPrompt create(
        final Menu menu,
        final UUID target,
        final String targetName) {
      return new KickPrompt(menu, target, targetName);
    }

    @Override
    protected String getPrompt(final ConversationContext ctx) {
      return "&7Type in the reason to kick &6" + targetName;
    }

    @Override
    protected @Nullable Prompt acceptValidatedInput(
        @NotNull final ConversationContext context,
        @NotNull final String input) {

      final Player viewer = getPlayer(context);
      final val optionalPlayer = Players.find(target);

      optionalPlayer.ifPresent((player -> player.kickPlayer(input)));

      Common.runLater(() -> {
        if (!viewer.isOnline()) {
          return;
        }

        Common.tell(
            viewer,
            "&7Successfully kicked {target}".replace("{target}", targetName));

        Common.runLaterAsync(20, () -> menu.displayTo(viewer, true));
      });
      return null;
    }
  }
}
