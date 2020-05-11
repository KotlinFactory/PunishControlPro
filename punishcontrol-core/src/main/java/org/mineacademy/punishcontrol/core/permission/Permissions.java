package org.mineacademy.punishcontrol.core.permission;

import de.leonhard.storage.util.FileUtils;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.providers.PluginDataProvider;

/**
 * Manager class for your {@link Permission}
 * <p>
 * With this class you can store & registered permissions
 */
@UtilityClass
public class Permissions {

  private final PluginDataProvider PLUGIN_DATA_PROVIDER = Providers.pluginDataProvider();

  private final List<Permission> registeredPermissions = new ArrayList<>();

  public List<Permission> registeredPermissions() {
    return Collections.unmodifiableList(registeredPermissions);
  }

  public void register(@NonNull final Permission permission) {
    registeredPermissions.add(permission);
  }

  public void registerAll(@NonNull final List<Permission> permissions) {
    registeredPermissions.addAll(permissions);
  }

  @SneakyThrows
  public <T> void addFromClass(final Class<?> clazz) {
    for (final Field field : clazz.getFields()) {
      if (field.getType() != Permission.class) {
        continue;
      }

      final Permission perm = (Permission) field.get(null);
      if (perm == null) {
        continue;
      }
      register(perm);
    }
  }


  public void writeToFile() {
    final File dataFile = FileUtils.getAndMake(
        PLUGIN_DATA_PROVIDER.getNamed() + ".perms",
        PLUGIN_DATA_PROVIDER.getDataFolder().getAbsolutePath());

    final List<String> out = new ArrayList<>(Arrays.asList(
        "# " + PLUGIN_DATA_PROVIDER.getNamed() + " v." + PLUGIN_DATA_PROVIDER
            .getVersion(),
        "# This file lists all permissions we use.",
        "# You can also view them using our menu system.",
        "# ",
        "# Do not change them! Please use the settings.yml instead.", 
        ""
    ));

    for (final val perm : registeredPermissions()) {
      out.add(perm.permission() + ";" + perm.type() + ";" + String
          .join(",", perm.description()));
    }

    FileUtils.write(dataFile, out);
  }


}
