package org.mineacademy.punishcontrol.core.punish.importer;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public abstract class AbstractImporter implements PunishImporter {

  private final String name;

  protected AbstractImporter(String name) {
    this.name = name;
    PunishImporters.register(this);
  }
}
