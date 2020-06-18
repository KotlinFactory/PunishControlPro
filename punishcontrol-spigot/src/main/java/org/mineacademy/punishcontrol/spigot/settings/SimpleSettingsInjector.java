package org.mineacademy.punishcontrol.spigot.settings;

import lombok.experimental.UtilityClass;
import org.mineacademy.fo.collection.StrictList;
import org.mineacademy.fo.settings.SimpleSettings;
import org.mineacademy.punishcontrol.core.settings.Settings;

/**
 * Wrapper class to use our LightningStorage-based Settings in Foundation.
 */
@UtilityClass
public class SimpleSettingsInjector {

  public void inject() {
    SimpleSettings.DEBUG_SECTIONS = new StrictList<>(Settings.DEBUG_SECTIONS);
    SimpleSettings.LOCALE_PREFIX = Settings.LOCALE_PREFIX;
    SimpleSettings.PLUGIN_PREFIX = Settings.PLUGIN_PREFIX;
    SimpleSettings.LAG_THRESHOLD_MILLIS = Settings.LAG_THRESHOLD_MILLIS;
    SimpleSettings.NOTIFY_UPDATES = Settings.NOTIFY_UPDATES;
  }
}
