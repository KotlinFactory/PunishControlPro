package org.mineacademy.punishcontrol.core.settings;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.mineacademy.punishcontrol.core.MessageType;
import org.mineacademy.punishcontrol.core.permission.Permission;
import org.mineacademy.punishcontrol.core.permission.PermissionType;
import org.mineacademy.punishcontrol.core.permission.Permissions;
import org.mineacademy.punishcontrol.core.setting.SimpleSettings;
import org.mineacademy.punishcontrol.core.storage.StorageType;

public final class Settings extends SimpleSettings {

  public static List<String> COMMAND_ALIASES = new ArrayList<>();
  public static StorageType STORAGE_TYPE;

  private static void init() {
    STORAGE_TYPE = Enum.valueOf(StorageType.class, getString("Storage").toUpperCase());
    COMMAND_ALIASES = getStringList("Command_Aliases");
  }

  @Override
  protected int getConfigVersion() {
    return 2;
  }

  public static final class MySQL {

    public static String DATABASE;
    public static String HOST;
    public static Integer PORT;
    public static String USER;
    public static String PASSWORD;
    public static Boolean REMIGRATE;

    private static void init() {
      pathPrefix("MySQL");
      HOST = getOrDefault("Host", "Not set");
      PORT = getOrDefault("Port", 3306);
      DATABASE = getOrDefault("Database", "Not set");
      USER = getOrDefault("User", "Not set");
      REMIGRATE = getOrDefault("Remigrate", true);
      PASSWORD = getOrDefault("Password", "*");
    }
  }

  public static final class Punish {

    public static final class Ban {

      public static Boolean APPLY_ON_IP;
      public static Boolean ENABLED;

      private static void init() {
        pathPrefix("Punishes.Ban");
        APPLY_ON_IP = getBoolean("Apply_On_Ip");
        ENABLED = getBoolean("Enabled");
      }
    }

    public static final class Mute {

      public static List<String> DISABLED_COMMANDS;
      public static Boolean APPLY_ON_IP;
      public static Boolean ENABLED;

      private static void init() {
        pathPrefix("Punishes.Mute");
        ENABLED = getBoolean("Enabled");
        DISABLED_COMMANDS = getStringList("Disabled_Commands");
        APPLY_ON_IP = getBoolean("Apply_On_Ip");
      }
    }

    public static final class Warn {

      public static MessageType messageType;

      private static void init() {
        pathPrefix("Punishes.Warn");
        messageType = MessageType.valueOf(
            getOrSetDefault("Message_Type", "TITLE")
                .toUpperCase());

      }
    }
  }

  public static final class Notifications {

    public static final class Punish {

      public static Boolean ENABLED;
      public static String PERMISSION;

      private static void init() {
        pathPrefix("Notifications.Punish");
        ENABLED = getBoolean("Enabled");
        PERMISSION = getString("Permission");
        Permissions.register(
            Permission
                .of(
                    PERMISSION,
                    "See notifications on punishments",
                    "Even if the punishment is silent")
                .type(PermissionType.OTHER)
        );
      }
    }

    public static final class SilentPunish {

      public static Boolean ENABLED;
      public static String PERMISSION;

      private static void init() {
        pathPrefix("Notifications.Silent-Punish");
        ENABLED = getBoolean("Enabled");
        PERMISSION = getString("Permission");
        Permissions.register(
            Permission
                .of(
                    PERMISSION,
                    "See notifications on punishments",
                    "Even if the punishment is silent")
                .type(PermissionType.OTHER)
        );
      }
    }
  }

  public static final class Advanced {

    public static Boolean ENABLE_BACKUPS;
    public static Boolean ONLINE_MODE;
    public static Boolean CACHE_RESULTS;
    public static String DATE_FORMAT = "MM/dd/yyyy/hh";
    public static Double MIN_SIMILARITY;
    public static Integer MIN_PROTOCOL_VERSION_SUPPORTED;

    public static Boolean ENCOURAGE_BUNGEE_USAGE;

    public static Boolean STARTS_WITH;

    private static void init() {
      pathPrefix("Advanced");
      DATE_FORMAT = getString("Date_Format");
      ONLINE_MODE = getBoolean("Online_Mode");
      CACHE_RESULTS = getBoolean("Cache_Results");
      MIN_SIMILARITY = getDouble("Searcher.Min_Similarity_Needed");
      STARTS_WITH = getBoolean("Searcher.Use_Starts_With");
      ENABLE_BACKUPS = getBoolean("Enable_Backups");
      ENCOURAGE_BUNGEE_USAGE = getBoolean("Encourage_Bungee_Usage");
      MIN_PROTOCOL_VERSION_SUPPORTED = getInteger("Min_Protocol_Version_Supported");
    }

    public static String formatDate(final long ms) {
      if (ms == -1) {
        return "never";
      }
      final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
          Advanced.DATE_FORMAT);
      return simpleDateFormat.format(new Date(ms));
    }

    public static final class API {

      private static void init() {

        pathPrefix("Advanced.API");
      }
    }
  }
}
