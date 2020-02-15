package org.mineacademy.punishcontrol.core.punish;

import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.NonFinal;

import java.util.UUID;

@Data
@Accessors(chain = true, fluent = true)
public abstract class Punish {
	private String reason;
	private String ip;
	private final UUID target, creator;
	@NonNull private PunishDuration punishDuration;
	private final PunishType punishType;
	@NonNull private long creation;

	@NonFinal private boolean removed = false;

	public boolean isOld() {
		if (removed()) {
			return true;
		}
		return punishDuration.toMs() < System.currentTimeMillis();
	}

	public abstract void create();
}
