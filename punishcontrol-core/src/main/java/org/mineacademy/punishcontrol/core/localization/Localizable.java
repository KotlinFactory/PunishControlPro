package org.mineacademy.punishcontrol.core.localization;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.mineacademy.punishcontrol.core.setting.Replacer;


@Data
@Builder
@NonNull
@Accessors(fluent = true, chain = true)
public class Localizable {

  private final Class<?> clazz;
  private final Field field;
  private final String path;
  private Object rawValue;

  public List<String> variables() {
    // Implicit null check
    if (!(rawValue instanceof Replacer))
      return new ArrayList<>();

    final Replacer replacer = (Replacer) rawValue;

    return replacer.variables()
        .stream()
        .map(value -> "{" + value + "}")
        .collect(Collectors.toList());
  }

  @SuppressWarnings("unchecked")
  public Collection<String> value() {
    if (rawValue == null)
      return new ArrayList<>();

    if (rawValue instanceof Collection<?>)
      return (Collection<String>) rawValue;

    if (rawValue instanceof String[])
      return Arrays.asList(((String[]) rawValue));

    if (rawValue instanceof Replacer)
      return Arrays.asList(((Replacer) rawValue).replacedMessage());

    return new ArrayList<>();
  }
}
