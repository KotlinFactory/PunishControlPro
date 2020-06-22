package org.mineacademy.punishcontrol.core.setting;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.*;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class CustomItem {

  public static CustomItem of(@NonNull final String name) {
    return new CustomItem(name);
  }

  public static CustomItem of(
      @NonNull final String name,
      @NonNull final String itemType,
      @NonNull final String... description) {
    return new CustomItem(name, itemType, Arrays.asList(description));
  }

  public static CustomItem of(
      @NonNull final String name,
      @NonNull final String itemType,
      @NonNull final List<String> description) {
    return new CustomItem(name, itemType, description);
  }

  @NonNull
  private String name;
  private String itemType;
  private List<String> description;

  public List<String> description() {
    return Collections.unmodifiableList(description);
  }

  public CustomItem description(@NonNull final String... description) {
    this.description = Arrays.asList(description);
    return this;
  }

  public <T extends Enum<T>> T to(Class<T> enumType) {
    return Enum.valueOf(enumType, itemType);
  }
}
