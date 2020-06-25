package org.mineacademy.punishcontrol.proxy.menus.browsers;

import de.exceptionflug.mccommons.inventories.api.ClickType;
import de.exceptionflug.protocolize.items.ItemStack;
import de.exceptionflug.protocolize.items.ItemType;
import de.leonhard.storage.Yaml;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.NonNull;
import lombok.val;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NonNls;
import org.mineacademy.bfo.debug.Debugger;
import org.mineacademy.bfo.plugin.SimplePlugin;
import org.mineacademy.burst.item.Item;
import org.mineacademy.burst.menu.AbstractBrowser;
import org.mineacademy.burst.util.Scheduler;
import org.mineacademy.punishcontrol.core.injector.annotations.Localizable;
import org.mineacademy.punishcontrol.core.injectors.LocalizationInjector;
import org.mineacademy.punishcontrol.core.localization.Localizables;
import org.mineacademy.punishcontrol.core.notification.Notification;
import org.mineacademy.punishcontrol.core.notification.Notifications;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.providers.ExceptionHandler;
import org.mineacademy.punishcontrol.core.setting.YamlStaticConfig;
import org.mineacademy.punishcontrol.core.settings.Localization;
import org.mineacademy.punishcontrol.core.settings.Settings;
import org.mineacademy.punishcontrol.proxy.DaggerProxyComponent;
import org.mineacademy.punishcontrol.proxy.menu.AbstractConfirmMenu;

@Localizable
public final class LanguageBrowser extends AbstractBrowser<Yaml> {

  @NonNls
  @Localizable("IRGEND_EIN_PATH")
  private static String IS_CURRENT_LANGUAGE_CLICK_TO_EDIT = "Is current language. Click to edit";
  @Localizable("Parts.Languages")
  private static String LANGUAGES = "Languages";
  private final Yaml settings;
  private final ExceptionHandler exceptionHandler;
  private final List<Class<?>> classes;

  @Inject
  public LanguageBrowser(
      SettingsBrowser parent,
      @Named("settings") Yaml settings,
      ExceptionHandler exceptionHandler,
      List<Class<?>> classes,
      Collection<Yaml> content) {
    super("LanguageBrowser", parent, content);
    this.settings = settings;
    this.exceptionHandler = exceptionHandler;
    this.classes = classes;
    setTitle("&8" + LANGUAGES);
  }

  public static void showTo(@NonNull final ProxiedPlayer player) {
    Scheduler.runAsync(
        () -> DaggerProxyComponent.create().languageBrowser().displayTo(player)
                      );
  }

  private static boolean isCurrentLanguageFile(Yaml yaml) {
    final String CURRENT = Settings.LOCALE_PREFIX;

    try {
      final String prefix = yaml.getName().split("\\.")[0].split("_")[1];
      return CURRENT.equalsIgnoreCase(prefix);
    } catch (Throwable throwable) {
      Debugger.saveError(
          throwable,
          "Invalid yaml name '" + yaml.getName() + "'",
          "Skipping it.");
      return false;
    }
  }

  @Override
  protected void onClick(ClickType clickType, Yaml yaml) {
    if (isCurrentLanguageFile(yaml))
      LocalizablesBrowser.showTo(getPlayer());
    else {
      // Make current localization
      Debugger.debug("Localization", "Changing localization");
      new AbstractConfirmMenu(this) {

        @Override
        public void onConfirm() {
          Debugger.debug("Localization", "We're here");
          try {
            async(() -> {
              Debugger.debug("Localization", "Started upgrading");
              final String localePrefix = yaml.getName().split("\\.")[0].split("_")[1];
              Settings.LOCALE_PREFIX = localePrefix;
              settings.set("Locale", localePrefix);
              Settings.resetSettingsCall();
              Localization.resetLocalizationCall();

              //TODO: Extract!

              val locale = Providers.createLocalizationYaml(localePrefix);

              Providers.localization(locale);

              YamlStaticConfig.loadAll(Settings.class, Localization.class);
              final LocalizationInjector localizationInjector = new LocalizationInjector(
                  locale);
              Localizables.clear();
              localizationInjector.startInjecting(classes);
              Debugger.debug(
                  "Localization",
                  "Injected: " + Localizables.localizables().size());
              Debugger.debug("Localization", "Changed localization");
            });
          } catch (final Throwable throwable) {
            LanguageBrowser.this.animateTitle("Can't change language - See console");
            exceptionHandler.saveError(
                throwable,
                "LanguageBrowser.onClick(): Can't change language");
          }
        }
      }.displayTo(getPlayer());
    }
  }

  @Override
  protected ItemStack convertToItemStack(Yaml yaml) {
    if (yaml == null)
      return null;

    if (!yaml.contains("Skull_Hash")) {
      String name;
      try {
        name = yaml.getName();
      } catch (final Throwable throwable) {
        name = "unknown";
      }

      Notifications.register(
          Notification
              .of("Invalid localization file!")
              .text(
                  "One of the localization",
                  "files is not formatted probably",
                  "File: " + name)
              .itemType(ItemType.BARRIER));
      return null;
    }

    final String loreString =
        isCurrentLanguageFile(yaml)
            ? "&a" + IS_CURRENT_LANGUAGE_CLICK_TO_EDIT
            : "&7Click to set as current language.";

    return Item
        .of(yaml.getString("Skull_Hash"))
        .name("&6" + yaml.getString("Name"))
        .lore(loreString)
        .build();

  }

  @Override
  protected String[] getInfo() {
    return new String[]{
        "Menu to select",
        "the language of " + SimplePlugin.getNamed()
    };
  }

  @Override
  public void reDisplay() {
    showTo(getPlayer());
  }
}
