package org.mineacademy.punishcontrol.spigot.commands;

import org.mineacademy.fo.collection.StrictList;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.punish.Punish;
import org.mineacademy.punishcontrol.core.storage.PlayerCache;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;
import org.mineacademy.punishcontrol.spigot.settings.Localization;

import javax.inject.Inject;
import java.util.UUID;

public class PlayerInfoCommand extends SimpleCommand {

  private final PlayerProvider playerProvider;
  private final StorageProvider storageProvider;

  @Inject
  public PlayerInfoCommand(
      final PlayerProvider playerProvider, final StorageProvider storageProvider) {
    super(new StrictList<>("pi", "playerinfo"));
    this.playerProvider = playerProvider;
    this.storageProvider = storageProvider;
    setMinArguments(1);
    setUsage("[player]");
  }

  @Override
  protected void onCommand() {

    final UUID target = parseUUID(args[0]);

    final PlayerCache playerCache = storageProvider.getCacheFor(target);

    playerCache.listPunishes().stream().filter(Punish::isOld).onClose(System.out::println);
    // Send info here
  }

  private UUID parseUUID(final String nameOrUUID) {

    if (nameOrUUID.length() == 36) {
      return UUID.fromString(nameOrUUID);
    }

    if (nameOrUUID.length() <= 16) {
      return playerProvider.getUUID(nameOrUUID);
    }

    // Invalid String
    returnTell(Localization.DATA_MISSING);
    // Will actually never be called;
    // The Java compiler just can't infer the exception thrown in returnTell()
    return UUID.randomUUID();
  }
}
