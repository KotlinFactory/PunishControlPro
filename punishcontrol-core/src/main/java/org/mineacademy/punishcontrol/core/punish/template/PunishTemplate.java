package org.mineacademy.punishcontrol.core.punish.template;

import de.leonhard.storage.Json;
import de.leonhard.storage.util.FileUtils;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.mineacademy.punishcontrol.core.punish.PunishDuration;
import org.mineacademy.punishcontrol.core.punish.PunishType;

import java.io.File;
import java.util.List;
import java.util.Optional;

public final class PunishTemplate extends Json {

	public static PunishTemplate load(@NotNull final File file) {
		return new PunishTemplate(file);
	}

	public static PunishTemplate createWithDefaults(@NotNull final File file) {
		final PunishTemplate template = new PunishTemplate(file);

		//Setting defaults
		template.permission();
		template.reason();
		template.permission();

		return template;
	}

	public static Optional<PunishTemplate> getByName(@NonNull final File folder, @NonNull final String name) {
		final List<File> files = FileUtils.listFiles(folder, ".json");

		final Optional<File> optionalFile = files.stream().filter((file) -> file.getName().replace(".json", "").equalsIgnoreCase(name)).findFirst();

		//If optionalFile is present -> PunishTemplate of this file else: Optional.empty()
		return optionalFile.map(PunishTemplate::new);

		//
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

	public String permission() {
		return getOrSetDefault("Permission", "punishcontrol.template." + file.getName().replace(".json", ""));
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
