package org.mineacademy.punishcontrol.core.punish;

import de.leonhard.storage.util.Valid;
import java.util.Optional;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.punishcontrol.core.punishes.Ban;
import org.mineacademy.punishcontrol.core.punishes.Mute;
import org.mineacademy.punishcontrol.core.punishes.Warn;

/**
 * Class to build a punishment from a given punish-type and other information
 *
 * <p>Especially useful if we don't know which punishtype finally will be
 * choosen like in the specific Punish-Commands
 * <p>
 * Every field exluding punishType might be null! * Don't access without prior
 * null-checks
 *
 * <p>* TODO: do not obfuscate!
 */
@Getter
@Setter
@Accessors(fluent = true, chain = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@NonNull
public final class PunishBuilder {

  @NonNull
  private PunishType punishType;
  private UUID target;
  private UUID creator;
  private PunishDuration duration;
  private long creation;
  @Nullable
  private String ip;
  private boolean silent, superSilent, removed;

  private String reason;

  public static PunishBuilder of(final PunishType punishType) {
    return new PunishBuilder(punishType);
  }

  // ----------------------------------------------------------------------------------------------------
  // Methods which can't be handled by lombok
  // ----------------------------------------------------------------------------------------------------

  public Optional<String> ip() {
    return Optional.ofNullable(ip);
  }

  public PunishBuilder duration(@NonNull final PunishDuration punishDuration) {
    duration = punishDuration;
    return this;
  }

  public PunishBuilder duration(final long duration) {
    this.duration = PunishDuration.of(duration);
    return this;
  }

  // ----------------------------------------------------------------------------------------------------
  // Building
  // ----------------------------------------------------------------------------------------------------

  public Punish build() {

    Valid.notNull(punishType, "Forgot to set punishtype in builder!");
    Valid.notNull(target, "Forgot to set target in builder!");
    Valid.notNull(creator, "Forgot to set creator in builder!");
    Valid.notNull(reason, "Forgot to set reason in builder!");

    switch (punishType) {
      case BAN:
        return Ban.of(target, creator, duration)
            .reason(reason)
            .creation(creation)
            .ip(ip)
            .removed(removed)
            .isSilent(silent)
            .isSuperSilent(superSilent);
      case MUTE:
        return Mute.of(target, creator, duration)
            .reason(reason)
            .creation(creation)
            .ip(ip)
            .removed(removed)
            .isSilent(silent)
            .isSuperSilent(superSilent);
      case WARN:
        return Warn.of(target, creator, duration)
            .reason(reason)
            .creation(creation)
            .ip(ip)
            .reason(reason)
            .removed(removed)
            .isSilent(silent)
            .isSuperSilent(superSilent);
    }
    Valid.error("Invalid punishtype! Neither WARN/BAN/MUTE!",
        "What have you done to your jdk?!");
    return build(); // Will never be executed. Just here because of the java-compiler.
  }
}
