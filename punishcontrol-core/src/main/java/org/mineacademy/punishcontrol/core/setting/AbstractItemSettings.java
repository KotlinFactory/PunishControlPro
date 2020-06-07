package org.mineacademy.punishcontrol.core.setting;

import de.leonhard.storage.LightningBuilder;
import de.leonhard.storage.Yaml;
import de.leonhard.storage.internal.settings.ConfigSettings;
import de.leonhard.storage.internal.settings.DataType;
import de.leonhard.storage.util.Valid;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.mineacademy.punishcontrol.core.providers.ExceptionHandler;
import org.mineacademy.punishcontrol.core.providers.PluginDataProvider;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractItemSettings {

  protected final PluginDataProvider pluginDataProvider;
  protected final ExceptionHandler exceptionHandler;

  protected static final List<CustomItem> items = new ArrayList<>();
  private Yaml config;

  public static List<CustomItem> items() {
    return Collections.unmodifiableList(items);
  }

  protected final Yaml getConfigInstance() {
    if (config != null) {
      return config;
    }

    return config = LightningBuilder
        .fromPath("Items.yml", pluginDataProvider.getDataFolder().getAbsolutePath())
        .addInputStreamFromResource("Items.yml")
        .setConfigSettings(ConfigSettings.PRESERVE_COMMENTS)
        .setDataType(DataType.SORTED)
        .createConfig()
        .addDefaultsFromInputStream();
  }

  public void load() {
    try {
      load0();
    } catch (Throwable throwable) {
      exceptionHandler.saveError(throwable, "Exception while loading CustomItems");
    }
  }

  private void load0() throws IllegalAccessException {
    for (final Field field : getClass().getFields()) {
      if (field.getType() != CustomItem.class) {
        continue;
      }

      final CustomItem item = (CustomItem) field.get(null);

      Valid.notNull(
          item.name(),
          "Name of " + item.getClass().getSimpleName() + " mustn't be null");

      Valid.notNull(
          item.itemType(),
          "Itemtype of " + item.name() + " mustn't be null");

      // Getting data out of our yaml.
      item.itemType(getConfigInstance().getOrSetDefault(item.name() + ".Type", item.itemType()));

      // Adding to our registered items
      items.add(item);
    }
  }

  public Optional<CustomItem> find(@NonNull final String name) {
    for (CustomItem item : items()) {
      if (item.name() == null) {
        continue;
      }

      if (item.name().equalsIgnoreCase(name)) {
        return Optional.of(item);
      }
    }

    return Optional.empty();
  }

  public void setType(
      final CustomItem customItem,
      final String newType) {
    customItem.itemType(newType);
    getConfigInstance().set(customItem.name() + ".Type", newType);
  }
}
