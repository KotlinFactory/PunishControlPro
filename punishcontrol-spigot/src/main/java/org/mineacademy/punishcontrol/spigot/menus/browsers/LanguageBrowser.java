package org.mineacademy.punishcontrol.spigot.menus.browsers;

import de.leonhard.storage.Yaml;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.NonNull;
import lombok.val;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NonNls;
import org.mineacademy.fo.debug.Debugger;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.remain.CompMaterial;
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
import org.mineacademy.punishcontrol.spigot.DaggerSpigotComponent;
import org.mineacademy.punishcontrol.spigot.Scheduler;
import org.mineacademy.punishcontrol.spigot.menu.AbstractConfirmMenu;
import org.mineacademy.punishcontrol.spigot.menu.browser.AbstractBrowser;

@Localizable
public final class LanguageBrowser extends AbstractBrowser<Yaml> {

  @NonNls
  @Localizable("Menu.Proxy.LanguageBrowser.IS_CURRENT_LANGUAGE_CLICK_TO_EDIT")
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
    super( parent, content);
    this.settings = settings;
    this.exceptionHandler = exceptionHandler;
    this.classes = classes;
    setTitle("&8" + LANGUAGES);
  }

  public static void showTo(@NonNull final Player player) {
    Scheduler.runAsync(
        () -> DaggerSpigotComponent.create().languageBrowser().displayTo(player)
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
  protected void onPageClick(Player player, Yaml yaml, ClickType click) {
    if (isCurrentLanguageFile(yaml))
      LocalizablesBrowser.showTo(getViewer());
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
      }.displayTo(getViewer());
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
              .itemType(CompMaterial.BARRIER));
      return null;
    }

    final String loreString =
        isCurrentLanguageFile(yaml)
            ? "&a" + IS_CURRENT_LANGUAGE_CLICK_TO_EDIT
            : "&7Click to set as current language.";

    return ItemCreator
        .ofSkullHash(yaml.getString("Skull_Hash"))
        .name("&6" + yaml.getString("Name"))
        .lore(loreString)
        .build()
        .make();

  }

  @Override
  protected String[] getInfo() {
    return new String[]{
        "Menu to select",
        "the language of " + SimplePlugin.getNamed()
    };
  }
}
