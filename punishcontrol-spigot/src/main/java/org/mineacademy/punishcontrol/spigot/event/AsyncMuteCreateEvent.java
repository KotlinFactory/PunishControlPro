package org.mineacademy.punishcontrol.spigot.event;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.mineacademy.punishcontrol.core.punish.Mute;


@Getter
@Setter
public final class AsyncMuteCreateEvent extends Event implements Cancellable {

	private final Mute mute;
	private boolean cancelled;

	public static AsyncMuteCreateEvent newInstance(@NonNull final Mute mute) {
		return new AsyncMuteCreateEvent(mute);
	}

	private AsyncMuteCreateEvent(final Mute mute) {
		super(true);
		this.mute = mute;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return new HandlerList();
	}
}
