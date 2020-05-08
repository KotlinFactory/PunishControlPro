package org.mineacademy.punishcontrol.core.conversation;

import de.leonhard.storage.LightningBuilder;
import de.leonhard.storage.Yaml;
import de.leonhard.storage.internal.settings.ConfigSettings;
import de.leonhard.storage.internal.settings.DataType;
import org.mineacademy.punishcontrol.core.fo.constants.FoConstants.Header;
import org.mineacademy.punishcontrol.core.provider.Providers;

public interface SimpleMySQLStorageConversation {

    Yaml yaml =  LightningBuilder
        .fromPath("settings.yml",
            Providers.pluginDataProvider().getDataFolder().getAbsolutePath())
        .addInputStreamFromResource("settings.yml")
        .setConfigSettings(ConfigSettings.PRESERVE_COMMENTS)
        .setDataType(DataType.SORTED)
        .createConfig();

  default void setToConfig(final String key, final Object value) {
    yaml.set(key, value);
    yaml.setHeader(Header.UPDATED_FILE);
  }
}
