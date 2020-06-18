package org.mineacademy.punishcontrol.core.localization;

import java.lang.reflect.Field;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;


@Data
@Builder
@NonNull
@Accessors(fluent = true, chain = true)
public class Localizable {

  private final Class<?> clazz;
  private final Field field;
  private final String path;
}
