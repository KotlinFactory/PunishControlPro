package org.mineacademy.punishcontrol.core.injector;


import de.leonhard.storage.util.Valid;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractInjector<C extends Annotation, F extends Annotation>
    implements Injector<C, F> {

  protected final Class<C> classAnnotationClass;
  protected final Class<F> fieldAnnotationClass;

  @Override
  public final void startInjecting(List<Class<?>> classes) {
    for (Class<?> clazz : classes) {

      if (!clazz.isAnnotationPresent(classAnnotationClass)) {
        continue;
      }

//      System.out.println("Trying to inject: " + clazz.getName());

      for (Field field : clazz.getDeclaredFields()) {

        // We only inject static fields
        if (!Modifier.isStatic(field.getModifiers())) {
//          System.out.println("Field is not static: " + field.getName());
          continue;
        }

        if (!field.isAnnotationPresent(fieldAnnotationClass)) {
//          System.out.println("field annotation is not present at");
          continue;
        }

        Valid.checkBoolean(
            !Modifier.isFinal(field.getModifiers()),
            "Can't inject final field '" + field.getName() + "'",
            "Class: " + clazz.getName());

        final F fieldAnnotation = field.getAnnotation(fieldAnnotationClass);
        final String path = pathFromAnnotation(fieldAnnotation);
        Valid.checkBoolean(
            !path.isEmpty(),
            "Path mustn't be empty",
            "Class: " + clazz.getName(),
            "Field: " + field.getName());
        try {
//          System.out.println("Trying to inject: " + field.getName() + " in " + clazz.getName());
          field.setAccessible(true);
          final Object raw = field.get(null);
          field.set(null, getValueByPath(path, raw));
          System.out.println("Injected: " + field.getName() + " in " + clazz.getName());
        } catch (final Throwable throwable) {
          throwable.printStackTrace();
        }
      }
    }
  }
}
