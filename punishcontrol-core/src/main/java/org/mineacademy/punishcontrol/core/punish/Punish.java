package org.mineacademy.punishcontrol.core.punish;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@FieldDefaults(makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Punish {
	UUID target, creator;
	PunishDuration punishDuration;
	PunishType punishType;


	public boolean isOld() {
		return punishDuration.getMs() < System.currentTimeMillis();
	}
}
