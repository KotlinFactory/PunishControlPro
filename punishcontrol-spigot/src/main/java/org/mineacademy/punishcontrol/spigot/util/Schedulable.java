package org.mineacademy.punishcontrol.spigot.util;

import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.plugin.SimplePlugin;

public interface Schedulable {

  default void sync(final Runnable runnable) {
    Bukkit.getScheduler().runTask(SimplePlugin.getInstance(), runnable);
  }

  default void async(final Runnable runnable) {
    Bukkit.getScheduler()
        .runTaskAsynchronously(SimplePlugin.getInstance(), runnable);
  }

  default void later(final Runnable runnable, final long ticks) {
    Bukkit.getScheduler()
        .runTaskLater(SimplePlugin.getInstance(), runnable, ticks);
  }

  default void laterAsync(final Runnable runnable, final long ticks) {
    Bukkit.getScheduler()
        .runTaskLaterAsynchronously(SimplePlugin.getInstance(), runnable,
            ticks);
  }

  default void repeat(
      final Consumer<Integer> runnable, final long tickPeriod,
      final int times) {
    repeat(runnable, tickPeriod, tickPeriod, times);
  }

  default void repeat(
      final Consumer<Integer> runnable, final long delay,
      final long tickPeriod, final int times) {
    new BukkitRunnable() {
      private int count;

      @Override
      public void run() {
        count++;
        if (count == times) {
          cancel();
        }
        runnable.accept(count);
      }
    }.runTaskTimer(SimplePlugin.getInstance(), delay, tickPeriod);
  }

  default void repeatAsync(
      final Runnable runnable,
      final int delay,
      final int tickPeriod) {
    new BukkitRunnable() {
      private int count;

      @Override
      public void run() {
        runnable.run();
      }
    }.runTaskTimerAsynchronously(SimplePlugin.getInstance(), delay, tickPeriod);
  }

  default void repeatAsync(
      final Consumer<Integer> runnable,
      final long tickPeriod, final int times) {
    repeatAsync(runnable, tickPeriod, tickPeriod, times);
  }

  default void repeatAsync(
      final Consumer<Integer> runnable, final long delay,
      final long tickPeriod, final int times) {
    new BukkitRunnable() {
      private int count;

      @Override
      public void run() {
        count++;
        if (count == times) {
          cancel();
        }
        runnable.accept(count);
      }
    }.runTaskTimerAsynchronously(SimplePlugin.getInstance(), delay, tickPeriod);
  }

}