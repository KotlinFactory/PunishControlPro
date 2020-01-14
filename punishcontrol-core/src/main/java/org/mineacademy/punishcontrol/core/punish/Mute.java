package org.mineacademy.punishcontrol.core.punish;

import java.util.UUID;

public final class Mute extends Punish {


	private Mute(final UUID target, final UUID creator, final PunishDuration punishDuration, final PunishType punishType) {
		super(target, creator, punishDuration, punishType);
	}
}
