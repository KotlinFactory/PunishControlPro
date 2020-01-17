package org.mineacademy.punishcontrol.spigot.impl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.punishcontrol.core.provider.Providers;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DataSetter implements Listener {

	public static DataSetter newInstance() {
		return new DataSetter();
	}

	@EventHandler(ignoreCancelled = true)
	public void onJoin(final PlayerJoinEvent event) {
		final UUID uuid = event.getPlayer().getUniqueId();
		Common.runLaterAsync(() -> Providers.textureProvider().saveSkinTexture(uuid));
	}
}
