package org.mineacademy.punishcontrol.core.injectors;

import de.leonhard.storage.Yaml;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.NonNull;
import org.mineacademy.punishcontrol.core.injector.AbstractSettingsInjector;
import org.mineacademy.punishcontrol.core.injector.annotations.Setting;

public final class SettingsInjector extends AbstractSettingsInjector<Setting, Setting> {

  @Inject
  public SettingsInjector(@NonNull @Named("settings") final Yaml config) {
    super(Setting.class, Setting.class, config);
  }

  @Override
  public String pathFromAnnotation(Setting fieldAnnotation) {
    return fieldAnnotation.path();
  }
}
