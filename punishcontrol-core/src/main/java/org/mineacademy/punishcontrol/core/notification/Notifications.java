package org.mineacademy.punishcontrol.core.notification;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Notifications {

  private final Set<Notification> notifications = new HashSet<>();

  public Set<Notification> registeredNotifications() {
    return Collections.unmodifiableSet(notifications);
  }

  public void register(@NonNull final Notification notification) {
    notifications.add(notification);
  }

  public void unregister(@NonNull final Notification notification) {
    notifications.remove(notification);
  }
}
