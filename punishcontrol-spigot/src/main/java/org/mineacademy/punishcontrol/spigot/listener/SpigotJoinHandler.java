package org.mineacademy.punishcontrol.spigot.listener;

import de.leonhard.storage.util.Valid;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.mineacademy.punishcontrol.core.listener.JoinHandler;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SpigotJoinHandler extends JoinHandler implements Listener {

	public static SpigotJoinHandler newInstance() {
		return new SpigotJoinHandler();
	}

	@Override
	protected void setCancelReason(final List<String> list, @NonNull final Object rawEvent) {
		Valid.checkBoolean(rawEvent instanceof AsyncPlayerPreLoginEvent, "Event must be instance of AsyncPlayerPreLoginEvent");

		final AsyncPlayerPreLoginEvent event = (AsyncPlayerPreLoginEvent) rawEvent;

		event.setKickMessage(String.join("\n", list));
	}

	@Override
	protected void setCancelled(final boolean canceled, final Object rawEvent) {
		Valid.checkBoolean(rawEvent instanceof AsyncPlayerPreLoginEvent, "Event must be instance of AsyncPlayerPreLoginEvent");

		final AsyncPlayerPreLoginEvent event = (AsyncPlayerPreLoginEvent) rawEvent;

		if (canceled) {
			event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
		} else {
			event.setLoginResult(AsyncPlayerPreLoginEvent.Result.ALLOWED);
		}
	}

	@EventHandler
	public final void onJoin(final AsyncPlayerPreLoginEvent event) {
		final UUID uuid = event.getUniqueId();
		handlePlayerJoin(uuid, event);

	}
}
