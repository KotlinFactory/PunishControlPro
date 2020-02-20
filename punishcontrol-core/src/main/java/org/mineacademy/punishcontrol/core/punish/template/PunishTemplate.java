package org.mineacademy.punishcontrol.core.punish.template;

import de.leonhard.storage.Json;
import lombok.NonNull;
import org.mineacademy.punishcontrol.core.punish.PunishDuration;
import org.mineacademy.punishcontrol.core.punish.PunishType;

import java.io.File;

public final class PunishTemplate extends Json {

	public static PunishTemplate newInstance(final File file) {
		return new PunishTemplate(file);
	}

	private PunishTemplate(@NonNull final File file) {
		super(file);
	}

	// ----------------------------------------------------------------------------------------------------
	//
	// Methods for convenience
	//
	// ----------------------------------------------------------------------------------------------------


	// ----------------------------------------------------------------------------------------------------
	// Getters
	// ----------------------------------------------------------------------------------------------------

	public PunishType punishType() {
		return PunishType.valueOf(getOrDefault("Type", "Ban").toUpperCase());
	}

	public PunishDuration duration() {
		return PunishDuration.of(getOrSetDefault("Duration", "2 days"));
	}

	public String reason() {
		return getOrSetDefault("Reason", "Reason-Here");
	}

	// ----------------------------------------------------------------------------------------------------
	// Setters
	// ----------------------------------------------------------------------------------------------------

	public void punishType(@NonNull final PunishType punishType) {
		set("PunishType", punishType);
	}


	public void reason(@NonNull final String reason) {
		set("Reason", reason);
	}

	public void duration(@NonNull final PunishDuration punishDuration) {
		set("Duration", punishDuration.toString());
	}

}
