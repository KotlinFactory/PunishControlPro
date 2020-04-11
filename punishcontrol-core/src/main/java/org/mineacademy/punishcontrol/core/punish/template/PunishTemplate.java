package org.mineacademy.punishcontrol.core.punish.template;

import java.io.File;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.mineacademy.punishcontrol.core.flatfiles.SecureJson;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.providers.ExceptionHandler;
import org.mineacademy.punishcontrol.core.punish.PunishBuilder;
import org.mineacademy.punishcontrol.core.punish.PunishDuration;
import org.mineacademy.punishcontrol.core.punish.PunishType;

public final class PunishTemplate extends SecureJson {

  private PunishTemplate(@NonNull final File file) {
    super(file);
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

  @Override
  public ExceptionHandler exceptionHandler() {
    return Providers.exceptionHandler();
  }

  // ----------------------------------------------------------------------------------------------------
  //
  // Methods for convenience
  //
  // ----------------------------------------------------------------------------------------------------

  // ----------------------------------------------------------------------------------------------------
  // Getters
  // ----------------------------------------------------------------------------------------------------

  public PunishType punishType() {
    return PunishType.valueOf(getOrDefault("Type", "Ban").toUpperCase());
  }

  public PunishDuration duration() {
    return PunishDuration.of(getOrSetDefault("Duration", "2 days"));
  }

  public String name(){
    return file.getName().replace(".json", "");
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
        .reason(reason())
        .silent(silent())
        .superSilent(superSilent());
  }
}
