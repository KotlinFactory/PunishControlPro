package org.mineacademy.punishcontrol.spigot.gui;

import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.MenuPagged;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.punishcontrol.core.provider.Providers;

import java.util.List;
import java.util.UUID;

public final class PlayerBrowser extends MenuPagged<UUID> {

	public static void showTo(@NonNull final Player player) {
		showTo(player, true);
	}

	public static void showTo(@NonNull final Player player, final boolean offlinePlayer) {
		//Handling stuff
		of(offlinePlayer).displayTo(player);
	}

	static MenuPagged<UUID> of(final boolean offlinePlayer) {
		//Handling stuff
		if (offlinePlayer) {
			final List<UUID> offlinePlayers = Providers.playerProvider().getOfflinePlayers();
			return new PlayerBrowser(getSizeForPlayers(offlinePlayers.size()), new PunishControlGUI(), offlinePlayers);
		} else {
			final List<UUID> onlinePlayers = Providers.playerProvider().getOnlinePlayers();
			return new PlayerBrowser(getSizeForPlayers(onlinePlayers.size()), new PunishControlGUI(), onlinePlayers);
		}
	}

	protected static int getSizeForPlayers(final int playersOnline) {
		return playersOnline / 9 == 0 ? 9 : (playersOnline / 9) * 9;
	}

	private PlayerBrowser(final int pageSize, final Menu parent, final Iterable<UUID> pages) {
		super(pageSize, parent, pages);
		setTitle("§3Player-Browser");
	}

	@Override
	protected boolean addPageNumbers() {
		return true;
	}

	@Override
	protected ItemStack convertToItemStack(final UUID uuid) {
		final String name = Providers.playerProvider().getName(uuid);
		return ItemCreator
				.of(CompMaterial.PLAYER_HEAD)
				.glow(uuid.equals(getViewer().getUniqueId()))
				.name("§3" + name)
				.skullOwner(name)
				.lore("Click here for more info //Change-me")
				.build()
				.make();
	}

	@Override
	protected void onPageClick(final Player player, final UUID uuid, final ClickType clickType) {
		player.closeInventory();
		ChooseActionGUI.showTo(player);
	}

	@Override
	protected String[] getInfo() {
		return new String[0];
	}
}
