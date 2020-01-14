package org.mineacademy.punishcontrol.core.punish;

import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import java.util.UUID;

@Data
@FieldDefaults(makeFinal = true)
@Accessors(chain = true, fluent = true)
public abstract class Punish {
	UUID target, creator;
	@NonFinal
	@NonNull
	PunishDuration punishDuration;
	PunishType punishType;

	public boolean isOld() {
		return punishDuration.getMs() < System.currentTimeMillis();
	}
}
