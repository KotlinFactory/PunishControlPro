package org.mineacademy.punishcontrol.spigot.impl;

import de.leonhard.storage.Json;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.remain.Remain;
import org.mineacademy.punishcontrol.core.PunishControlManager;
import org.mineacademy.punishcontrol.core.provider.PlayerProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SpigotPlayerProvider extends Json implements PlayerProvider {
	public SpigotPlayerProvider() {
		super(PunishControlManager.FILES.UUID_STORAGE, SimplePlugin.getData().getAbsolutePath() + "/data/");
	}

	@Override
	public void saveUUIDAndName(@NonNull final UUID uuid, @NonNull final String s) {
		set(uuid.toString(), s);
	}

	@Override
	public List<UUID> getOfflinePlayers() {
		final List<UUID> result = new ArrayList<>();

		for (final OfflinePlayer player : Bukkit.getOfflinePlayers()) {
			result.add(player.getUniqueId());
		}

		return result;
	}

	@Override
	public List<UUID> getOnlinePlayers() {
		final List<UUID> result = new ArrayList<>();
		for (final Player player : Bukkit.getOnlinePlayers()) {
			result.add(player.getUniqueId());
		}
		return result;
	}

	@Override
	public boolean isOnline(@NonNull final UUID uuid) {
		return Bukkit.getPlayer(uuid) != null;
	}

	@Override
	public String getName(@NonNull final UUID uuid) {
		return Remain.getOfflinePlayerByUUID(uuid).getName();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}

	@Override
	public UUID getUUID(@NonNull final String name) {
		for (final OfflinePlayer player : Bukkit.getOfflinePlayers()) {
			if (player.getName() != null && player.getName().equals(name)) {
				return player.getUniqueId();
			}
		}

		return null;
	}

	@Override
	public String getIpIfOnline(@NonNull final UUID uuid) {
		final Player player = Bukkit.getPlayer(uuid);
		if (player == null || player.getAddress() == null) {
			return null;
		}
		return player.getAddress().getHostName();
	}

	@Override
	public void sendIfOnline(@NonNull final UUID uuid, final @NonNull String... messages) {
		final Player player = Bukkit.getPlayer(uuid);

		if (player == null) {
			return;
		}

		Common.tell(player, messages);
	}
}
