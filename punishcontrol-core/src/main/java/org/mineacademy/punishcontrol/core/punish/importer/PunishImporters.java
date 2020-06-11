package org.mineacademy.punishcontrol.core.punish.importer;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PunishImporters {

  private final Set<PunishImporter> importer = new HashSet<>();

  /**
   * Lists all of your registered {@link PunishImporter}'s
   */
  public Set<PunishImporter> importers() {
    return Collections.unmodifiableSet(importer);
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
