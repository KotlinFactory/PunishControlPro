package org.mineacademy.punishcontrol.core.punish.importers;

import org.mineacademy.punishcontrol.core.punish.importer.AbstractImporter;

public final class ExampleImporter extends AbstractImporter {

  public static ExampleImporter create() {
    return new ExampleImporter();
  }

  private ExampleImporter() {
    super("example");
  }
}
