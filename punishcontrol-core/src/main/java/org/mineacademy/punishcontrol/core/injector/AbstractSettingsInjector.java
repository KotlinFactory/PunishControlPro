package org.mineacademy.punishcontrol.core.injector;

import de.leonhard.storage.internal.DataStorage;
import de.leonhard.storage.internal.serialize.LightningSerializable;
import de.leonhard.storage.internal.serialize.LightningSerializer;
import java.lang.annotation.Annotation;
import java.util.Set;
import lombok.NonNull;

public abstract class AbstractSettingsInjector<C extends Annotation,
    F extends Annotation> extends AbstractInjector<C, F> {
  private final DataStorage dataStorage;

  protected AbstractSettingsInjector(
      Class<C> classAnnotationClass,
      Class<F> fieldAnnotationClass,
      DataStorage dataStorage,
      Set<String> packages) {
    super(classAnnotationClass, fieldAnnotationClass, packages);
    this.dataStorage = dataStorage;
  }

  @Override
  public DataStorage dataStorage() {
    return dataStorage;
  }

  @Override
  public final <T> T getValueByPath(@NonNull final String path, @NonNull final T def) {
    final LightningSerializable serializable =
        LightningSerializer.findSerializable(def.getClass());

    if (serializable == null) {
      return dataStorage.getOrSetDefault(path, def);
    }

    if (dataStorage.contains(path)) {
      return (T) dataStorage.getSerializable(path, def.getClass());
    }
    dataStorage.setSerializable(path, serializable);
    return (T) serializable.serialize(def);
  }

}
