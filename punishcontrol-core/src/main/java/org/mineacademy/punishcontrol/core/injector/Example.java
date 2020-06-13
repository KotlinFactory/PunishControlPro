package org.mineacademy.punishcontrol.core.injector;

import org.mineacademy.punishcontrol.core.injector.annotations.Setting;
import org.mineacademy.punishcontrol.core.setting.Replacer;

public class Example {

  @Setting
  public static final class Test {

    public static Replacer test() {
      return test;
    }

    @Setting(path = "test")
    private static Replacer test = Replacer.of("TEST-3");
  }
}
