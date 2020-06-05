package org.mineacademy.punishcontrol.core.settings;

import javax.inject.Inject;
import lombok.NonNull;
import org.mineacademy.punishcontrol.core.providers.ExceptionHandler;
import org.mineacademy.punishcontrol.core.providers.PluginDataProvider;
import org.mineacademy.punishcontrol.core.setting.AbstractItemSettings;
import org.mineacademy.punishcontrol.core.setting.CustomItem;

public final class ItemSettings extends AbstractItemSettings {

  public static CustomItem BAN_ITEM = CustomItem
      .of("Ban")
      .description("", "");


  @Inject
  public ItemSettings(
      @NonNull final PluginDataProvider pluginDataProvider,
      @NonNull final ExceptionHandler exceptionHandler) {
    super(pluginDataProvider, exceptionHandler);
  }
}
