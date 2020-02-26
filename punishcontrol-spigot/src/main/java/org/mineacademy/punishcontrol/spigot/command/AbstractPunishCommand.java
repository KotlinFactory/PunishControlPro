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
import org.mineacademy.punishcontrol.spigot.settings.Settings;

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
	public static final List<AbstractPunishCommand> REGISTERED_COMMANDS = new ArrayList<>();

	public static final String INVALID_SILENCE_USAGE = "§cCan't be silent and super-silent simultaneously";
	public static final String UNKNOWN_PLAYER = "§cThis player is not known";


	private final int MIN_ARGS_FOR_CONSOLE = 3;
	public boolean consoleAllowed = true;
	private final String[] MORE_ARGUMENTS_AS_CONSOLE_MESSAGE = new String[]{
		"You need to provide more information to run this command from console", "Please provide 3 arguments", "Usage: " + getUsage()
	};

	private final int maxArgs;
	private boolean silent;
	private boolean superSilent;

	protected AbstractPunishCommand(@NonNull final String... labels) {
		this(3, labels);
	}

	protected AbstractPunishCommand(final int maxArgs, @NonNull final String... labels) {
		super(new StrictList<>(labels));
		this.maxArgs = maxArgs;
		setTellPrefix(Settings.PLUGIN_PREFIX);
		addTellPrefix(true);
		REGISTERED_COMMANDS.add(this);

	}

	// ----------------------------------------------------------------------------------------------------
	// Methods to override
	// ----------------------------------------------------------------------------------------------------

	//Ex {command} [player] -> Can only be run as a player
	protected void onTargetProvided(@NonNull final Player player, @NonNull final UUID target) {

	}

	//Ex {command} [player] [] -> Can only be run as a player
	protected void onTargetAndDurationProvided(@NonNull final Player player, @NonNull final UUID target, @NonNull final PunishDuration punishDuration) {

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

	/*
	ban linkskeinemitte 10d Hacking  //3
	report [-PARAM] linkskeinemitte test reason with spaces //
	kick [-PARAM] linkskeinemitte
	kick [-PARAM] linkskeinemitte hacking
	warn [-PARAM] linkskeinemitte 10d hacking

	unban linkskeinemitte //1
	unmute linkskeinemitte //1
	unwarn linkskeinemitte //1
	 */

	@Override protected final void onCommand() {

		//Checking the console if needed.
		if (!isConsoleAllowed()) {
			checkConsole();
		}

		this.silent = checkSilent();
		this.superSilent = checkSuperSilent();

		if (isSilent() && isSuperSilent()) {
			returnTell(INVALID_SILENCE_USAGE);
		}

		//Getting our arguments (skipping flags like '-S') & Setting up our reason/punishduration if found.
		final StringBuilder reason = new StringBuilder();
		PunishDuration punishDuration = null;

		final List<String> finalArgs = new ArrayList<>(Arrays.asList(args));
		//Args without params
		finalArgs.removeAll(Arrays.asList("-S", "-s", "-silent", "-super-slient"));

		for (final String arg : finalArgs) {
			if (finalArgs.indexOf(arg) == 0) {
				continue;
			}

			if (finalArgs.indexOf(arg) == 1) {
				punishDuration = PunishDuration.of(arg);
			} else {
				reason.append(arg).append(" ");
			}
		}

		final int size = Math.min(finalArgs.size(), 3);

		switch (size) {
			case 0: //Noting
				if (!isPlayer()) {
					returnTell(MORE_ARGUMENTS_AS_CONSOLE_MESSAGE);
				}
				MenuPlayerBrowser.showTo(getPlayer());
				break;
			case 1: //Target

				if (!isPlayer()) {
					returnTell(MORE_ARGUMENTS_AS_CONSOLE_MESSAGE);
				}
				onTargetProvided(getPlayer(), findTarget(finalArgs));
				break;
			case 2: //Target, Duration
				//ban NAME DURATION
				if (getMaxArgs() < 2) {
					returnInvalidArgs();
				}

				if (!isPlayer()) {
					returnTell(MORE_ARGUMENTS_AS_CONSOLE_MESSAGE);
				}

				onTargetAndDurationProvided(getPlayer(), findTarget(finalArgs), punishDuration);
				break;
			case 3:  //Target, Duration, Reason
				if (getMaxArgs() < 3) {
					returnInvalidArgs();
				}

				//Validating the reason
				handleReasonInput(reason.toString());

				//ban NAME REASON, DURATION
				onTargetAndDurationAndReasonProvided(getSender(), findTarget(finalArgs), punishDuration, reason.toString());
				break;
		}

		//Declaring the reason. Instantiation below

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

	private UUID findTarget(final List<String> args) {
		final UUID target = Providers.playerProvider().getUUID(args.get(0));
		checkNotNull(target, UNKNOWN_PLAYER);
		return target;
	}
}
