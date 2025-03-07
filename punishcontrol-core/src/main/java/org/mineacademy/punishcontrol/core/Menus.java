package org.mineacademy.punishcontrol.core;

import java.util.UUID;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;

@UtilityClass
public class Menus {

  private final StorageProvider STORAGE_PROVIDER = Providers.storageProvider();

  private final String[] INFO_LORE = {
      "§7Punish / More information", " ", "&7Banned: {false/true}",
      "&7Muted: {false/true}"
  };

  public String[] getInfoForPlayer(@NonNull final UUID target) {
    final boolean isBanned = STORAGE_PROVIDER.isBanned(target);
    final boolean isMuted = STORAGE_PROVIDER.isMuted(target);

    return String.join("\n", INFO_LORE)
        .replace("{Banned}", isBanned + "")
        .replace("{Muted}", isMuted + "")
        .split("\n");
  }

  private String[] replace(
      @NonNull final String[] array,
      @NonNull final CharSequence target,
      @NonNull final CharSequence replacement) {
    final String[] result = new String[array.length];

    for (int i = 0; i < result.length; i++) {
      final String message = result[i];

      result[i] = message.replace(target, replacement);
    }

    return result;
  }
}
