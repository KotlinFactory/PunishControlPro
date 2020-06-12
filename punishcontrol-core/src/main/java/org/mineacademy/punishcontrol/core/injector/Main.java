package org.mineacademy.punishcontrol.core.injector;

import de.leonhard.storage.Json;
import de.leonhard.storage.internal.DataStorage;
import java.util.Arrays;
import java.util.HashSet;
import org.mineacademy.punishcontrol.core.injector.annotations.Setting;
import org.mineacademy.punishcontrol.core.injectors.SettingsInjector;

public class Main {

  public static void main(String[] args) {
    final DataStorage dataStorage = new Json("Test", "");
    final SettingsInjector injector = new SettingsInjector(dataStorage,
       new HashSet<>(Arrays.asList("org.mineacademy", Main.class.getPackage().getName())));

    injector.startInjecting();
  }


  public static final class Test{

    @Setting(path = "test")
    private static String test = "TEST-3";
  }
}
