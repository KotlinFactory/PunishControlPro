package org.mineacademy.punishcontrol.proxy.conversations;

import de.exceptionflug.mccommons.inventories.proxy.utils.Schedulable;
import java.util.stream.Collectors;
import lombok.NonNull;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.bfo.conversation.SimpleConversation;
import org.mineacademy.bfo.conversation.SimplePrompt;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.proxy.menu.browser.AbstractPlayerBrowser;

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
      @NonNull final ProxiedPlayer player,
      @NonNull final AbstractPlayerBrowser abstractPlayerBrowser) {
    return new SearchPlayerConversation(player, abstractPlayerBrowser);
  }

  private SearchPlayerConversation(
      @NonNull final ProxiedPlayer player,
      @NonNull final AbstractPlayerBrowser abstractPlayerBrowser) {
    super(player);
    this.abstractPlayerBrowser = abstractPlayerBrowser;
  }

  @Override
  protected SimplePrompt getFirstPrompt() {
    return new SearchPlayerPrompt(this);
  }

  private class SearchPlayerPrompt extends SimplePrompt {

    private SearchPlayerPrompt(SimpleConversation parent) {
      super(parent);
    }

    @Override
    public @NotNull String getPrompt() {
      return "&7Please type in a partial name to search for.";
    }

    @Override
    protected @Nullable SimplePrompt acceptValidatedInput(@NonNull String input) {

      async(() -> abstractPlayerBrowser.redisplay(
          playerProvider.search(input)
              .stream()
              .map(result -> playerProvider.findUUID(result.result()).orElse(null))
              .collect(Collectors.toList())));

      return null;
    }
  }
}
