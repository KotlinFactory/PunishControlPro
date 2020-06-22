package org.mineacademy.punishcontrol.spigot.conversations;

import lombok.NonNull;
import org.bukkit.conversations.ConversationContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.conversation.SimpleConversation;
import org.mineacademy.fo.conversation.SimplePrompt;
import org.mineacademy.punishcontrol.spigot.menu.browser.AbstractSearchableBrowser;
import org.mineacademy.punishcontrol.spigot.util.Schedulable;

public final class SearchConversation
    extends SimpleConversation
    implements Schedulable {

  public static SearchConversation create(
      @NonNull AbstractSearchableBrowser searchableBrowser) {
    return new SearchConversation(searchableBrowser);
  }

  private final AbstractSearchableBrowser searchableBrowser;

  private SearchConversation(@NonNull AbstractSearchableBrowser searchableBrowser) {
    this.searchableBrowser = searchableBrowser;
  }

  @Override
  protected SimplePrompt getFirstPrompt() {
    return new SearchPrompt();
  }

  public class SearchPrompt extends SimplePrompt {

    private SearchPrompt() {
    }

    @Override
    public @NotNull String getPrompt(ConversationContext ctx) {
      return "&7Please type in a value to search for";
    }

    @Override
    protected @Nullable SimplePrompt acceptValidatedInput(
        ConversationContext ctx,
        String input) {
      async(() -> {
        searchableBrowser.redisplay(searchableBrowser.searchByPartialString(input));
      });
      return null;
    }
  }
}
