package org.mineacademy.punishcontrol.core.punishes;

import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.punishcontrol.core.punish.Punish;
import org.mineacademy.punishcontrol.core.punish.PunishDuration;
import org.mineacademy.punishcontrol.core.punish.PunishType;

@Setter
@Accessors(chain = true, fluent = true)
public final class Warn extends Punish {

  private Warn(final UUID target, final UUID creator, final long ms) {
    super(target, creator, PunishDuration.of(ms), PunishType.WARN,
        System.currentTimeMillis());
  }

  public Warn(final long creation, final Map<String, Object> rawData) {
    super(creation, rawData, PunishType.WARN);
  }

  public static Warn ofRawData(
      final long creation,
      @NonNull final Map<String, Object> rawData) {
    return new Warn(creation, rawData);
  }

  // ----------------------------------------------------------------------------------------------------
  // Static-Factory-Methods
  // ----------------------------------------------------------------------------------------------------

  public static Warn of(
      @NonNull final UUID target, @NonNull final UUID creator,
      final long ms) {
    return new Warn(target, creator, ms);
  }

  public static Warn of(
      @NonNull final UUID target,
      @NonNull final UUID creator,
      final PunishDuration punishDuration) {
    return new Warn(target, creator, punishDuration.toMs());
  }

  public static Warn of(
      @NonNull final String target,
      @NonNull final String creator,
      @NonNull final PunishDuration punishDuration) {
    return Warn.of(parseUUIDSave(target), parseUUIDSave(creator), punishDuration);
  }

  public static Warn of(
      @NonNull final String target, @NonNull final String creator, final long duration) {
    return Warn.of(parseUUIDSave(target), parseUUIDSave(creator), duration);
  }

  // ----------------------------------------------------------------------------------------------------
  // Overridden methods from Punish
  // ----------------------------------------------------------------------------------------------------

  @Override
  public Warn reason(final String reason) {
    return (Warn) super.reason(reason);
  }

  @Override
  public Warn removed(final boolean removed) {
    return (Warn) super.removed(removed);
  }


  @Override
  public Warn creation(@NonNull final Timestamp timestamp) {
    return (Warn) super.creation(timestamp);
  }

  @Override
  public Warn creation(@NonNull final Long millis) {
    return (Warn) super.creation(millis);
  }

  @Override
  public Warn creation(@NonNull final PunishDuration punishDuration) {
    return (Warn) super.creation(punishDuration);
  }

  @Override
  public Warn ip(@Nullable final String ip) {
    return (Warn) super.ip(ip);
  }

  @Override
  public Warn isSilent(final boolean isSilent) {
    return (Warn) super.isSilent(isSilent);
  }

  @Override
  public Warn isSuperSilent(final boolean isSuperSilent) {
    return (Warn) super.isSuperSilent(isSuperSilent);
  }
}
