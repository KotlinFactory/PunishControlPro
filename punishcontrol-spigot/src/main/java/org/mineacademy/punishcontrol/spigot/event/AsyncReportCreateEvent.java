package org.mineacademy.punishcontrol.spigot.event;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.mineacademy.punishcontrol.core.report.Report;

@Getter
@Setter
public final class AsyncReportCreateEvent extends Event implements Cancellable {
	private final Report report;
	private boolean cancelled;

	public static AsyncReportCreateEvent newInstance(@NonNull final Report report) {
		return new AsyncReportCreateEvent(report);
	}

	private AsyncReportCreateEvent(final Report report) {
		super(true);
		this.report = report;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return new HandlerList();
	}
}
