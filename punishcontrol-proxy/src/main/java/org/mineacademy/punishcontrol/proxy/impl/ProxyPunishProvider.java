package org.mineacademy.punishcontrol.proxy.impl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.mineacademy.punishcontrol.core.punish.Punish;
import org.mineacademy.punishcontrol.core.punish.PunishProvider;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProxyPunishProvider implements PunishProvider {

	public static ProxyPunishProvider newInstance() {
		return new ProxyPunishProvider();
	}

	@Override public void broadCastPunishMessage(final @NonNull Punish punish, final boolean silent, final boolean superSilent) {
//		ProxyServer.getInstance().getPluginManager().callEvent()
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
