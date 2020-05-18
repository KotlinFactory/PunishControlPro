package org.mineacademy.punishcontrol.core.punish.importer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PunishImporters {

  private final List<PunishImporter> importer = new ArrayList<>();

  public List<PunishImporter> importer() {
    return Collections.unmodifiableList(importer);
  }

  public void register(@NonNull final PunishImporter punishImporter) {
    importer.add(punishImporter);
  }
}
