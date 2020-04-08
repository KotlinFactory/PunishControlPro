package org.mineacademy.punishcontrol.spigot.settings;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.mineacademy.fo.settings.SimpleSettings;
import org.mineacademy.punishcontrol.core.storage.StorageType;

public final class Settings extends SimpleSettings {


  public static List<String> COMMAND_ALIASES = new ArrayList<>();
  public static StorageType STORAGE_TYPE;

  private static void init() {
    STORAGE_TYPE = Enum.valueOf(StorageType.class, getString("Storage"));
    COMMAND_ALIASES = getStringList("Command_Aliases");
  }

  @Override
  protected int getConfigVersion() {
    return 1;
  }

  public static final class Punish {
    public static final class Ban{

      private static void init() {
        pathPrefix("Punishes.Ban");

      }
    }

    public static final class Mute{
      public static List<String> BLOCKED_COMMANDS;

      private static void init() {
        pathPrefix("Punishes.Mute");
        BLOCKED_COMMANDS = getOrSetDefault("Allowed_Commands", new ArrayList<>());
      }
    }

    public static final class Warn{

      private static void init() {
        pathPrefix("Punishes.Warn");

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
      }
    }

    public static final class SilentPunish {
      public static Boolean ENABLED;
      public static String PERMISSION;

      private static void init() {
        pathPrefix("Notifications.Silent-Punish");
        ENABLED = getBoolean("Enabled");
        PERMISSION = getString("Permission");
      }
    }
  }

  public static final class Advanced {
    public static Boolean CACHE_RESULTS;
    public static String DATE_FORMAT = "dd,MM,yyyy";

    private static void init() {
      pathPrefix("Advanced");
      CACHE_RESULTS = getBoolean("Cache_Results");
    }

    public static String formatDate(final long ms) {
      final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
          Advanced.DATE_FORMAT);
      return simpleDateFormat.format(new Date(ms));
    }

    public static final class API {
      public static Boolean ENABLED;

      private static void init() {

        pathPrefix("Advanced.API");
        ENABLED = getBoolean("Enabled");
      }
    }
  }
}
