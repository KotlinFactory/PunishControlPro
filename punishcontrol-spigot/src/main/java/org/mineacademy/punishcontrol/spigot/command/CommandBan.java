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
	protected void onTargetProvided(@NonNull final Player player, final @NonNull UUID target) {
	}

	@Override
	protected void onTargetAndDurationProvided(@NonNull final Player player,
	                                           @NonNull final UUID target,
	                                           @NonNull final PunishDuration punishDuration) {
	}

	@Override
	protected void onTargetAndDurationAndReasonProvided(@NonNull final CommandSender player,
	                                                    @NonNull final UUID target,
	                                                    @NonNull final PunishDuration punishDuration,
	                                                    @NonNull final String reason) {
	}
}
