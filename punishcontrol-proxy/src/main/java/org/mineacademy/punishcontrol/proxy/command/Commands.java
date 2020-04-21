package org.mineacademy.punishcontrol.proxy.command;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Commands {

//  @SneakyThrows
//  public void registerCommand(@NonNull final Command command) {
//    ProxyServer.getInstance()
//        .getPluginManager()
//        .registerCommand(Providers.get(Plugin.class), command);
//  }
//
//  public void removeCommands(final String... cmds) {
//    Arrays
//        .stream(cmds)
//        .map(Commands::getCommand)
//        .filter(Objects::nonNull)
//        .forEach(
//            command -> ProxyServer.getInstance().getPluginManager().unregisterCommand(command));
//  }
//
//  public boolean isRegistered(final String command) {
//    return getCommand(command) != null;
//  }
//
//  @SneakyThrows
//  private void removeCommand(final String command) {
//    final Command cmd = getCommand(command);
//    if (cmd == null) {
//      return;
//    }
//    ProxyServer.getInstance().getPluginManager().unregisterCommand(cmd);
//  }
//
//  private Command getCommand(final String name) {
//    Command out = null;
//    for (final val entry : ProxyServer.getInstance().getPluginManager().getCommands()) {
//      if (entry.getValue().getName().equalsIgnoreCase(name)) {
//        out = entry.getValue();
//      }
//    }
//    return out;
//  }
}
