package org.mineacademy.punishcontrol.core.settings;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.mineacademy.punishcontrol.core.setting.SimpleLocalization;

public final class Localization extends SimpleLocalization {

  public static final String TARGET_IS_OFFLINE = "&cTarget is offline!";

  @Override
  protected int getConfigVersion() {
    return 1;
  }

  @Getter
  @Accessors(fluent = true)
  @RequiredArgsConstructor
  public enum Time {

    YEAR(getOrSetDefault("Parts.Year", "Year"),
        getOrSetDefault("Hash.Year.Hash",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzUyZmIzODhlMzMyMTJhMjQ3OGI1ZTE1YTk2ZjI3YWNhNmM2MmFjNzE5ZTFlNWY4N2ExY2YwZGU3YjE1ZTkxOCJ9fX0=")),
    MONTH(getOrSetDefault("Parts.Month", "Month"),
        getOrSetDefault("Hash.Month.Hash",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDljNDVhMjRhYWFiZjQ5ZTIxN2MxNTQ4MzIwNDg0OGE3MzU4MmFiYTdmYWUxMGVlMmM1N2JkYjc2NDgyZiJ9fX0=")),
    DAY(getOrSetDefault("Parts.Day", "Day"), getOrSetDefault("Hash.Day.Hash",
        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzE5M2RjMGQ0YzVlODBmZjlhOGEwNWQyZmNmZTI2OTUzOWNiMzkyNzE5MGJhYzE5ZGEyZmNlNjFkNzEifX19")),
    HOUR(getOrSetDefault("Parts.Hour", "Hour"),
        getOrSetDefault("Hash.Hour.Hash",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzFmMzQ2MmE0NzM1NDlmMTQ2OWY4OTdmODRhOGQ0MTE5YmM3MWQ0YTVkODUyZTg1YzI2YjU4OGE1YzBjNzJmIn19fQ==")),
    ;

    static {
      pathPrefix(null);
    }

    private final String localized;
    private final String hash;
  }

  public static final class Punish {

    public static Replacer PUNISH_BROADCAST_MESSAGE;
    public static Replacer BAN_MESSAGE;
    public static Replacer MUTE_MESSAGE;
    public static Replacer WARN_MESSAGE;
    public static String PLAYER_DOES_NOT_EXIST;

    private static void init() {
      pathPrefix(null);
      PLAYER_DOES_NOT_EXIST = getString("Commands.Player.Not_Exists");
      pathPrefix("Punish");
      PUNISH_BROADCAST_MESSAGE = getReplacer("Message_To_Broadcast")
          .find("chat_line", "player", "type", "reason", "ip");

      BAN_MESSAGE = getReplacer("Ban_Message")
          .find("reason", "duration");
      MUTE_MESSAGE = getReplacer("Mute_Message")
          .find("reason", "duration");

      WARN_MESSAGE = getReplacer("Warn_Message")
          .find("reason", "duration");
    }
  }
}
