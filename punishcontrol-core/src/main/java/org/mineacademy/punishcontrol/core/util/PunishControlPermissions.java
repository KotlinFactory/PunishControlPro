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
      .of(
          "punishcontrol.menu.settings.player",
          "Access the storage-settings")
      .type(PermissionType.MENU);

  public Permission MENU_SETTINGS_PLAYER = Permission
      .of(
          "punishcontrol.menu.settings.storage",
          "Access the player-settings")
      .type(PermissionType.MENU);

  public Permission MENU_SETTINGS_TEMPLATES = Permission
      .of(
          "punishcontrol.menu.settings.templates",
          "Access the template-settings")
      .type(PermissionType.MENU);

  public Permission MENU_SETTINGS_NOTIFICATIONS = Permission
      .of(
          "punishcontrol.menu.settings.notifications",
          "Access the notifications")
      .type(PermissionType.MENU);

  public Permission MENU_SETTINGS_CUSTOMIZATION = Permission
      .of(
          "punishcontrol.menu.settings.customization",
          "Customize PunishControl's menus")
      .type(PermissionType.OTHER);

  public Permission TOGGLE_PUNISHABLE = Permission
      .of(
          "punishcontrol.menu.settings.punishable",
          "Toggle whether you can punish a player or not")
      .type(PermissionType.MENU);

  public Permission IMPORT_PUNISHMENTS = Permission
      .of(
          "punishcontrol.import.punishments",
          "Toggle whether punishments can be importer or not")
      .type(PermissionType.OTHER);

  public Permission MENU_SETTINGS_LANGUAGE = Permission
      .of(
          "punishcontrol.settings.language",
          "Change the language")
      .type(PermissionType.MENU);

  public Permission MENU_SETTINGS_LOCALIZATION = Permission
      .of(
          "punishcontrol.settings.localization",
          "Change the messages of a given localization")
      .type(PermissionType.MENU);
}
