package org.mineacademy.punishcontrol.spigot;

import dagger.Component;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.spigot.commands.BanCommand;
import org.mineacademy.punishcontrol.spigot.commands.ChooseActionCommand;
import org.mineacademy.punishcontrol.spigot.commands.KickCommand;
import org.mineacademy.punishcontrol.spigot.commands.MuteCommand;
import org.mineacademy.punishcontrol.spigot.commands.PlayerInfoCommand;
import org.mineacademy.punishcontrol.spigot.commands.SearchCommand;
import org.mineacademy.punishcontrol.spigot.commands.UnBanCommand;
import org.mineacademy.punishcontrol.spigot.commands.UnMuteCommand;
import org.mineacademy.punishcontrol.spigot.commands.UnWarnCommand;
import org.mineacademy.punishcontrol.spigot.commands.WarnCommand;
import org.mineacademy.punishcontrol.spigot.importers.BukkitPunishImporter;
import org.mineacademy.punishcontrol.spigot.listeners.SpigotDataSetter;
import org.mineacademy.punishcontrol.spigot.menus.MainMenu;
import org.mineacademy.punishcontrol.spigot.menus.browsers.AllPunishesBrowser;
import org.mineacademy.punishcontrol.spigot.menus.browsers.CustomItemBrowser;
import org.mineacademy.punishcontrol.spigot.menus.browsers.PlayerBrowser;
import org.mineacademy.punishcontrol.spigot.menus.browsers.PunishTemplateBrowser;
import org.mineacademy.punishcontrol.spigot.menus.browsers.PunishedPlayerBrowser;
import org.mineacademy.punishcontrol.spigot.menus.browsers.SettingsBrowser;
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

  // ----------------------------------------------------------------------------------------------------
  // PunishImporters
  // ----------------------------------------------------------------------------------------------------

  BukkitPunishImporter bukkitPunishImporter();
}
