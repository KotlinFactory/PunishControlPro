package org.mineacademy.punishcontrol.spigot.command;

import org.mineacademy.fo.collection.StrictList;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.punishcontrol.core.provider.Providers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class AbstractSimplePunishControlCommand extends SimpleCommand {
    public static final List<AbstractSimplePunishControlCommand> REGISTERED_COMMANDS = new ArrayList<>();

    public static final String INVALID_SILENCE_USAGE =
        "§cCan't be silent and super-silent simultaneously";
    public static final String UNKNOWN_PLAYER = "§cThis player is not known";

    protected final String[] MORE_ARGUMENTS_AS_CONSOLE_MESSAGE =
        new String[] {
            "You need to provide more information to run this command from console",
            "Please provide 3 arguments",
            "Usage: " + getUsage()
        };

    public boolean consoleAllowed = true;
    protected boolean silent;
    protected boolean superSilent;

  protected AbstractSimplePunishControlCommand(final String label) {
    super(label);
  }

  protected AbstractSimplePunishControlCommand(final StrictList<String> labels) {
    super(labels);
  }

  protected AbstractSimplePunishControlCommand(final String label, final List<String> aliases) {
    super(label, aliases);
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

  protected UUID findTarget(final List<String> args) {
    final UUID target = Providers.playerProvider().getUUID(args.get(0));
    checkNotNull(target, UNKNOWN_PLAYER);
    return target;
  }
}
