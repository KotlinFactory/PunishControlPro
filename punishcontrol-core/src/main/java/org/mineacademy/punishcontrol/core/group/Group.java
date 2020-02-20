package org.mineacademy.punishcontrol.core.group;

import lombok.Builder;
import org.mineacademy.punishcontrol.core.punish.PunishDuration;


@Builder
public final class Group {

	private final String name;
	private final String permission;
	private final String item = "STONE";
	private final int priority;

	private final PunishDuration banLimit;
	private final PunishDuration muteLimit;
	private final PunishDuration warnLimit;

	public String name() {
		return name;
	}

	public String permission() {
		return permission;
	}

	public String item() {
		return item;
	}

	public int priority() {
		return priority;
	}
}
