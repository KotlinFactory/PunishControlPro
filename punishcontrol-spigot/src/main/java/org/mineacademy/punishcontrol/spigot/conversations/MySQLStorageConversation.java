package org.mineacademy.punishcontrol.spigot.conversations;

import de.leonhard.storage.util.ClassWrapper;
import org.bukkit.conversations.ConversationContext;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.conversation.SimpleConversation;
import org.mineacademy.fo.conversation.SimplePrompt;
import org.mineacademy.punishcontrol.core.conversation.StorageSettable;
import org.mineacademy.punishcontrol.core.settings.Settings.MySQL;
import org.mineacademy.punishcontrol.spigot.menus.settings.StorageSettingsMenu;

public final class MySQLStorageConversation
    extends SimpleConversation
    implements StorageSettable {

  private final String type;

  public static MySQLStorageConversation create(final String type) {
    return new MySQLStorageConversation(type);
  }

  private MySQLStorageConversation(final String type) {
    this.type = type;
  }

  @Override
  protected SimplePrompt getFirstPrompt() {
    return new MySQLPrompt();
  }

  private final class MySQLPrompt extends SimplePrompt {

    private MySQLPrompt() {
      super();
    }

    @Override
    protected String getPrompt(final ConversationContext ctx) {
      return "&8Enter a value for '" + type + "'";
    }

    @Override
    protected @Nullable SimplePrompt acceptValidatedInput(
        final ConversationContext conversationContext,
        final String input) {

      if ("Host".equalsIgnoreCase(type)) {
        setToConfig("MySQL.Host", input);
        MySQL.HOST = input;
      } else if ("Port".equalsIgnoreCase(type)) {
        setToConfig("MySQL.Port", input);
        MySQL.PORT = ClassWrapper.INTEGER.getInt(input);
      } else if ("Database".equalsIgnoreCase(type)) {
        setToConfig("MySQL.Database", input);
        MySQL.DATABASE = input;
      } else if ("User".equalsIgnoreCase(type)) {
        setToConfig("MySQL.User", input);
        MySQL.USER = input;
      } else if ("Password".equalsIgnoreCase(type)) {
        setToConfig("MySQL.Password", input);
        MySQL.PASSWORD = input;
      }

      StorageSettingsMenu.showTo(getPlayer(conversationContext));
      return null;
    }
  }
}
