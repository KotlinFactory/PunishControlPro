package org.mineacademy.punishcontrol.core.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor //Must be public
public abstract class LoadingCache<K, V> {

  private final Map<K, V> map = new HashMap<>();
  private final long duration;
  private final TimeUnit timeUnit;

  public abstract V load(K k);


  public synchronized void put(final K k, final V v) {
    map.put(k, v);
    new Timer()
        .schedule(
            new TimerTask() {
              @Override
              public void run() {
                if (!map.containsKey(k)) {
                  return;
                }

                map.remove(k);
              }
            },
            timeUnit.toMillis(duration));
  }

  public synchronized V get(final K k) {
    if (k != null && !map.containsKey(k)) {
      put(k, load(k));
    }

    return map.get(k);
  }
}
