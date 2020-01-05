package org.mineacademy.punishcontrol.core.report;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor()
public final class Report {
	@NonNull
	private final UUID target, creator;


}
