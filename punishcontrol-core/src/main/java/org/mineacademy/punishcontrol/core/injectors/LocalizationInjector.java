package org.mineacademy.punishcontrol.core.injectors;

import de.leonhard.storage.Yaml;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.NonNull;
import org.mineacademy.punishcontrol.core.injector.AbstractSettingsInjector;
import org.mineacademy.punishcontrol.core.injector.annotations.Localizable;

public final class LocalizationInjector
    extends AbstractSettingsInjector<Localizable, Localizable>{

  @Inject
  public LocalizationInjector(@NonNull @Named("localization") final Yaml config) {
    super(Localizable.class, Localizable.class, config);
  }

  @Override
  public String pathFromAnnotation(Localizable fieldAnnotation) {
    return fieldAnnotation.value();
  }
}


