package org.mineacademy.punishcontrol.core.event;

import lombok.NonNull;

public interface Event {
  void canceled(boolean canceled);

  boolean canceled();

  void cancelReason(@NonNull final String cancelReason);

  String cancelReason();
}
