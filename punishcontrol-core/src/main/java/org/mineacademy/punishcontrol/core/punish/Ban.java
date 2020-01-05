package org.mineacademy.punishcontrol.core.punish;

import java.util.UUID;

public class Ban extends Punish {

	protected Ban(final UUID target, final UUID creator, final PunishDuration punishDuration, final PunishType punishType) {
		super(target, creator, punishDuration, punishType);
	}
}
