package org.mineacademy.punishcontrol.spigot.conversations;

import java.util.stream.Collectors;
import lombok.NonNull;
import org.bukkit.conversations.ConversationContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.conversation.SimpleConversation;
import org.mineacademy.fo.conversation.SimplePrompt;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.spigot.menu.browser.AbstractPlayerBrowser;
import org.mineacademy.punishcontrol.spigot.util.Schedulable;

/**
 * Conversation to search for players by their partial name
 */
public final class SearchPlayerConversation
    extends SimpleConversation
    implements Schedulable {

  /**
   * Can't be injected
   */
  private final PlayerProvider playerProvider = Providers.playerProvider();

  private final AbstractPlayerBrowser abstractPlayerBrowser;

  public static SearchPlayerConversation create(
      @NonNull final AbstractPlayerBrowser abstractPlayerBrowser) {
    return new SearchPlayerConversation(abstractPlayerBrowser);
  }

  private SearchPlayerConversation(
      @NonNull final AbstractPlayerBrowser abstractPlayerBrowser) {
    this.abstractPlayerBrowser = abstractPlayerBrowser;
  }

  @Override
  protected SimplePrompt getFirstPrompt() {
    return new SearchPlayerPrompt();
  }

  private class SearchPlayerPrompt extends SimplePrompt {

    @Override
    public @NotNull String getPrompt(ConversationContext conversationContext) {
      return "&7Please type in a partial name to search for.";
    }

    @Override
    protected @Nullable SimplePrompt acceptValidatedInput(
        @NotNull ConversationContext conversationContext, @NonNull String input) {

      async(() -> abstractPlayerBrowser.redisplay(
          playerProvider.search(input)
              .stream()
              .map(result -> playerProvider.findUUID(result.result()).orElse(null))
              .collect(Collectors.toList())));

      return null;
    }
  }
}
