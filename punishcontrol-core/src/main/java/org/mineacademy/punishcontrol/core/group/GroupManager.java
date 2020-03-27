package org.mineacademy.punishcontrol.core.group;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.mineacademy.punishcontrol.core.provider.Providers;
import org.mineacademy.punishcontrol.core.providers.PlayerProvider;

import java.util.*;

@UtilityClass
public class GroupManager {
  private final PlayerProvider provider = Providers.playerProvider();
  private final List<Group> registeredGroups = new ArrayList<>();

  public void registerGroup(@NonNull final Group group) {
    registeredGroups.add(group);
  }

  /**
   * @param target UUID to find the group of
   * @return The group of the highest priority the player according to the UUID has
   */
  public Optional<Group> primaryGroupOf(@NonNull final UUID target) {
    Group lastResult = null;

    for (final Group group : registeredGroups) {

      // Does the player has the permissions he need
      if (!provider.hasPermission(target, group.permission())) {
        continue;
      }

      // Has the last result a higher priority than the current group?
      if (lastResult != null && lastResult.priority() >= group.priority()) {
        continue;
      }

      lastResult = group;
    }

    return Optional.ofNullable(lastResult);
  }

  // Returns the name of the registered groups
  public List<String> getGroupNames() {

    final List<String> result = new ArrayList<>();

    for (final Group group : registeredGroups) {
      result.add(group.name());
    }

    return result;
  }

  public List<Group> registeredGroups() {
    // We don't want anybody else to change our list:)
    return Collections.unmodifiableList(registeredGroups);
  }
}
