package org.mineacademy.punishcontrol.spigot.command;

import lombok.NonNull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
	protected void onTargetProvided(final Player player, final @NonNull UUID target) {
	}

	@Override
	protected void onTargetAndDurationProvided(final Player player, final @NonNull UUID target, final String reason) {
	}

	@Override
	protected void onTargetAndDurationAndReasonProvided(final @NonNull CommandSender player, final @NonNull UUID target, final @NonNull PunishDuration punishDuration, final @NonNull String reason) {
	}
}
