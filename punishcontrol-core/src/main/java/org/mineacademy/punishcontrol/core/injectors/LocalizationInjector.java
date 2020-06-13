package org.mineacademy.punishcontrol.core.injectors;

import de.leonhard.storage.Yaml;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.NonNull;
import org.mineacademy.punishcontrol.core.injector.AbstractSettingsInjector;
import org.mineacademy.punishcontrol.core.injector.annotations.Localization;

public final class LocalizationInjector
    extends AbstractSettingsInjector<Localization, Localization>{

  @Inject
  public LocalizationInjector(@NonNull @Named("localization") final Yaml config) {
    super(Localization.class, Localization.class, config);
  }

  @Override
  public String pathFromAnnotation(Localization fieldAnnotation) {
    return fieldAnnotation.path();
  }
}


