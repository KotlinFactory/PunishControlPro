package org.mineacademy.punishcontrol.proxy;

import dagger.Component;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.proxy.commands.*;
import org.mineacademy.punishcontrol.proxy.listeners.ProxyDataSetter;
import org.mineacademy.punishcontrol.proxy.menus.LocalizableEditorMenu;
import org.mineacademy.punishcontrol.proxy.menus.MainMenu;
import org.mineacademy.punishcontrol.proxy.menus.browsers.*;
import org.mineacademy.punishcontrol.proxy.menus.punish.PunishCreatorMenu;
import org.mineacademy.punishcontrol.proxy.menus.settings.LanguageSettingsMenu;
import org.mineacademy.punishcontrol.proxy.menus.settings.StorageSettingsMenu;

/**
 * Interface implemented by dagger
 */
@Component(modules = {Providers.class, ProxyModule.class})
public interface ProxyComponent {

  // ----------------------------------------------------------------------------------------------------
  // Listener
  // ----------------------------------------------------------------------------------------------------
  ProxyDataSetter proxyDataSetter();

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

  StaffPunishesBrowser.Builder staffPunishesBrowserBuilder();

  //Settings - Menus

  SettingsBrowser settingsBrowser();

  StorageSettingsMenu mySqlSettingsMenu();

  LanguageSettingsMenu languageSettingsMenu();

  StorageSettingsMenu storageSettingsMenu();

  CustomItemBrowser customItemBrowser();

  PunishImporterBrowser punishImporterBrowser();

  LocalizablesBrowser localizableBrowser();

  LanguageBrowser languageBrowser();

  LocalizableEditorMenu.Builder localizableEditorMenuBuilder();
}
