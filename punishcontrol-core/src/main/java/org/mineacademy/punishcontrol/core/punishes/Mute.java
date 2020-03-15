package org.mineacademy.punishcontrol.core.punishes;

import lombok.NonNull;
import org.mineacademy.punishcontrol.core.punish.Punish;
import org.mineacademy.punishcontrol.core.punish.PunishDuration;
import org.mineacademy.punishcontrol.core.punish.PunishType;

import java.util.Map;
import java.util.UUID;

public final class Mute extends Punish {

  private Mute(final UUID target, final UUID creator, final long ms) {
    super(target, creator, PunishDuration.of(ms), PunishType.MUTE, System.currentTimeMillis());
  }

  public Mute(final long creation, @NonNull final Map<String, Object> rawData) {
    super(creation, rawData, PunishType.MUTE);
  }

  public static Mute ofRawData(final long creation, @NonNull final Map<String, Object> rawData) {
    return new Mute(creation, rawData);
  }

  // ----------------------------------------------------------------------------------------------------
  // Static factory-methods
  // ----------------------------------------------------------------------------------------------------

  public static Mute of(
      @NonNull final String creator, @NonNull final String target, final long ms) {
    return Mute.of(UUID.fromString(creator), UUID.fromString(target), ms);
  }

  public static Mute of(
      @NonNull final String target,
      @NonNull final String creator,
      @NonNull final PunishDuration punishDuration) {
    return Mute.of(UUID.fromString(target), UUID.fromString(creator), punishDuration);
  }

  public static Mute of(@NonNull final UUID target, @NonNull final UUID creator, final long ms) {
    return new Mute(target, creator, ms);
  }

  public static Mute of(
      @NonNull final UUID target,
      @NonNull final UUID creator,
      @NonNull final PunishDuration punishDuration) {
    return new Mute(target, creator, punishDuration.toMs());
  }

  // ----------------------------------------------------------------------------------------------------
  // Overridden methods from Punish
  // ----------------------------------------------------------------------------------------------------

  @Override
  public Mute punishDuration(@NonNull final PunishDuration punishDuration) {
    return (Mute) super.punishDuration(punishDuration);
  }

  @Override
  public Mute reason(final String reason) {
    return (Mute) super.reason(reason);
  }

  @Override
  public Mute creation(@NonNull final long creation) {
    return (Mute) super.creation(creation);
  }

  @Override
  public Mute removed(final boolean removed) {
    return (Mute) super.removed(removed);
  }

  @Override
  public Mute ip(@NonNull final String ip) {
    return (Mute) super.ip(ip);
  }

  @Override
  public Mute isSilent(final boolean isSilent) {
    return (Mute) super.isSilent(isSilent);
  }

  @Override
  public Mute isSuperSilent(final boolean isSuperSilent) {
    return (Mute) super.isSuperSilent(isSuperSilent);
  }
}
