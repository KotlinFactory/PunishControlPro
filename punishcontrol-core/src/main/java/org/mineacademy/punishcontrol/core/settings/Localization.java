package org.mineacademy.punishcontrol.core.settings;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.mineacademy.punishcontrol.core.setting.SimpleLocalization;

public class Localization extends SimpleLocalization {

  public static String PLAYER_DOES_NOT_EXIST;

  private static void init() {

    PLAYER_DOES_NOT_EXIST = getString("Player.Not_Exists");
  }

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
    MONTH(getOrSetDefault("Parts.Year", "Year"),
        getOrSetDefault("Hash.Month.Hash",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDljNDVhMjRhYWFiZjQ5ZTIxN2MxNTQ4MzIwNDg0OGE3MzU4MmFiYTdmYWUxMGVlMmM1N2JkYjc2NDgyZiJ9fX0=")),
    DAY(getOrSetDefault("Parts.Year", "Year"), getOrSetDefault("Hash.Day.Hash",
        "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzE5M2RjMGQ0YzVlODBmZjlhOGEwNWQyZmNmZTI2OTUzOWNiMzkyNzE5MGJhYzE5ZGEyZmNlNjFkNzEifX19")),
    HOUR(getOrSetDefault("Parts.Year", "Year"),
        getOrSetDefault("Hash.Hour.Hash",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzFmMzQ2MmE0NzM1NDlmMTQ2OWY4OTdmODRhOGQ0MTE5YmM3MWQ0YTVkODUyZTg1YzI2YjU4OGE1YzBjNzJmIn19fQ==")),
    ;

    private final String localized;
    private final String hash;
  }

}
