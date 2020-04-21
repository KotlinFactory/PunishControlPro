package org.mineacademy.punishcontrol.core.permission;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

/**
 * Manager class for your {@link Permission}
 *
 * With this class you can store & registered permissions
 */
@UtilityClass
public class Permissions {

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
}
