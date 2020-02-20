package org.mineacademy.punishcontrol.spigot.impl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mineacademy.punishcontrol.core.punish.Punish;
import org.mineacademy.punishcontrol.core.punish.PunishMessageBroadcaster;
import org.mineacademy.punishcontrol.spigot.settings.Settings;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SpigotPunishMessageBroadcaster implements PunishMessageBroadcaster {

	public static SpigotPunishMessageBroadcaster newInstance() {
		return new SpigotPunishMessageBroadcaster();
	}

	@Override public void broadcastMessage(@NonNull final Punish punish, final boolean silent, final boolean superSilent) {

		//No one will be notified
		if (superSilent) {
			return;
		}

		if (silent) {

			if (!Settings.Notifications.SilentPunish.enabled) {
				return;
			}

			for (final Player player : Bukkit.getOnlinePlayers()) {

				//The player {} has been banned by
				if (!player.hasPermission(Settings.Notifications.SilentPunish.permission)) {
					continue;
				}

				//Sending message
			}

			return;
		}

		if (!Settings.Notifications.Punish.enabled) {
			return;
		}

		for (final Player player : Bukkit.getOnlinePlayers()) {

			if (!player.hasPermission(Settings.Notifications.Punish.permission)) {
				continue;
			}

			//Sending message
		}
	}
}
