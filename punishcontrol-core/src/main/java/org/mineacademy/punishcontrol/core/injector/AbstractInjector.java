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
import org.mineacademy.punishcontrol.core.setting.Replacer;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractInjector<C extends Annotation, F extends Annotation>
    implements Injector<C, F> {

  protected final Class<C> classAnnotationClass;
  protected final Class<F> fieldAnnotationClass;

  @Override
  public final void startInjecting(final List<Class<?>> classes) {
    for (final Class<?> clazz : classes) {

      if (!clazz.isAnnotationPresent(classAnnotationClass))
        continue;
//      if (!clazz.getSimpleName().equalsIgnoreCase("AbstractPunishBrowser"))
//        continue;

//      System.out.println("Trying to inject: " + clazz.getName());

      {

        for (final Field field : clazz.getDeclaredFields()) {

          // We only inject static fields
          //          System.out.println("Field is not static: " + field.getName());
          if (!Modifier.isStatic(field.getModifiers()))
            continue;

          //          System.out.println("field annotation is not present at");
          if (!field.isAnnotationPresent(fieldAnnotationClass))
            continue;

          Valid.checkBoolean(
              !Modifier.isFinal(field.getModifiers()),
              "Can't inject final field '" + field.getName() + "'",
              "Field is final!",
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
//            System.out.println("Pre: ");
//            debugRep(raw);
            field.set(null, getValueByPath(path, raw));
            onInjected(clazz, field, path);
//            System.out.println("After: ");
//            debugRep(getValueByPath(path, raw));
//            System.out.println("Injected: " + field.getName() + " in " + clazz.getName());
          } catch (final Throwable throwable) {
            System.err.println("Exception while injecting!");
            System.err.println("Path:  '" + path + "'");
            throwable.printStackTrace();
          }
        }
      }
    }
  }

  protected void onInjected(final Class<?> clazz, final Field field, final String path) {

  }


  private static void debugRep(final Object replacer) {
    if (!(replacer instanceof Replacer))
      return;
    final Replacer rep = (Replacer) replacer;
    for (final String s : rep.replacedMessage())
      System.out.println(s);
  }
}
