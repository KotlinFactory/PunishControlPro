package org.mineacademy.punishcontrol.proxy.conversations;

import de.leonhard.storage.util.ClassWrapper;
import lombok.NonNull;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.bfo.conversation.SimpleConversation;
import org.mineacademy.bfo.conversation.SimplePrompt;
import org.mineacademy.punishcontrol.core.conversation.SimpleMySQLStorageConversation;
import org.mineacademy.punishcontrol.core.settings.Settings;
import org.mineacademy.punishcontrol.core.settings.Settings.MySQL;
import org.mineacademy.punishcontrol.proxy.menus.settings.StorageSettingsMenu;

public final class MySQLStorageConversation
    extends SimpleConversation
    implements SimpleMySQLStorageConversation {

  private final String type;

  public static MySQLStorageConversation create(
      final ProxiedPlayer player,
      final String type) {
    return new MySQLStorageConversation(player, type);
  }

  private MySQLStorageConversation(
      @NonNull final ProxiedPlayer player,
      final String type) {
    super(player);
    this.type = type;
  }

  @Override
  protected SimplePrompt getFirstPrompt() {
    return new MySQLPrompt();
  }

  private final class MySQLPrompt extends SimplePrompt {

    private MySQLPrompt() {
      super(MySQLStorageConversation.this);
    }

    @Override
    public @NotNull String getPrompt() {
      return "&8Enter a value for '" + type + "'";
    }

    @Override
    protected @Nullable SimplePrompt acceptValidatedInput(final String input) {
      Settings.pathPrefix(null);

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

      StorageSettingsMenu.showTo(getPlayer());
      return null;
    }
  }
}
