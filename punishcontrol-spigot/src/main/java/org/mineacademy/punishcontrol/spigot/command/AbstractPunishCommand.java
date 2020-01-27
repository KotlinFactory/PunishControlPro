package org.mineacademy.punishcontrol.spigot.command;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mineacademy.fo.collection.StrictList;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.punish.PunishDuration;
import org.mineacademy.punishcontrol.spigot.gui.MenuPlayerBrowser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Command to handle (Un) banning, muting, warning, reporting & kicking players
 */
/*
TODO: Put in core & work with type parameters
 */
@Getter
public abstract class AbstractPunishCommand extends SimpleCommand {
	public boolean consoleAllowed = true;
	private static final List<AbstractPunishCommand> REGISTERED_COMMANDS = new ArrayList<>();
	public static final String INVALID_SILENCE_USAGE = "§cCan't be silent and super-silent simultaneously";
	public static final String UNKNOWN_PLAYER = "§cThis player is not known";

	private final int maxArgs;
	private boolean silent;
	private boolean superSilent;

	protected AbstractPunishCommand(@NonNull final String... labels) {
		this(3, labels);
	}

	protected AbstractPunishCommand(final int maxArgs, @NonNull final String... labels) {
		super(new StrictList<>(labels));
		this.maxArgs = maxArgs;
		addTellPrefix(true);
	}

	// ----------------------------------------------------------------------------------------------------
	// Methods to override
	// ----------------------------------------------------------------------------------------------------
	//

	//Ex {command} [player] -> Can only be run as a player
	protected void onTargetProvided(@NonNull final Player player, @NonNull final UUID target) {

	}

	//Ex {command} [player] [] -> Can only be run as a player
	protected void onTargetAndDurationProvided(@NonNull final Player player, @NonNull final UUID target, @NonNull final String reason) {

	}


	//Ex {command} [player] [] -> Can only be run as a player
	protected void onTargetAndDurationAndReasonProvided(@NonNull final CommandSender player,
	                                                    @NonNull final UUID target,
	                                                    @NonNull final PunishDuration punishDuration,
	                                                    @NonNull final String reason) {
	}

	//Is the reason provided valid (For reports for example)? If not, use returnTell to break up the command
	protected void handleReasonInput(@NonNull final String reason) {
	}

	@Override
	protected final void onCommand() {

		//Checking the console if needed.
		if (!isConsoleAllowed()) {
			checkConsole();
		}

		this.silent = checkSilent();
		this.superSilent = checkSuperSilent();


		if (isSilent() && isSuperSilent()) {
			returnTell(INVALID_SILENCE_USAGE);
		}

		final List<String> finalArgs = new ArrayList<>(Arrays.asList(args));
		finalArgs.removeAll(Arrays.asList("-S", "-s", "-silent", "-super-slient"));

		//Declaring the reason. Instantiation below
		final String reason;

		switch (finalArgs.size()) {
			case 0:
				if (!isPlayer()) {

					returnTell("You need to provide more information to run this command from console", "Please provide 3 arguments", "Usage: " + getUsage());
				}
				MenuPlayerBrowser.showTo(getPlayer());
				break;
			case 1:

				if (!isPlayer()) {
					returnTell("You need to provide more information to run this command from console", "Please provide 3 arguments", "Usage: " + getUsage());
				}
				onTargetProvided(getPlayer(), findTarget());
				break;
			case 2:
				//ban NAME DURATION
				if (getMaxArgs() < 2) {
					returnInvalidArgs();
				}

				if (!isPlayer()) {
					returnTell("You need to provide more information to run this command from console", "Please provide 3 arguments", "Usage: " + getUsage());
				}

				reason = finalArgs.get(1);

				//Validating the reason
				handleReasonInput(reason);

				onTargetAndDurationProvided(getPlayer(), findTarget(), reason);
				break;
			case 3:
				if (getMaxArgs() < 3) {
					returnInvalidArgs();
				}

				reason = finalArgs.get(1);

				//Validating the reason
				handleReasonInput(reason);

				//ban NAME REASON, DURATION
				onTargetAndDurationAndReasonProvided(getSender(), findTarget(), PunishDuration.of(finalArgs.get(2)), reason);
				break;
		}
	}

	// ----------------------------------------------------------------------------------------------------
	// Internal  helper-methods
	// ----------------------------------------------------------------------------------------------------

	private boolean checkSilent() {
		for (final String arg : args) {
			if (arg.equals("-s") || arg.equals("-silent")) {
				return true;
			}
		}
		return false;
	}

	private boolean checkSuperSilent() {
		for (final String arg : args) {
			//TODO Rework
			if (arg.equals("-S") || arg.equals("-Super-Silent")) {
				return true;
			}
		}
		return false;
	}

	private UUID findTarget() {
		final UUID target = Providers.playerProvider().getUUID(args[0]);
		checkNotNull(target, UNKNOWN_PLAYER);
		return target;
	}
}
