package org.mineacademy.punishcontrol.core.setting;

import de.leonhard.storage.LightningBuilder;
import de.leonhard.storage.Yaml;
import de.leonhard.storage.internal.settings.ConfigSettings;
import de.leonhard.storage.internal.settings.DataType;
import de.leonhard.storage.util.Valid;

/**
 * A simple implementation of a basic localization file. We create the
 * localization/messages_LOCALEPREFIX.yml file automatically and fill it with values from
 * your localization/messages_LOCALEPREFIX.yml file placed within in your plugins jar
 * file.
 */
@SuppressWarnings("unused")
public abstract class SimpleLocalization extends YamlStaticConfig {

  /**
   * The message for player if they lack a permission.
   */
  public static String NO_PERMISSION = "&cInsufficient permission ({permission}).";
  /**
   * The server prefix. Example: you have to use it manually if you are sending messages
   * from the console to players
   */
  public static String SERVER_PREFIX = "[Server]";

  // --------------------------------------------------------------------
  // Version
  // --------------------------------------------------------------------
  /**
   * The console localized name. Example: Console
   */
  public static String CONSOLE_NAME = "Console";
  /**
   * The message when a section is missing from data.db file (typically we use this file
   * to store serialized values such as arenas from minigame plugins).
   */
  public static String DATA_MISSING = "&c{name} lacks database information! Please only create {type} in-game! Skipping..";
  /**
   * The message when the console attempts to start a server conversation which is
   * prevented.
   */
  public static String CONVERSATION_REQUIRES_PLAYER = "Only players may enter this conversation.";

  // --------------------------------------------------------------------
  // Shared values
  // --------------------------------------------------------------------

  // NB: Those keys are optional - you do not have to write them into your
  // messages_X.yml files
  // but if you do, we will use your values instead of the default ones!
  /**
   * The configuration version number, found in the "Version" key in the file.,
   */
  protected static Integer VERSION;
  private static boolean localizationClassCalled;

  /**
   * Load the values -- this method is called automatically by reflection in the {@link
   * YamlStaticConfig} class!
   */
  private static void init() {
    pathPrefix(null);
    Valid.checkBoolean(
        !localizationClassCalled,
        "Localization class already loaded!");

    if (isSet("No_Permission")) {
      NO_PERMISSION = getString("No_Permission");
    }

    if (isSet("Server_Prefix")) {
      SERVER_PREFIX = getString("Server_Prefix");
    }

    if (isSet("Console_Name")) {
      CONSOLE_NAME = getString("Console_Name");
    }

    if (isSet("Data_Missing")) {
      DATA_MISSING = getString("Data_Missing");
    }

    if (isSet("Conversation_Requires_Player")) {
      CONVERSATION_REQUIRES_PLAYER = getString(
          "Conversation_Requires_Player");
    }

    localizationClassCalled = true;
  }

  /**
   * Was this class loaded?
   *
   * @return
   */
  public static Boolean isLocalizationCalled() {
    return localizationClassCalled;
  }

  /**
   * Reset the flag indicating that the class has been loaded, used in reloading.
   */
  public static void resetLocalizationCall() {
    localizationClassCalled = false;
  }

  @Override
  protected final Yaml getConfigInstance() {
    final String name = "messages_" + SimpleSettings.LOCALE_PREFIX;
    return LightningBuilder
        .fromPath(name, DATA.getDataFolder().getAbsolutePath() + "/localization/")
        .addInputStreamFromResource("localization/" + name + ".yml")
        .setConfigSettings(ConfigSettings.PRESERVE_COMMENTS)
        .setDataType(DataType.SORTED)
        .createConfig()
        .addDefaultsFromInputStream();
  }

  /**
   * Set and update the config version automatically, however the {@link #VERSION} will
   * contain the older version used in the file on the disk so you can use it for
   * comparing in the init() methods
   *
   * <p>
   * Please call this as a super method when overloading this!
   */
  @Override
  protected void beforeLoad() {
    // Load version first so we can use it later
    pathPrefix(null);

    if ((VERSION = getInteger("Version")) != getConfigVersion()) {
      //update
      set("Version", getConfigVersion());
    }
  }

  /**
   * Return the very latest config version
   *
   * <p>
   * Any changes here must also be made to the "Version" key in your settings file.
   *
   * @return
   */
  protected abstract int getConfigVersion();

  /**
   * Locale keys related to your plugin commands
   */
  public static final class Commands {

    /**
     * The message at "No_Console" key shown when console is denied executing a command.
     */
    public static String NO_CONSOLE = "&cYou may only use this command as a player";

    /**
     * The message shown when there is a fatal error running this command
     */
    public static String COOLDOWN_WAIT = "&cWait {duration} second(s) before using this command again.";

    /**
     * The message shown when the player tries a command but inputs an invalid first
     * argument parameter. We suggest he types /{label} ? for help so make sure you
     * implement some help there as well.
     */
    public static String INVALID_ARGUMENT = "&cInvalid argument. Run &6/{label} ? &cfor help.";

