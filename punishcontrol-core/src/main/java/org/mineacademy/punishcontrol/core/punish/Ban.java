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
		return new Ban(target, creator, duration);
	}

	public static Ban of(@NonNull final UUID target, @NonNull final UUID creator, final long ms) {
		return new Ban(target, creator, PunishDuration.of(ms));
	}

	private Ban(final UUID target, final UUID creator, final PunishDuration punishDuration) {
		super(target, creator, punishDuration, PunishType.BAN);
	}

	@Override
	public Ban punishDuration(@NonNull final PunishDuration punishDuration) {
		return (Ban) super.punishDuration(punishDuration);
	}

	public String ip() {
		if (ip != null) {
			return ip;
		}

		//Getting ip?
		return null;

	}
}
