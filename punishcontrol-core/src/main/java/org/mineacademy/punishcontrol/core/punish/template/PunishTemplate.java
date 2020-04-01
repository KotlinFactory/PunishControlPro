package org.mineacademy.punishcontrol.core.punish.template;

import de.leonhard.storage.Json;
import de.leonhard.storage.util.FileUtils;
import de.leonhard.storage.util.Valid;
import java.io.File;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.providers.ExceptionHandler;
import org.mineacademy.punishcontrol.core.punish.PunishBuilder;
import org.mineacademy.punishcontrol.core.punish.PunishDuration;
import org.mineacademy.punishcontrol.core.punish.PunishType;

public final class PunishTemplate extends Json {

  private static final ExceptionHandler EXCEPTION_HANDLER = Providers.exceptionHandler();
  private final boolean canWrite;

  private PunishTemplate(@NonNull final File file) {
    super(file);
    canWrite = file.canWrite();
  }

  public static PunishTemplate load(@NotNull final File file) {
    return new PunishTemplate(file);
  }

  public static PunishTemplate createWithDefaults(@NotNull final File file) {
    final PunishTemplate template = new PunishTemplate(file);

    // Setting defaults
    template.permission();
    template.reason();
    template.permission();

    return template;
  }

  public static Optional<PunishTemplate> getByName(
      @NonNull final File folder, @NonNull final String name) {
    final List<File> files = FileUtils.listFiles(folder, ".json");

    final Optional<File> optionalFile =
        files.stream()
            .filter((file) -> file.getName().replace(".json", "").equalsIgnoreCase(name))
            .findFirst();

    // If optionalFile is present -> PunishTemplate of this file else: Optional.empty()
    return optionalFile.map(PunishTemplate::new);

    //
  }

  // ----------------------------------------------------------------------------------------------------
  //
  // Methods for convenience
  //
  // ----------------------------------------------------------------------------------------------------

  // ----------------------------------------------------------------------------------------------------
  // Overridden methods of DataStorage (LightningStorage) to provide better exception-handling
  // ----------------------------------------------------------------------------------------------------

  @Override
  public <T> T getOrSetDefault(final String key, @NonNull final T def) {
    try {
      super.getOrSetDefault(key, def);
    } catch (final Throwable throwable) {
      EXCEPTION_HANDLER.saveError(
          throwable,
          "Exception while trying searching: '" + key + "' in '" + file.getName() + "'",
          "Have you altered the data?",
          "Additional data: searched: " + def.getClass().getSimpleName());
    }
    return def;
  }

  @Override
  public void set(final String key, @NonNull final Object value) {
    Valid.checkBoolean(
        canWrite,
        "Can't write to file - Access denied",
        "This is not an issue of PunishControlPro");
    try {
      super.set(key, value);
    } catch (final Throwable throwable) {
      EXCEPTION_HANDLER.saveError(
          throwable,
          "Exception while writing to '" + file.getName() + "'",
          "Path: '" + key + "'",
          "Value-Type '" + value.getClass().getSimpleName() + "'",
          "Value: '" + value.toString() + "'");
    }
  }

  // ----------------------------------------------------------------------------------------------------
  // Getters
  // ----------------------------------------------------------------------------------------------------


  public PunishType punishType() {
    return PunishType.valueOf(getOrDefault("Type", "Ban").toUpperCase());
  }

  public PunishDuration duration() {
    return PunishDuration.of(getOrSetDefault("Duration", "2 days"));
  }

  public String reason() {
    return getOrSetDefault("Reason", "Reason-Here");
  }

  public boolean silent() {
    return getOrSetDefault("Silent", false);
  }

  public boolean superSilent() {
    return getOrSetDefault("SuperSilent", false);
  }

  public String permission() {
    return getOrSetDefault(
        "Permission", "punishcontrol.template." + file.getName().replace(".json", ""));
  }

  // ----------------------------------------------------------------------------------------------------
  // Setters
  // ----------------------------------------------------------------------------------------------------

  public void punishType(@NonNull final PunishType punishType) {
    set("PunishType", punishType);
  }

  public void reason(@NonNull final String reason) {
    set("Reason", reason);
  }

  public void duration(@NonNull final PunishDuration punishDuration) {
    set("Duration", punishDuration.toString());
  }

  public PunishBuilder toPunishBuilder() {
    return PunishBuilder.of(punishType())
        .duration(duration())
        .silent(silent())
        .superSilent(superSilent());
  }
}
