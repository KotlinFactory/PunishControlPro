package org.mineacademy.punishcontrol.proxy.menus.settings;

import java.util.ArrayList;
import lombok.experimental.UtilityClass;
import org.mineacademy.bfo.settings.SimpleSettings;
import org.mineacademy.punishcontrol.core.settings.Settings;

@UtilityClass
public class SimpleSettingsInjector {
  public void inject(){
    SimpleSettings.DEBUG_SECTIONS = new ArrayList<>(Settings.DEBUG_SECTIONS);
    SimpleSettings.LOCALE_PREFIX = Settings.LOCALE_PREFIX;
    SimpleSettings.PLUGIN_PREFIX = Settings.PLUGIN_PREFIX;
    SimpleSettings.LAG_THRESHOLD_MILLIS = Settings.LAG_THRESHOLD_MILLIS;
    SimpleSettings.NOTIFY_UPDATES = Settings.NOTIFY_UPDATES;
  }
}