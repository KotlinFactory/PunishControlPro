package org.mineacademy.punishcontrol.proxy.impl;

import lombok.NonNull;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.mineacademy.bfo.Common;
import org.mineacademy.punishcontrol.core.provider.PlayerProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProxyPlayerProvider implements PlayerProvider {
	@Override
	public List<UUID> getOfflinePlayers() {
		return null;
	}

	@Override
	public List<UUID> getOnlinePlayers() {
		final List<UUID> result = new ArrayList<>();

		for (final ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
			result.add(player.getUniqueId());
		}

		return result;
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
	public String getIpIfOnline(@NonNull final UUID uuid) {
		final ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);
		if (player == null || player.getAddress() == null) {
			return null;
		}
		return player.getAddress().getHostName();
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
