package org.mineacademy.punishcontrol.core.fo.constants;

import lombok.experimental.UtilityClass;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.providers.PluginDataProvider;
import org.mineacademy.punishcontrol.core.util.TimeUtil;

/**
 * Stores constants for this plugin
 */
@UtilityClass
public class FoConstants {

  private static final PluginDataProvider DATA = Providers
      .pluginDataProvider();

  public String configLine() {
    return "-------------------------------------------------------------------------------------------";
  }

  @UtilityClass
  public class File {

    /**
     * The name of our settings file
     */
    public final String SETTINGS = "settings.yml";

    /**
     * The error.log file created automatically to log errors to
     */
    public final String ERRORS = "error.log";

    /**
     * The debug.log file to log debug messages to
     */
    public final String DEBUG = "debug.log";

    /**
     * The data.db file (uses YAML) for saving various data
     */
    public final String DATA = "data.db";

    /**
     * Files related to the ChatControl plugin
     */
    public final class ChatControl {

      /**
       * The command-spy.log file in logs/ folder
       */
      public final String COMMAND_SPY = "logs/command-spy.log";

      /**
       * The chat log file in logs/ folder
       */
      public final String CHAT_LOG = "logs/chat.log";

      /**
       * The admin log in log/s folder
       */
      public final String ADMIN_CHAT = "logs/admin-chat.log";

      /**
       * The bungee chat log file in logs/ folder
       */
      public final String BUNGEE_CHAT = "logs/bungee-chat.log";

      /**
       * The rules log file in logs/ folder
       */
      public final String RULES_LOG = "logs/rules.log";

      /**
       * The console log file in logs/ folder
       */
      public final String CONSOLE_LOG = "logs/console.log";

      /**
       * The file logging channels joins and leaves in logs/ folder
       */
      public final String CHANNEL_JOINS = "logs/channel-joins.log";
    }
  }

  @UtilityClass
  public class Header {

    /**
     * The header for data.db file
     */
    public final String[] DATA_FILE = new String[]{
        "",
        "This file stores various data you create via the plugin.",
        "",
        " ** THE FILE IS MACHINE GENERATED. PLEASE DO NOT EDIT **",
        ""
    };

    /**
     * The header that is put into the file that has been automatically updated.
     */
    public final String[] UPDATED_FILE = new String[]{
        configLine(),
        "",
        " Your file has been automatically updated at " + TimeUtil.getFormattedDate(),
        " to " + DATA.getNamed() + " " + DATA.getVersion(),
        "",
        " Due to the improvements LightningStorage brought most comments",
        " should have been preserved.",
        "",
        " If you'd like to view the default file, you can either:",
        " a) Open the " + DATA.getSource().getName() + " with a WinRar or similar",
        " b) or, visit: https://github.com/kangarko/" + DATA.getNamed() + "/wiki",
        "",
        configLine(),
        ""
    };
  }
}
