package org.mineacademy.punishcontrol.core.permission;

import lombok.*;
import lombok.experimental.Accessors;


@Setter
@Getter
@Accessors(fluent = true, chain = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Permission {

  private final String permission;
  private final String[] description;
  private PermissionType type = PermissionType.COMMAND;

  public static Permission of(@NonNull final String name, final String... desc) {
    return new Permission(name, desc);
  }
}
