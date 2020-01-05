package org.mineacademy.punishcontrol.core.punish;

import java.util.UUID;

public class Mute extends Punish {

	protected Mute(final UUID target, final UUID creator, final PunishDuration punishDuration, final PunishType punishType) {
		super(target, creator, punishDuration, punishType);
	}
}
