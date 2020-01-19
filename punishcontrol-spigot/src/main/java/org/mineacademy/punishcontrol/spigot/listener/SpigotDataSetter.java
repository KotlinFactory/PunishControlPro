package org.mineacademy.punishcontrol.spigot.listener;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.punishcontrol.core.provider.Providers;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SpigotDataSetter implements Listener {

	public static SpigotDataSetter newInstance() {
		return new SpigotDataSetter();
	}

	@EventHandler(ignoreCancelled = true)
	public void onJoin(final PlayerJoinEvent event) {
		final UUID uuid = event.getPlayer().getUniqueId();
		final String name = event.getPlayer().getName();
		Common.runLaterAsync(() -> {
			Providers.textureProvider().saveSkinTexture(uuid);
			Providers.playerProvider().saveUUIDAndName(uuid, name);
		});
	}
}
