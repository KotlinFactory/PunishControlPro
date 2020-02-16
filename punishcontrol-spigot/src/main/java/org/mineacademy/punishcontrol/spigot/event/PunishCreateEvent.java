package org.mineacademy.punishcontrol.spigot.event;

import lombok.Data;
import lombok.NonNull;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.mineacademy.punishcontrol.core.punish.Punish;
import org.mineacademy.punishcontrol.core.punish.PunishType;

@Data
public class PunishCreateEvent extends Event implements Cancellable {
	private boolean cancelled;

	private final Punish punish;

	public PunishCreateEvent(@NonNull final Punish punish) {
		super(true);
		this.punish = punish;
	}

	public PunishType punishType() {
		return punish.punishType();
	}


	@Override public @NotNull HandlerList getHandlers() {
		return new HandlerList();
	}
}
