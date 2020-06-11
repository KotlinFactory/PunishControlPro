package org.mineacademy.punishcontrol.spigot;

import dagger.Component;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.spigot.commands.*;
import org.mineacademy.punishcontrol.spigot.importers.BukkitPunishImporter;
import org.mineacademy.punishcontrol.spigot.listeners.SpigotDataSetter;
import org.mineacademy.punishcontrol.spigot.menus.MainMenu;
import org.mineacademy.punishcontrol.spigot.menus.browsers.*;
import org.mineacademy.punishcontrol.spigot.menus.punish.PunishCreatorMenu;
import org.mineacademy.punishcontrol.spigot.menus.settings.LanguageSettingsMenu;
import org.mineacademy.punishcontrol.spigot.menus.settings.StorageSettingsMenu;

/**
 * Interface implemented by dagger
 */
@Component(modules = {Providers.class, SpigotModule.class})
public interface SpigotComponent {

  // ----------------------------------------------------------------------------------------------------
  // Listener
  // ----------------------------------------------------------------------------------------------------

  SpigotDataSetter spigotDataSetter();

  // ----------------------------------------------------------------------------------------------------
  // Commands
  // ----------------------------------------------------------------------------------------------------

  KickCommand commandKick();

  BanCommand commandBan();

  MuteCommand commandMute();

  WarnCommand commandWarn();

  UnBanCommand commandUnBan();

  UnMuteCommand commandUnMute();

  UnWarnCommand commandUnWarn();

  PlayerInfoCommand commandPlayerInfo();

  SearchCommand searchCommand();

  ChooseActionCommand chooseActionCommand();

  StaffHistoryCommand staffHistoryCommand();

  StaffRollbackCommand staffRollbackCommand();

  // ----------------------------------------------------------------------------------------------------
  // Menus
  // ----------------------------------------------------------------------------------------------------

  MainMenu menuMain();

  PlayerBrowser playerBrowserMenu();

  AllPunishesBrowser punishBrowserMenu();

  PunishTemplateBrowser punishTemplateBrowser();

  PunishCreatorMenu punishCreatorMenu();

  PunishedPlayerBrowser punishedPlayerBrowser();

  //Settings - Menus

  SettingsBrowser settingsBrowser();

  StorageSettingsMenu mySqlSettingsMenu();

  LanguageSettingsMenu languageSettingsMenu();

  StorageSettingsMenu storageMenu();

  CustomItemBrowser customItemBrowser();

  PunishImporterBrowser punishImporterBrowser();


  // ----------------------------------------------------------------------------------------------------
  // PunishImporters
  // ----------------------------------------------------------------------------------------------------

  BukkitPunishImporter bukkitPunishImporter();

   StaffPunishesBrowser.Builder staffPunishesBrowserBuilder();
}
