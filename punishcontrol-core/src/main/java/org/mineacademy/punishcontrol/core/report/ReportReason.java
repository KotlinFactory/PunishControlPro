package org.mineacademy.punishcontrol.core.report;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@RequiredArgsConstructor
public final class ReportReason {
	@NonNull
	private final String reason;

	@Override
	public String toString() {
		return reason == null ? "Not set" : reason;
	}
}
