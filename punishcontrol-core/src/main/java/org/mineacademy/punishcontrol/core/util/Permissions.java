package org.mineacademy.punishcontrol.core.util;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;

@UtilityClass
@FieldDefaults(makeFinal = true, level = AccessLevel.PUBLIC)
public class Permissions {

  public static final String PERMISSION_MENU_SETTINGS_PLAYER = "punishcontrol.menu.settings.player";
  public static final String PERMISSION_MENU_SETTINGS_STORAGE = "punishcontrol.menu.settings.storage";
}
