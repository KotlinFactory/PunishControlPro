package org.mineacademy.punishcontrol.core.punishes;

import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.mineacademy.punishcontrol.core.punish.Punish;
import org.mineacademy.punishcontrol.core.punish.PunishDuration;
import org.mineacademy.punishcontrol.core.punish.PunishType;

import java.util.Map;
import java.util.UUID;

@Setter
@Accessors(chain = true, fluent = true)
public final class Warn extends Punish {

	public static Warn ofRawData(final long creation, @NonNull final Map<String, Object> rawData) {
		return new Warn(creation, rawData);
	}

	public static Warn of(@NonNull final UUID target, @NonNull final UUID creator, final long ms) {
		return new Warn(target, creator, ms);
	}

	public static Warn of(@NonNull final UUID target, @NonNull final UUID creator, final PunishDuration punishDuration) {
		return new Warn(target, creator, punishDuration.toMs());
	}

	// ----------------------------------------------------------------------------------------------------
	// Constructors
	// ----------------------------------------------------------------------------------------------------

	private Warn(final UUID target, final UUID creator, final long ms) {
		super(target, creator, PunishDuration.of(ms), PunishType.WARN, System.currentTimeMillis());
	}

	public Warn(final long creation, final Map<String, Object> rawData) {
		super(creation, rawData, PunishType.WARN);
	}

	// ----------------------------------------------------------------------------------------------------
	// Overridden methods from Punish
	// ----------------------------------------------------------------------------------------------------

	@Override public Warn reason(final String reason) {
		return (Warn) super.reason(reason);
	}

	@Override public Warn removed(final boolean removed) {
		return (Warn) super.removed(removed);
	}

	@Override public Warn creation(@NonNull final long creation) {
		return (Warn) super.creation(creation);
	}

	@Override public Warn ip(@NonNull final String ip) {
		return (Warn) super.ip(ip);
	}

	@Override public Warn isSilent(final boolean isSilent) {
		return (Warn) super.isSilent(isSilent);
	}

	@Override public Warn isSuperSilent(final boolean isSuperSilent) {
		return (Warn) super.isSuperSilent(isSuperSilent);
	}
}
