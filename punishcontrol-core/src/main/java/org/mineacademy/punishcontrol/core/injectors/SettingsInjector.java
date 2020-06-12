package org.mineacademy.punishcontrol.core.injectors;

import de.leonhard.storage.internal.DataStorage;
import java.util.Set;
import lombok.NonNull;
import org.mineacademy.punishcontrol.core.injector.AbstractSettingsInjector;
import org.mineacademy.punishcontrol.core.injector.annotations.Setting;

public final class SettingsInjector extends AbstractSettingsInjector<Setting, Setting> {

  public SettingsInjector(
      @NonNull final DataStorage dataStorage,
      @NonNull final Set<String> packages) {
    super(Setting.class, Setting.class, dataStorage, packages);
  }

  @Override
  public String pathFromAnnotation(Setting fieldAnnotation) {
    return fieldAnnotation.path();
  }
}
