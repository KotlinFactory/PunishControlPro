package org.mineacademy.punishcontrol.core.punish;

import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Setter
@Accessors(chain = true, fluent = true)
public final class Warn extends Punish {

	private Warn(final UUID target, final UUID creator, @NonNull final PunishDuration punishDuration, final PunishType punishType) {
		super(target, creator, punishDuration, punishType);
	}

	@Override
	public void create() {

	}
}
