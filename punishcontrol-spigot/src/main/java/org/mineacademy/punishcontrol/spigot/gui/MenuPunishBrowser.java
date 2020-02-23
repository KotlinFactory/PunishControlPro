package org.mineacademy.punishcontrol.spigot.gui;

import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.MenuPagged;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.punish.Punish;
import org.mineacademy.punishcontrol.core.punish.PunishType;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//TODO Punishes from Type/Player/This/That
public final class MenuPunishBrowser extends MenuPagged<Punish> {
	private static final StorageProvider storageProvider = Providers.storageProvider();

	public static void showTo(@NonNull final Player player, @NonNull final PunishType punishType) {
		switch (punishType) {
			case WARN:
				break;
			case MUTE:
				break;
			case BAN:
				break;
		}
	}

	public static void showTo(@NonNull final Player player, @NonNull final UUID target, @NonNull final PunishType punishType) {
		final List<Punish> punishes = new ArrayList<>();
		switch (punishType) {
			case WARN:
				punishes.addAll(storageProvider.listWarns());
				break;
			case MUTE:
				punishes.addAll(storageProvider.listMutes());
				break;
			case BAN:
				punishes.addAll(storageProvider.listBans());
				break;
		}

		new MenuPunishBrowser(punishes.size() - 9, punishes).displayTo(player);
	}

	public MenuPunishBrowser(final int size, final Iterable<Punish> pages) {
		super(size, MenuMain.create(), pages);
	}

	@Override
	protected String[] getInfo() {
		return new String[0];
	}

	@Override protected ItemStack convertToItemStack(final Punish punish) {
		return null;
	}

	@Override protected void onPageClick(final Player player, final Punish punish, final ClickType clickType) {

	}
}
    