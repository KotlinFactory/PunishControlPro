package org.mineacademy.punishcontrol.core.injector;


import de.leonhard.storage.util.Valid;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.reflections.Reflections;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractInjector<C extends Annotation, F extends Annotation>
    implements Injector<C, F> {

  protected final Class<C> classAnnotationClass;
  protected final Class<F> fieldAnnotationClass;
  private final Set<String> packages;

  @Override
  public final void startInjecting() {
    for (String pack : packages) {
      try {
        System.out.println();
        injectPackage(new Reflections(pack));
      } catch (Throwable throwable) {
        throwable.printStackTrace();
      }
    }
  }

  @Override
  public final void injectPackage(@NonNull final Reflections reflections)
      throws Exception {
    for (Class<?> clazz : reflections
        .getTypesAnnotatedWith(classAnnotationClass)) {
      for (Field field : clazz.getFields()) {

        // We only inject static fields
        if (!Modifier.isStatic(field.getModifiers())) {
          continue;
        }

        Valid.checkBoolean(
            !Modifier.isFinal(field.getModifiers()),
            "Can't inject final field '" + field.getName() + "'");

        if (!field.isAnnotationPresent(fieldAnnotationClass)) {
          continue;
        }

        final F fieldAnnotation = field.getAnnotation(fieldAnnotationClass);
        final String path = pathFromAnnotation(fieldAnnotation);
        try {
          final Object raw = field.get(null);
          field.set(null, getValueByPath(path, raw));
        } catch (final Throwable throwable) {
          System.err.println("Exception while getting & injecting members");
          throwable.printStackTrace();
        }
      }
    }
  }
}
