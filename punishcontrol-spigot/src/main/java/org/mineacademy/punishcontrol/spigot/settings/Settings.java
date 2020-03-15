package org.mineacademy.punishcontrol.spigot.settings;

import org.mineacademy.fo.settings.SimpleSettings;
import org.mineacademy.punishcontrol.core.storage.StorageType;

import java.util.ArrayList;
import java.util.List;

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

    private static void init() {
      pathPrefix("Advanced");
      CACHE_RESULTS = getBoolean("Cache_Results");
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
