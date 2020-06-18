package org.mineacademy.punishcontrol.core.injectors;

import de.leonhard.storage.Yaml;
import java.lang.reflect.Field;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.NonNull;
import org.mineacademy.punishcontrol.core.injector.AbstractSettingsInjector;
import org.mineacademy.punishcontrol.core.injector.annotations.Localizable;
import org.mineacademy.punishcontrol.core.localization.Localizables;

public final class LocalizationInjector
    extends AbstractSettingsInjector<Localizable, Localizable> {

  @Inject
  public LocalizationInjector(@NonNull @Named("localization") final Yaml config) {
    super(Localizable.class, Localizable.class, config);
  }

  @Override
  protected void onInjected(final Class<?> clazz, final Field field, final String path) {
    Localizables.register(
        org.mineacademy.punishcontrol.core.localization.Localizable
            .builder()
            .clazz(clazz)
            .field(field)
            .path(path)
            .build()
    );
  }

  @Override
  public String pathFromAnnotation(final Localizable fieldAnnotation) {
    return fieldAnnotation.value();
  }
}


