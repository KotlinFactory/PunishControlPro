package org.mineacademy.punishcontrol.spigot.command;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mineacademy.fo.collection.StrictList;
import org.mineacademy.punishcontrol.core.punish.PunishDuration;
import org.mineacademy.punishcontrol.spigot.menus.MenuPlayerBrowser;
import org.mineacademy.punishcontrol.spigot.settings.Settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/** Command to handle (Un) banning, muting, warning, reporting & kicking players */
/*
TODO: Put in core & work with type parameters
 */

@Getter
public abstract class AbstractPunishCommand extends AbstractSimplePunishControlCommand {
  private final int maxArgs;

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

  // Ex {command} [player] -> Can only be run as a player
  protected void onTargetProvided(@NonNull final Player player, @NonNull final UUID target) {}

  // Ex {command} [player] [] -> Can only be run as a player
  protected void onTargetAndDurationProvided(
      @NonNull final Player player,
      @NonNull final UUID target,
      @NonNull final PunishDuration punishDuration) {}

  // Ex {command} [player] [] -> Can only be run as a player
  protected void onTargetAndDurationAndReasonProvided(
      @NonNull final CommandSender player,
      @NonNull final UUID target,
      @NonNull final PunishDuration punishDuration,
      @NonNull final String reason) {}

  // Is the reason provided valid (For reports for example)? If not, use returnTell to break up the
  // command
  protected void handleReasonInput(@NonNull final String reason) {}

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

  @Override
  protected final void onCommand() {

    // Checking the console if needed.
    if (!consoleAllowed) {
      checkConsole();
    }

    silent = checkSilent();
    superSilent = checkSuperSilent();

    if (silent && superSilent) {
      returnTell(INVALID_SILENCE_USAGE);
    }

    // Getting our arguments (skipping flags like '-S') & Setting up our reason/punishduration if
    // found.
    final StringBuilder reason = new StringBuilder();
    PunishDuration punishDuration = null;

    final List<String> finalArgs = new ArrayList<>(Arrays.asList(args));
    // Args without params
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
      case 0: // Noting
        if (!isPlayer()) {
          returnTell(MORE_ARGUMENTS_AS_CONSOLE_MESSAGE);
        }
        MenuPlayerBrowser.showTo(getPlayer());
        break;
      case 1: // Target
        if (!isPlayer()) {
          returnTell(MORE_ARGUMENTS_AS_CONSOLE_MESSAGE);
        }
        onTargetProvided(getPlayer(), findTarget(finalArgs));
        break;
      case 2: // Target, Duration
        // ban NAME DURATION
        if (getMaxArgs() < 2) {
          returnInvalidArgs();
        }

        if (!isPlayer()) {
          returnTell(MORE_ARGUMENTS_AS_CONSOLE_MESSAGE);
        }

        onTargetAndDurationProvided(getPlayer(), findTarget(finalArgs), punishDuration);
        break;
      case 3: // Target, Duration, Reason
        if (getMaxArgs() < 3) {
          returnInvalidArgs();
        }

        // Validating the reason
        handleReasonInput(reason.toString());

        // ban NAME REASON, DURATION
        onTargetAndDurationAndReasonProvided(
            getSender(), findTarget(finalArgs), punishDuration, reason.toString());
        break;
    }

    // Declaring the reason. Instantiation below

  }
}
