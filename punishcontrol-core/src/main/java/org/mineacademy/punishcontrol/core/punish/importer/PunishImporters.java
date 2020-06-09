package org.mineacademy.punishcontrol.core.punish.importer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PunishImporters {

  private final List<PunishImporter> importer = new ArrayList<>();

  /**
   * Lists all of your registered {@link PunishImporter}'s
   */
  public List<PunishImporter> importers() {
    return Collections.unmodifiableList(importer);
  }

  /**
   * Lists the PunishImporters that are actually ready to be used (plugin required is
   * installed)
   */
  public List<PunishImporter> applicableImporters() {
    return importers()
        .stream()
        .filter(PunishImporter::applicable)
        .collect(Collectors.toList());
  }

  public void register(@NonNull final PunishImporter punishImporter) {
    importer.add(punishImporter);
  }
}
