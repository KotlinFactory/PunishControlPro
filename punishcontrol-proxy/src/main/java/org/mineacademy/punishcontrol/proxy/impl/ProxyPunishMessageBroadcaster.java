package org.mineacademy.punishcontrol.proxy.impl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.mineacademy.punishcontrol.core.punish.Punish;
import org.mineacademy.punishcontrol.core.punish.PunishMessageBroadcaster;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProxyPunishMessageBroadcaster implements PunishMessageBroadcaster {

	public static ProxyPunishMessageBroadcaster newInstance() {
		return new ProxyPunishMessageBroadcaster();
	}

	@Override public void broadcastMessage(final @NonNull Punish punish, final boolean silent, final boolean superSilent) {
		if (superSilent) {
			return;
		}

		if (silent) {
			for (final ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {


			}

			return;
		}

	}
}
