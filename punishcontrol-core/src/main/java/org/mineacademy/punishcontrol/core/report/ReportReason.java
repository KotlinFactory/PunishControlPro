package org.mineacademy.punishcontrol.core.report;


import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public final class ReportReason {
	@NonNull
	private final String reason;

	@Override
	public String toString() {
		return reason == null ? "Not set" : reason;
	}
}
