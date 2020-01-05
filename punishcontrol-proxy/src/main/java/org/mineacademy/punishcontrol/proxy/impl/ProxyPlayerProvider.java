package org.mineacademy.punishcontrol.proxy.impl;

import lombok.NonNull;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.mineacademy.bfo.Common;
import org.mineacademy.punishcontrol.core.provider.PlayerProvider;

import java.util.List;
import java.util.UUID;

public class ProxyPlayerProvider implements PlayerProvider {
	@Override
	public List<UUID> getOfflinePlayers() {
		return null;
	}

	@Override
	public boolean isOnline(@NonNull final UUID uuid) {
		return false;
	}

	@Override
	public String getName(@NonNull final UUID uuid) {
		return null;
	}

	@Override
	public UUID getUUID(@NonNull final String name) {
		return null;
	}

	@Override
	public void sendIfOnline(@NonNull final UUID uuid, final @NonNull String... messages) {
		final ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);

		if (player == null) {
			return;
		}

		Common.tell(player, messages);
	}
}
