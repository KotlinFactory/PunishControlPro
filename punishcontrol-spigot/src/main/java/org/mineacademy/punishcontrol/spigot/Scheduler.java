package org.mineacademy.punishcontrol.spigot;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.mineacademy.fo.plugin.SimplePlugin;

@UtilityClass
public class Scheduler {

  public void runAsync(final Runnable runnable) {
    Bukkit.getScheduler()
        .runTaskAsynchronously(SimplePlugin.getInstance(), runnable);
  }

  public boolean isPrimaryThread() {
    return Thread.currentThread().getName().equalsIgnoreCase("Main");
  }

  public void runSync(final Runnable runnable) {
    Bukkit.getScheduler().runTask(SimplePlugin.getInstance(), runnable);
  }

  public void runAsyncLater(final long delay, final Runnable runnable) {
    Bukkit.getScheduler()
        .runTaskLaterAsynchronously(SimplePlugin.getInstance(), runnable,
            delay);
  }

  public void runAsyncLater(final int delay, final Runnable runnable) {
    runAsyncLater((long) delay, runnable);
  }

  public void runLater(final int delay, final Runnable runnable) {
    Bukkit.getScheduler()
        .runTaskLater(SimplePlugin.getInstance(), runnable, delay);
  }

  public BukkitTask runTaskTimer(final long delay, final long period,
      final Runnable runnable) {
    return Bukkit.getScheduler()
        .runTaskTimer(SimplePlugin.getInstance(), runnable, delay, period);
  }

  public BukkitTask runTaskTimerAsync(final int delay, final int period,
      final Runnable runnable) {
    return Bukkit.getScheduler()
        .runTaskTimerAsynchronously(SimplePlugin.getInstance(), runnable,
            delay, period);
  }
}