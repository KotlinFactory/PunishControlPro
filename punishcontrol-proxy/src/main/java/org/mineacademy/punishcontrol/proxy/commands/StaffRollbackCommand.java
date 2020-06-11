package org.mineacademy.punishcontrol.proxy.commands;

import de.exceptionflug.protocolize.items.ItemType;
import de.leonhard.storage.util.Valid;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.inject.Inject;
import lombok.NonNull;
import lombok.val;
import org.mineacademy.bfo.collection.StrictList;
import org.mineacademy.bfo.collection.expiringmap.ExpiringMap;
import org.mineacademy.bfo.model.SimpleComponent;
import org.mineacademy.punishcontrol.core.notification.Notification;
import org.mineacademy.punishcontrol.core.notification.Notifications;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;
import org.mineacademy.punishcontrol.core.punish.Punish;
import org.mineacademy.punishcontrol.core.punish.PunishDuration;
import org.mineacademy.punishcontrol.core.storage.StorageProvider;
import org.mineacademy.punishcontrol.proxy.command.AbstractSimplePunishControlCommand;

public final class StaffRollbackCommand extends AbstractSimplePunishControlCommand {

  private static final Random random = new Random();
  private static final ExpiringMap<Integer, List<Punish>> applyQueue = ExpiringMap
      .builder()
      .expiration(30L, TimeUnit.MINUTES)
      .build();
  private final StorageProvider storageProvider;


  @Inject
  public StaffRollbackCommand(
      @NonNull final PlayerProvider playerProvider,
      @NonNull final StorageProvider storageProvider) {
    super(playerProvider, new StrictList<>("staffrollback", "staffr", "stfr"));
    this.storageProvider = storageProvider;
    setPermission("punishcontrol.command.playerinfo");
    setUsage("<player> <time>");
    setDescription("Rollback punishments");
  }

  @Override
  protected void onCommand() {

    if (args.length < 1) {
      returnTell("&c" + getUsage());
    }

    async(() -> {

      // Is confirmation code

      final String data = args[0];

      if (args.length == 2) {

        if (applyQueue.size() > 5000) {
          tell("&7Queue contains to many values. Clearing...");
          applyQueue.clear();
        }

        final UUID target = findTarget(data);
        final PunishDuration punishDuration = PunishDuration
            .of(args[1]);

        //PunishDuration mustn't be empty: If its empty the given string had the wrong format
        checkBoolean(!punishDuration.isEmpty(),
            "&cInvalid time format! Example: 10days");

        final List<Punish> punishmentsToRemove = storageProvider.listPunishes()
            .stream()
            .filter(punish -> punish.creator().equals(target)).filter(punish -> ((
                // Time elapsed since creation must be greater than our duration
                System.currentTimeMillis() - punish.creation())) < punishDuration.toMs())
            .collect(Collectors.toList());

        if (punishmentsToRemove.size() < 1) {
          tell(
              "&cThis wouldn't remove any punishments!",
              "&cTry a higher duration");
          return;
        }

        // Number to type in to confirm
        int confirmCode;
        do {
          confirmCode = nextBetween(1000, 9999);
          // Code mustn't be known
        } while (applyQueue.containsKey(confirmCode));

        applyQueue.put(confirmCode, punishmentsToRemove);

        tell("&7This action would remove " + punishmentsToRemove.size() + " "
            + "&7punishments");

        SimpleComponent
            .of("&7Please use")
            .append(" ")
            .append("&7/" + getLabel() + " " + confirmCode)
            .onHover("&7Click to copy")
            .onClickSuggestCmd("/" + getLabel() + " " + confirmCode)
            .append(" ")
            .append("&7to apply")

            .send(sender);

        tell("&7This action can not be undone!");

        return;
      }

      if (args.length == 1) {
        if (!isNumeric(data)) {
          tell("&c/" + getLabel() + " " + getUsage());
          return;
        }

        final Integer number = Integer.parseInt(data);
        val raw = applyQueue.get(number);
        if (raw == null) {
          tell("&cInvalid confirmation code!");
          return;
        }

        for (Punish punish : raw) {
          storageProvider.removePunish(punish);
        }

        applyQueue.remove(number);
        tell("&aSuccessfully removed &6" + raw.size() + " &apunishments");

        Notifications.register(
            Notification
                .of("&3Rollback!")
                .text(
                    "",
                    sender.getName() + " removed " + raw.size() + " punishments",
                    ""
                )
                .itemType(ItemType.BARRIER)
        );
      }
    });
  }

  private static int nextBetween(final int min, final int max) {
    Valid.checkBoolean(min <= max, "Min !< max");

    return min + nextInt(max - min + 1);
  }

  /**
   * Returns a random integer, see {@link Random#nextInt(int)}
   *
   * @param boundExclusive
   * @return
   */
  private static int nextInt(final int boundExclusive) {
    return random.nextInt(boundExclusive);
  }

  private static boolean isNumeric(String str) {
    if (str == null) {
      return false;
    }

    int sz = str.length();

    for (int i = 0; i < sz; i++) {
      if (!Character.isDigit(str.charAt(i))) {
        return false;
      }
    }

    return true;
  }
}
