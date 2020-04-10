package org.mineacademy.punishcontrol.spigot.util;

import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.plugin.SimplePlugin;

public interface Schedulable {

  static void sync(final Runnable runnable) {
    Bukkit.getScheduler().runTask(SimplePlugin.getInstance(), runnable);
  }

  static void async(final Runnable runnable) {
    Bukkit.getScheduler()
        .runTaskAsynchronously(SimplePlugin.getInstance(), runnable);
  }

  static void later(final Runnable runnable, final long ticks) {
    Bukkit.getScheduler()
        .runTaskLater(SimplePlugin.getInstance(), runnable, ticks);
  }

  static void laterAsync(final Runnable runnable, final long ticks) {
    Bukkit.getScheduler()
        .runTaskLaterAsynchronously(SimplePlugin.getInstance(), runnable,
            ticks);
  }

  static void repeat(final Consumer<Integer> runnable, final long tickPeriod,
      final int times) {
    repeat(runnable, tickPeriod, tickPeriod, times);
  }

  static void repeat(final Consumer<Integer> runnable, final long delay,
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

  static void repeatAsync(final Consumer<Integer> runnable,
      final long tickPeriod, final int times) {
    repeatAsync(runnable, tickPeriod, tickPeriod, times);
  }

  static void repeatAsync(final Consumer<Integer> runnable, final long delay,
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