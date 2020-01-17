package org.mineacademy.punishcontrol.spigot.command;

import lombok.NonNull;
import org.bukkit.command.CommandSender;
import org.mineacademy.punishcontrol.core.punish.PunishDuration;

import java.util.UUID;

public final class CommandBan extends AbstractPunishCommand {

	public static CommandBan newInstance() {
		return new CommandBan();
	}

	private CommandBan() {
		super("ban");
	}

	@Override
	protected void onCase2(final @NonNull CommandSender sender, final @NonNull UUID target) {
	}

	@Override
	protected void onCase3(final @NonNull CommandSender sender, final @NonNull UUID target, final @NonNull PunishDuration punishDuration) {
	}

	@Override
	protected void onCase4(final @NonNull CommandSender player, final @NonNull UUID target, final @NonNull PunishDuration punishDuration, final @NonNull String reason) {
	}
}
