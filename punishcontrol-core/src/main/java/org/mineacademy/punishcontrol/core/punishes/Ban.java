package org.mineacademy.punishcontrol.core.punishes;

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
public final class Ban extends Punish {

  private Ban(final long creation, final Map<String, Object> rawData) {
    super(creation, rawData, PunishType.BAN);
  }

  private Ban(
      final UUID target,
      final UUID creator,
      final PunishDuration punishDuration,
      final long dateOfCreation) {
    super(target, creator, punishDuration, PunishType.BAN, dateOfCreation);
  }

  // ----------------------------------------------------------------------------------------------------
  // Factory-Methods
  // ----------------------------------------------------------------------------------------------------

  public static Ban of(
      @NonNull final String target, @NonNull final String creator, final long ms) {
    return Ban.of(target, creator, PunishDuration.of(ms));
  }

  public static Ban of(
      @NonNull final String target,
      @NonNull final String creator,
      @NonNull final PunishDuration punishDuration) {
    return Ban.of(parseUUIDSave(target), parseUUIDSave(creator), punishDuration);
  }

  public static Ban of(
      @NonNull final UUID target,
      @NonNull final UUID creator,
      @NonNull final PunishDuration duration) {
    return new Ban(target, creator, duration, System.currentTimeMillis());
  }

  public static Ban ofRawData(final long creation,
      @NonNull final Map<String, Object> rawData) {
    return new Ban(creation, rawData);
  }
  // ----------------------------------------------------------------------------------------------------
  // Overridden methods for builder
  // ----------------------------------------------------------------------------------------------------

  @Override
  public Ban punishDuration(@NonNull final PunishDuration punishDuration) {
    return (Ban) super.punishDuration(punishDuration);
  }

  @Override
  public Ban reason(final String reason) {
    return (Ban) super.reason(reason);
  }

  @Override
  public Ban creation(@NonNull final long creation) {
    return (Ban) super.creation(creation);
  }

  @Override
  public Ban ip(@Nullable final String ip) {
    return (Ban) super.ip(ip);
  }

  @Override
  public Ban removed(final boolean removed) {
    return (Ban) super.removed(removed);
  }

  @Override
  public Ban isSilent(final boolean isSilent) {
    return (Ban) super.isSilent(isSilent);
  }

  @Override
  public Ban isSuperSilent(final boolean isSuperSilent) {
    return (Ban) super.isSuperSilent(isSuperSilent);
  }
}
