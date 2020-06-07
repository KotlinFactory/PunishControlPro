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
      .itemType("ACACIA_DOOR")
      .description("Item representing ban's");

  public static CustomItem MUTE_ITEM = CustomItem
      .of("Mute")
      .itemType("PAPER")
      .description("Item representing mute's");

  public static CustomItem APPLY_ITEM = CustomItem
      .of("Apply")
      .itemType("EMERALD_BLOCK")
      .description("Item clicked", "When something is going to be applied");

  public static CustomItem REASON_ITEM = CustomItem
      .of("Reason")
      .itemType("BOOK")
      .description("Item presenting a reason");

  public static CustomItem ADD_ITEM = CustomItem
      .of("&aAdd new")
      .itemType("EMERALD")
      .description("Item clicked when", "Adding new punishment templates");

  @Inject
  public ItemSettings(
      @NonNull final PluginDataProvider pluginDataProvider,
      @NonNull final ExceptionHandler exceptionHandler) {
    super(pluginDataProvider, exceptionHandler);
  }
}
