package org.mineacademy.punishcontrol.proxy.impl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.mineacademy.bfo.Common;
import org.mineacademy.bfo.debug.Debugger;
import org.mineacademy.burst.provider.UUIDNameProvider;
import org.mineacademy.punishcontrol.core.provider.AbstractPlayerProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProxyPlayerProvider extends AbstractPlayerProvider implements UUIDNameProvider { //Compatibility

	public static ProxyPlayerProvider newInstance() {
		return new ProxyPlayerProvider();
	}

	@Override
	public List<UUID> getOfflinePlayers() {
		final List<UUID> result = new ArrayList<>();
		for (final String str : singleLayerKeySet()) {
			try {
				result.add(UUID.fromString(str));
			} catch (final Throwable throwable) {
				Debugger.saveError(throwable);
			}
		}

		return result;
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
		return ProxyServer.getInstance().getPlayer(uuid) != null;
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
