package org.mineacademy.punishcontrol.core.event;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true, chain = false)
public abstract class AbstractEvent implements Event {
  private boolean canceled;
  private String cancelReason;

  protected AbstractEvent() {
    Events.add(this);
  }
}
