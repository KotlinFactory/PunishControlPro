package org.mineacademy.punishcontrol.core.punish.template;

import de.leonhard.storage.internal.provider.InputStreamProvider;
import de.leonhard.storage.internal.provider.LightningProviders;
import de.leonhard.storage.util.FileUtils;
import java.io.File;
import java.io.InputStream;
import java.util.*;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.mineacademy.punishcontrol.core.group.Groups;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;

@UtilityClass
public class PunishTemplates {

  private final PlayerProvider playerProvider = Providers.playerProvider();
  private final List<PunishTemplate> registeredTemplates = new ArrayList<>();
  private final InputStreamProvider inputStreamProvider = LightningProviders
      .inputStreamProvider();

  public boolean unregister(final PunishTemplate punishTemplate) {
    return registeredTemplates.remove(punishTemplate);
  }

  public void register(@NonNull final PunishTemplate punishTemplate) {
    registeredTemplates.add(punishTemplate);
  }

  public Optional<PunishTemplate> fromName(@NonNull final String name) {
    for (final PunishTemplate template : registeredTemplates) {
      if (!template.name().equalsIgnoreCase(name)) {
        continue;
      }
      return Optional.of(template);
    }

    return Optional.empty();
  }

  public boolean hasAccess(
      @NonNull final UUID target,
      @NonNull final PunishTemplate punishTemplate) {
    final val optionalGroup = Groups.primaryGroupOf(target);

    if (optionalGroup.isPresent()) {
      final val group = optionalGroup.get();
      if (group.templateByPasses().contains("*")) {
        return true;
      }
      if (group.templateByPasses().contains(punishTemplate.name())) {
        return true;
      }
    }

    return playerProvider.hasPermission(target, punishTemplate.permission());
  }

  public void load(@NonNull final File directory) {
    for (final File file : FileUtils.listFiles(directory, ".json")) {
      registeredTemplates.add(PunishTemplate.load(file));
    }

    // No templates created
    if (registeredTemplates.size() == 0) {
      extractAndAddDefaults(directory);
    }
  }

  public List<PunishTemplate> list() {
    return Collections.unmodifiableList(registeredTemplates);
  }

  private void extractAndAddDefaults(@NonNull final File destination) {
    final InputStream banInputStream =
        inputStreamProvider
            .createInputStreamFromInnerResource("templates/default-ban.json");
    final InputStream muteInputStream =
        inputStreamProvider
            .createInputStreamFromInnerResource("templates/default-mute.json");
    final InputStream warnInputStream =
        inputStreamProvider
            .createInputStreamFromInnerResource("templates/default-warn.json");

    FileUtils.writeToFile(
        FileUtils.getAndMake("default-ban.json", destination.getAbsolutePath()),
        banInputStream);
    FileUtils.writeToFile(
        FileUtils
            .getAndMake("default-mute.json", destination.getAbsolutePath()),
        muteInputStream);
    FileUtils.writeToFile(
        FileUtils
            .getAndMake("default-warn.json", destination.getAbsolutePath()),
        warnInputStream);

    registeredTemplates.add(
        PunishTemplate.load(
            new File(
                destination.getAbsolutePath() + "/templates/",
                "default-ban.json")));
    registeredTemplates.add(
        PunishTemplate.load(
            new File(
                destination.getAbsolutePath() + "/templates/",
                "default-mute.json")));
    registeredTemplates.add(
        PunishTemplate.load(
            new File(
                destination.getAbsolutePath() + "/templates/",
                "default-warn.json")));
  }
}
