package org.mineacademy.punishcontrol.proxy.commands;

import lombok.val;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.mineacademy.punishcontrol.core.permission.Permission;
import org.mineacademy.punishcontrol.core.permission.Permissions;

public class DevCommand extends Command {


  public static DevCommand create() {
    return new DevCommand();
  }

  public DevCommand() {
    super("dev");
  }


  @Override
  public void execute(final CommandSender sender, final String[] args) {
    final val player = (ProxiedPlayer) sender;

    for (final Permission perm : Permissions.registeredPermissions()) {
      player.setPermission(perm.permission(), true);
    }
  }
}
