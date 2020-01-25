package org.mineacademy.punishcontrol.spigot.event;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.mineacademy.punishcontrol.core.punish.Ban;

@Getter
@Setter
public final class AsyncBanCreateEvent extends Event implements Cancellable {

	private final Ban ban;
	private boolean cancelled;

	public static AsyncBanCreateEvent newInstance(@NonNull final Ban ban) {
		return new AsyncBanCreateEvent(ban);
	}

	private AsyncBanCreateEvent(final Ban ban) {
		super(true);
		this.ban = ban;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return new HandlerList();
	}


}
