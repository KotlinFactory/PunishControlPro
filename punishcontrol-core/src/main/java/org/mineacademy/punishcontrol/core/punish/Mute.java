package org.mineacademy.punishcontrol.core.punish;

import lombok.NonNull;

import java.util.UUID;

public final class Mute extends Punish {

	private Mute(final UUID target, final UUID creator, final long ms) {
		super(target, creator, PunishDuration.of(ms), PunishType.MUTE, System.currentTimeMillis());
	}

	public static Mute of(@NonNull final UUID target, @NonNull final UUID creator, final long ms) {
		return new Mute(target, creator, ms);
	}

	public static Mute of(@NonNull final UUID target, @NonNull final UUID creator, @NonNull final PunishDuration punishDuration) {
		return new Mute(target, creator, punishDuration.toMs());
	}

	@Override
	public Mute punishDuration(@NonNull final PunishDuration punishDuration) {
		return (Mute) super.punishDuration(punishDuration);
	}

	@Override public Mute reason(final String reason) {
		return (Mute) super.reason(reason);
	}

	@Override public Mute creation(@NonNull final long creation) {
		return (Mute) super.creation(creation);
	}

	@Override
	public Mute removed(final boolean removed) {
		return (Mute) super.removed(removed);
	}

	@Override
	public void create() {
	}
}
