package org.mineacademy.punishcontrol.proxy.command.punishcontrol;

import lombok.Getter;
import lombok.NonNull;
import net.md_5.bungee.api.CommandSender;
import org.mineacademy.bfo.collection.StrictList;
import org.mineacademy.bfo.command.SimpleCommand;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.punish.PunishDuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/** Command to handle (Un) banning, muting, warning, reporting & kicking players */
/*
TODO: Put in core & work with type parameters
 */
@Getter
public abstract class AbstractPunishCommand extends SimpleCommand {
  public static final String INVALID_SILENCE_USAGE =
      "§cCan't be silent and super-silent simultaneously";
  public static final String UNKNOWN_PLAYER = "§cThis player is not known";
  private static final List<AbstractPunishCommand> REGISTERED_COMMANDS = new ArrayList<>();
  private final int maxArgs;
  private boolean silent;
  private boolean superSilent;

  protected AbstractPunishCommand(@NonNull final String... labels) {
    this(3, labels);
  }

  protected AbstractPunishCommand(final int maxArgs, @NonNull final String... labels) {
    super(new StrictList<>(labels));
    this.maxArgs = maxArgs;
  }

  // ----------------------------------------------------------------------------------------------------
  // Methods to override
  // ----------------------------------------------------------------------------------------------------

  protected void onCase2(@NonNull final CommandSender sender, @NonNull final UUID target) {}

  protected void onCase3(
      @NonNull final CommandSender sender,
      @NonNull final UUID target,
      @NonNull final PunishDuration punishDuration) {}

  protected void onCase4(
      @NonNull final CommandSender player,
      @NonNull final UUID target,
      @NonNull final PunishDuration punishDuration,
      @NonNull final String reason) {}

  @Override
  protected final void onCommand() {

    this.silent = checkSilent();
    this.superSilent = checkSuperSilent();
    this.superSilent = checkSuperSilent();

    if (isSilent() && isSuperSilent()) {
      returnTell(INVALID_SILENCE_USAGE);
    }

    final List<String> finalArgs = new ArrayList<>(Arrays.asList(args));
    finalArgs.removeAll(Arrays.asList("-S", "-s", "-silent", "-super-slient"));

    switch (finalArgs.size()) {
      case 0:
        if (!isPlayer()) {
          returnTell(
              "Console needs to provide more information",
              "To run this command,",
              "please provide at least 2 arguments.");
        }
        break;
      case 1:
        onCase2(getSender(), findTarget());
        break;
      case 2:
        if (getMaxArgs() < 2) {
          returnInvalidArgs();
        }
        onCase3(getSender(), findTarget(), PunishDuration.of(finalArgs.get(0)));
        break;
      case 3:
        if (getMaxArgs() < 3) {
          returnInvalidArgs();
        }
        onCase4(getSender(), findTarget(), PunishDuration.of(finalArgs.get(1)), finalArgs.get(2));
        break;

      default:
        returnInvalidArgs();
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
      // TODO Rework
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
