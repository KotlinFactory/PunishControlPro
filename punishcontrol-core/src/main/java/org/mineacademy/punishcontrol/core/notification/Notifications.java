package org.mineacademy.punishcontrol.core.notification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Notifications {

  private final List<Notification> notifications = new ArrayList<>();

  public List<Notification> registeredNotifications() {
    return Collections.unmodifiableList(notifications);
  }

  public void register(@NonNull final Notification notification) {
    notifications.add(notification);
  }

  public void unregister(@NonNull final Notification notification) {
    notifications.remove(notification);
  }
}
