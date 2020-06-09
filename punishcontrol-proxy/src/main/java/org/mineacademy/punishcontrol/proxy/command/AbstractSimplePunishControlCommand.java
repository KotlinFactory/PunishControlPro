package org.mineacademy.punishcontrol.proxy.command;

import de.exceptionflug.mccommons.inventories.proxy.utils.Schedulable;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import lombok.val;
import org.mineacademy.bfo.collection.StrictList;
import org.mineacademy.bfo.command.SimpleCommand;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;

public abstract class AbstractSimplePunishControlCommand
    extends SimpleCommand
    implements Schedulable {

  public static final String INVALID_SILENCE_USAGE =
      "§cCan't be silent and super-silent simultaneously";
  public static final String UNKNOWN_PLAYER = "§cThis player is not known";

  protected final String[] MORE_ARGUMENTS_AS_CONSOLE_MESSAGE =
      new String[]{
          "You need to provide more information to run this command from console",
          "Please provide 3 arguments",
          "Usage: " + getLabel() + " [duration] [reason]",
          "Or: " + getLabel() + " <template>"
      };

  protected final PlayerProvider playerProvider;

  public boolean consoleAllowed = true;
  protected boolean silent;
  protected boolean superSilent;

  protected AbstractSimplePunishControlCommand(
      @NonNull final PlayerProvider playerProvider,
      @NonNull final String label) {
    super(label);
    this.playerProvider = playerProvider;
    addTellPrefix(false);
  }

  protected AbstractSimplePunishControlCommand(
      @NonNull final PlayerProvider playerProvider,
      @NonNull final StrictList<String> labels) {
    super(labels);
    this.playerProvider = playerProvider;
  }

  // ----------------------------------------------------------------------------------------------------
  // Helper methods for our specific plugin
  // ----------------------------------------------------------------------------------------------------

  protected boolean checkSilent() {
    for (final String arg : args) {
      if (arg.equals("-s") || arg.equals("-silent")) {
        return true;
      }
    }
    return false;
  }

  protected boolean checkSuperSilent() {
    for (final String arg : args) {
      // TODO Rework
      if (arg.equals("-S") || arg.equals("-Super-Silent")) {
        return true;
      }
    }
    return false;
  }


  protected UUID findTarget() {
    return findTarget(Arrays.asList(args));
  }

  protected UUID findTarget(final List<String> args) {
    final String name = args.get(0);

    return findTarget(name);
  }

  protected UUID findTarget(final String name) {
    //UUID!
    if (name.length() == 36) {
      try {
        return UUID.fromString(name);
      } catch (final Throwable throwable) {
        returnTell("UUID-String is malformatted!");
      }
    }

    final val optionalTarget = Providers.playerProvider().findUUID(name);
//    checkNotNull(target, UNKNOWN_PLAYER);
    //
    checkBoolean(optionalTarget.isPresent(), UNKNOWN_PLAYER);
    if (!optionalTarget.isPresent()) {
      throw new Error();
    }
    return optionalTarget.get();
  }
}
