package org.mineacademy.punishcontrol.core.report;

import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(fluent = true)
public final class Report {
	@NonNull
	private final UUID target, creator;
}
