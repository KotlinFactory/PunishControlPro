package org.mineacademy.punishcontrol.spigot.listener;

import lombok.NonNull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.provider.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.provider.providers.TextureProvider;

import javax.inject.Inject;
import java.util.UUID;

public final class SpigotDataSetter implements Listener {
	private final TextureProvider textureProvider;
	private final PlayerProvider playerProvider;

	@Inject
	public SpigotDataSetter(@NonNull final TextureProvider textureProvider, @NonNull final PlayerProvider playerProvider) {
		this.textureProvider = textureProvider;
		this.playerProvider = playerProvider;
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
