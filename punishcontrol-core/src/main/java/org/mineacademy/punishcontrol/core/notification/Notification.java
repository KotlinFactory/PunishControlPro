package org.mineacademy.punishcontrol.core.notification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
public class Notification {

  private Object itemType;

  private final String name;

  private List<String> text = new ArrayList<>();

  public static Notification of(@NonNull final String name) {
    return new Notification(name);
  }

  private Notification(@NonNull final String name) {
    this.name = name;
  }

  public Notification text(@NonNull final String... text) {
    this.text = Arrays.asList(text);
    return this;
  }

  public List<String> text() {
    return text;
  }


  private final long creation = System.currentTimeMillis();

  @SuppressWarnings("unchecked")
  public <T> T itemType() {
    return (T) itemType;
  }

  public <T> Notification itemType(final T stack) {
    itemType = stack;
    return this;
  }
}
