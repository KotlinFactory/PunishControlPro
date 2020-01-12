package org.mineacademy.punishcontrol.core.punish;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data()
@FieldDefaults(makeFinal = true)
public abstract class Punish {
	UUID target, creator;
	PunishDuration punishDuration;
	PunishType punishType;

	public boolean isOld() {
		return punishDuration.getMs() < System.currentTimeMillis();
	}
}