    /**
     * The message shown when the player tries a command but inputs an invalid second
     * argument parameter. We so suggest he types /{label} {0} for help
     */
    public static String INVALID_SUB_ARGUMENT = "&cInvalid argument. Run '/{label} {0}' for help.";

    public static String INVALID_ARGUMENT_MULTILINE = "&cInvalid argument. Usage:";

    /**
     * The description label
     */
    public static String LABEL_DESCRIPTION = "&cDescription: {description}";

    public static String LABEL_USAGES = "&cUsages:";

    /**
     * The usage label
     */
    public static String LABEL_USAGE = "&cUsage:";

    /**
     * The message at "Reload_Success" key shown when the plugin has been reloaded
     * successfully.
     */
    public static String RELOAD_SUCCESS = "&6{plugin_name} {plugin_version} has been reloaded.";

    /**
     * The message at "Reload_Fail" key shown when the plugin has failed to reload.
     */
    public static String RELOAD_FAIL = "&4Oups, &creloading failed! See the console for more information. Error: {error}";

    /**
     * The message shown when there is a fatal error running this command
     */
    public static String ERROR = "&4&lOups! &cThe command failed :( Check the console and report the error.";

    /**
     * Load the values -- this method is called automatically by reflection in the {@link
     * YamlStaticConfig} class!
     */
    private static void init() {
      pathPrefix("Commands");

      if (isSet("No_Console")) {
        NO_CONSOLE = getString("No_Console");
      }

      if (isSet("Cooldown_Wait")) {
        COOLDOWN_WAIT = getString("Cooldown_Wait");
      }

      if (isSet("Invalid_Argument")) {
        INVALID_ARGUMENT = getString("Invalid_Argument");
      }

      if (isSet("Invalid_Sub_Argument")) {
        INVALID_SUB_ARGUMENT = getString("Invalid_Sub_Argument");
      }

      if (isSet("Invalid_Argument_Multiline")) {
        INVALID_ARGUMENT_MULTILINE = getString(
            "Invalid_Argument_Multiline");
      }

      if (isSet("Label_Description")) {
        LABEL_DESCRIPTION = getString("Label_Description");
      }

      if (isSet("Label_Usage")) {
        LABEL_USAGE = getString("Label_Usage");
      }

      if (isSet("Label_Usages")) {
        LABEL_USAGES = getString("Label_Usages");
      }

      if (isSet("Reload_Success")) {
        RELOAD_SUCCESS = getString("Reload_Success");
      }

      if (isSet("Reload_Fail")) {
        RELOAD_FAIL = getString("Reload_Fail");
      }

      if (isSet("Error")) {
        ERROR = getString("Error");
      }
    }
  }

  /**
   * Key related to players
   */
  public static class Player {

    /**
     * Message shown when the player is not online on this server
     */
    public static String NOT_ONLINE = "&cPlayer {player} &cis not online on this server.";

    /**
     * Load the values -- this method is called automatically by reflection in the {@link
     * YamlStaticConfig} class!
     */
    private static void init() {
      pathPrefix("Player");

      if (isSet("Not_Online")) {
        NOT_ONLINE = getString("Not_Online");
      }
    }
  }

  /**
   * Key related to the GUI system
   */
  public static class Menu {

    /**
     * Message shown when the player is not online on this server
     */
    public static String ITEM_DELETED = "&2The {item} has been deleted.";

    /**
     * Load the values -- this method is called automatically by reflection in the {@link
     * YamlStaticConfig} class!
     */
    private static void init() {
      pathPrefix("Menu");

      if (isSet("Item_Deleted")) {
        ITEM_DELETED = getString("Item_Deleted");
      }
    }
  }

  /**
   * Keys related to updating the plugin
   */
  public static class Update {

    /**
     * The message if a new version is found but not downloaded
     */
    public static String AVAILABLE =
        "&2A new version of &3{plugin_name}&2 is available.\n"
        + "&2Current version: &f{current}&2; New version: &f{new}\n"
        + "&2URL: &7https://www.spigotmc.org/resources/{resource_id}/.";

    /**
     * The message if a new version is found and downloaded
     */
    public static String DOWNLOADED =
        "&3{plugin_name}&2 has been upgraded from {current} to {new}.\n"
        + "&2Visit &7https://www.spigotmc.org/resources/{resource_id} &2for more information.\n"
        + "&2Please restart the server to load the new version.";

    /**
     * Load the values -- this method is called automatically by reflection in the {@link
     * YamlStaticConfig} class!
     */
    private static void init() {
      // Upgrade from old path
      pathPrefix("Update");

      if (isSet("Available")) {
        AVAILABLE = getString("Available");
      }

      if (isSet("Downloaded")) {
        DOWNLOADED = getString("Downloaded");
      }
    }
  }
}
