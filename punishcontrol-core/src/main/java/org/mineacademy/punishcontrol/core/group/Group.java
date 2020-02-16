package org.mineacademy.punishcontrol.core.group;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Accessors(fluent = true, chain = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Group {
	@Getter
	@NonNull
	private final String NAME;
	@NonNull
	@Getter
	@Setter
	private List<String> permissions;

	public static Group of(@NonNull final String name) {
		return of(name, new ArrayList<>());
	}

	public static Group of(@NonNull final String name, @NonNull final List<String> permissions) {
		return new Group(name, permissions);
	}

	public Group permissions(@NonNull final String... permissions) {
		//Arrays.asList used to make list mutable
		this.permissions = new ArrayList<>(Arrays.asList(permissions));
		return this;
	}

}
