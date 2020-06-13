package org.mineacademy.punishcontrol.core;

import de.leonhard.storage.internal.serialize.LightningSerializable;
import de.leonhard.storage.internal.serialize.LightningSerializer;
import java.util.List;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.mineacademy.punishcontrol.core.setting.Replacer;

@UtilityClass
public class PunishControlSerializers {

  public void register() {
    LightningSerializer.registerSerializable(
        new LightningSerializable<Replacer>() {
          @Override
          public Replacer deserialize(@NonNull Object obj) throws ClassCastException {
            if (obj instanceof List<?>) {
              return Replacer.of((List<String>) obj);
            }
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
  }
}
