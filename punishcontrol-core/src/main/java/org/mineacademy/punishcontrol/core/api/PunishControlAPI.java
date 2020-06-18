package org.mineacademy.punishcontrol.core.api;

import de.leonhard.storage.util.Valid;
import lombok.Setter;

public abstract class PunishControlAPI {

  /*
  Don't obfuscate:
  - punishes
  - PunishTemplateManager
  - PunishTemplate

   */

  @Setter
  private static volatile PunishControlAPI instance;

  public static PunishControlAPI getInstance() {
    Valid.notNull(
        instance,
        "The API wasn't loaded",
        "This means that PunishControlPro is either",
        "Not installed on your server or",
        "You were hooking into it before it was loaded.");

    return instance;
  }
}
