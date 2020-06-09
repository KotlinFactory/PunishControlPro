package org.mineacademy.punishcontrol.importers.punishimporter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.mineacademy.punishcontrol.core.punish.importer.PunishImporter;

@UtilityClass
public class PunishImporters {

  private final List<org.mineacademy.punishcontrol.core.punish.importer.PunishImporter> importer = new ArrayList<>();

  /**
   * Lists all of your registered {@link org.mineacademy.punishcontrol.core.punish.importer.PunishImporter}'s
   */
  public List<org.mineacademy.punishcontrol.core.punish.importer.PunishImporter> importers() {
    return Collections.unmodifiableList(importer);
  }

  /**
   * Lists the PunishImporters that are actually ready to be used (plugin required is
   * installed)
   */
  public List<org.mineacademy.punishcontrol.core.punish.importer.PunishImporter> applicableImporters() {
    return importers()
        .stream()
        .filter(org.mineacademy.punishcontrol.core.punish.importer.PunishImporter::isApplicable)
        .collect(Collectors.toList());
  }

  public void register(@NonNull final PunishImporter punishImporter) {
    importer.add(punishImporter);
  }
}
