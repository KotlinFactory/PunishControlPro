package org.mineacademy.punishcontrol.core.punish.template;

import de.leonhard.storage.internal.provider.InputStreamProvider;
import de.leonhard.storage.internal.provider.LightningProviders;
import de.leonhard.storage.util.FileUtils;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@UtilityClass
public class PunishTemplateManager {
  private final List<PunishTemplate> punishTemplates = new ArrayList<>();
  private final InputStreamProvider inputStreamProvider = LightningProviders.inputStreamProvider();

  public void loadTemplates(@NonNull final File directory) {
    for (final File file : FileUtils.listFiles(directory, ".json")) {
      punishTemplates.add(PunishTemplate.load(file));
    }

    // No templates created
    if (punishTemplates.size() == 0) {
      extractAndAddDefaults(directory);
    }
  }

  public List<PunishTemplate> punishTemplates() {
    return Collections.unmodifiableList(punishTemplates);
  }

  private void extractAndAddDefaults(@NonNull final File destination) {
    final InputStream banInputStream =
        inputStreamProvider.createInputStreamFromInnerResource("templates/default-ban.json");
    final InputStream muteInputStream =
        inputStreamProvider.createInputStreamFromInnerResource("templates/default-mute.json");
    final InputStream warnInputStream =
        inputStreamProvider.createInputStreamFromInnerResource("templates/default-warn.json");

    FileUtils.writeToFile(
        FileUtils.getAndMake("default-ban.json", destination.getAbsolutePath()), banInputStream);
    FileUtils.writeToFile(
        FileUtils.getAndMake("default-mute.json", destination.getAbsolutePath()), muteInputStream);
    FileUtils.writeToFile(
        FileUtils.getAndMake("default-warn.json", destination.getAbsolutePath()), warnInputStream);

    punishTemplates.add(
        PunishTemplate.load(
            new File(destination.getAbsolutePath() + "/templates/", "default-ban.json")));
    punishTemplates.add(
        PunishTemplate.load(
            new File(destination.getAbsolutePath() + "/templates/", "default-mute.json")));
    punishTemplates.add(
        PunishTemplate.load(
            new File(destination.getAbsolutePath() + "/templates/", "default-warn.json")));
  }
}
