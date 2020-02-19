package org.mineacademy.punishcontrol.core.group;

import lombok.*;
import lombok.experimental.Accessors;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.provider.providers.PlayerProvider;


@Getter
@Accessors(fluent = true, chain = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Group {
	private static final PlayerProvider provider = Providers.playerProvider();

	@NonNull
	private final String name;
	@NonNull
	@Setter
	private String permission;
	@Setter
	private String item = "STONE";
	private final int priority;

	public static Group of(@NonNull final String name, @NonNull final String permission, final int priority) {
		return new Group(name, permission, priority);
	}
}
