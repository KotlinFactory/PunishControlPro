package org.mineacademy.punishcontrol.core.punish;

import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Setter
@Accessors(chain = true, fluent = true)
public final class Ban extends Punish {
	private String ip;

	public static Ban of(@NonNull final UUID target, @NonNull final UUID creator, @NonNull final PunishDuration duration) {
		return new Ban(target, creator, duration, System.currentTimeMillis());
	}

	public static Ban of(@NonNull final UUID target, @NonNull final UUID creator, final long ms) {
		return new Ban(target, creator, PunishDuration.of(ms), System.currentTimeMillis());
	}

	private Ban(final UUID target, final UUID creator, final PunishDuration punishDuration, final long dateOfCreation) {
		super(target, creator, punishDuration, PunishType.BAN, dateOfCreation);
	}

	@Override
	public Ban punishDuration(@NonNull final PunishDuration punishDuration) {
		return (Ban) super.punishDuration(punishDuration);
	}


	@Override public Ban reason(final String reason) {
		return (Ban) super.reason(reason);
	}

	@Override public Ban creation(@NonNull final long creation) {
		return (Ban) super.creation(creation);
	}

	@Override
	public Ban removed(final boolean removed) {
		return (Ban) super.removed(removed);
	}

	@Override
	public String ip() {
		if (ip != null) {
			return ip;
		}

		//Getting ip?
		return "unknown";

	}

	@Override
	public void create() {

	}
}
