package org.mineacademy.punishcontrol.spigot.event;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.mineacademy.punishcontrol.core.warn.Warn;

@Getter
@Setter
public final class AsyncWarnCreateEvent extends Event implements Cancellable {
	private final Warn warn;
	private boolean cancelled;

	public static AsyncWarnCreateEvent newInstance(@NonNull final Warn warn) {
		return new AsyncWarnCreateEvent(warn);
	}

	private AsyncWarnCreateEvent(final Warn warn) {
		super(true);
		this.warn = warn;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return new HandlerList();
	}
}
