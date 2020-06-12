package org.mineacademy.punishcontrol.core.injector;

import de.leonhard.storage.internal.DataStorage;
import java.lang.annotation.Annotation;
import lombok.NonNull;
import org.reflections.Reflections;

public interface Injector<C extends Annotation, F extends Annotation>  {

  String pathFromAnnotation(F fieldAnnotation);

  Class<C> classAnnotationClass();

  Class<F> fieldAnnotationClass();

  DataStorage dataStorage();

  void startInjecting();

  void injectPackage(@NonNull Reflections reflections)
      throws Exception;

  <T> T getValueByPath(@NonNull String path, @NonNull T def);
}
