package org.mineacademy.punishcontrol.core.util;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import org.mineacademy.punishcontrol.core.permission.Permission;
import org.mineacademy.punishcontrol.core.permission.PermissionType;

@UtilityClass
@FieldDefaults(makeFinal = true, level = AccessLevel.PUBLIC)
public class PunishControlPermissions {
  public Permission MENU_SETTINGS_STORAGE = Permission
      .of("punishcontrol.menu.settings.player",
          "Access the storage-settings")
      .type(PermissionType.MENU);

  public Permission MENU_SETTINGS_PLAYER = Permission
      .of("punishcontrol.menu.settings.storage",
      "Access the player-settings")
      .type(PermissionType.MENU);

  public Permission MENU_SETTINGS_TEMPLATES = Permission
      .of("punishcontrol.menu.settings.templates",
          "Access the template-settings")
      .type(PermissionType.MENU);

  public Permission MENU_SETTINGS_NOTIFICATIONS = Permission
      .of("punishcontrol.menu.settings.notifications",
          "Access the notifications")
      .type(PermissionType.MENU);


}
