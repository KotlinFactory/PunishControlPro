package org.mineacademy.punishcontrol.core.injector;

import de.leonhard.storage.Json;
import de.leonhard.storage.internal.DataStorage;
import de.leonhard.storage.internal.serialize.LightningSerializable;
import de.leonhard.storage.internal.serialize.LightningSerializer;
import java.util.List;
import lombok.NonNull;
import org.mineacademy.punishcontrol.core.injector.annotations.Setting;
import org.mineacademy.punishcontrol.core.injectors.SettingsInjector;
import org.mineacademy.punishcontrol.core.setting.Replacer;

public class Example {

  public static void start(List<Class<?>> classes) {
    final DataStorage dataStorage = new Json("Matrix", "plugins/PunishControl");
    final SettingsInjector injector = new SettingsInjector(dataStorage);

    LightningSerializer.registerSerializable(
        new LightningSerializable<Replacer>() {
          @Override
          public Replacer deserialize(@NonNull Object obj) throws ClassCastException {
            return Replacer.of(obj.toString());
          }

          @Override
          public Object serialize(@NonNull Replacer replacer) throws ClassCastException {
            return replacer.replacedMessage().length == 1
                ? replacer.replacedMessageJoined()
                : replacer.replacedMessage();
          }

          @Override
          public Class<Replacer> getClazz() {
            return Replacer.class;
          }
        }
    );

    injector.startInjecting(classes);
  }


  @Setting
  public static final class Test {

    public static Replacer test() {
      return test;
    }

    @Setting(path = "test")
    private static Replacer test = Replacer.of("TEST-3");
  }
}
