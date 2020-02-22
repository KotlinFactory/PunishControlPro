package org.mineacademy.punishcontrol.proxy.events;

import lombok.Data;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;
import org.mineacademy.punishcontrol.core.punish.Punish;

@Data
public class PunishCreateEvent extends Event implements Cancellable {

	private final Punish punish;

	private boolean cancelled;
}
