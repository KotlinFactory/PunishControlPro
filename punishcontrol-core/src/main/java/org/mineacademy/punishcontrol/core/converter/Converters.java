package org.mineacademy.punishcontrol.core.converter;

import de.leonhard.storage.util.Valid;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Converters {

  private final Map<Map.Entry<Class, Class>, Converter> CONVERTER_MAP = new ConcurrentHashMap<>();

  public <T> T convert(final Object source, final Class<T> to) {
    Converter converter = getConverter(source.getClass(), to);
    if (converter == null) {
      converter = getMultiConverter(source.getClass(), to);
    }

    Valid.notNull(
        converter,
        "No converter available to convert " + source.getClass().getName() + " to " + to
            .getName());
    return (T) converter.convert(source);
  }

  private <S, T> Converter<S, T> getMultiConverter(final Class<S> source,
      final Class<T> to) {
    final List<Converter> converters = new ArrayList<>();
    findConversionRoute(converters, source, to);
    if (converters.size() <= 1) {
      return null;
    }
    return src -> {
      Object out = src;
      for (final Converter converter : converters) {
        out = converter.convert(out);
      }
      return (T) out;
    };
  }

  private boolean findConversionRoute(final List<Converter> converters,
      final Class<?> source, final Class<?> to) {
    for (final Map.Entry<Class, Class> entry : CONVERTER_MAP.keySet()) {
      if (entry.getKey().equals(source)) {
        final List<Converter> list = new ArrayList<>();
        if (buildRoute(list, entry, to)) {
          list.add(CONVERTER_MAP.get(entry));
          Collections.reverse(list);
          converters.addAll(list);
          return true;
        }
      }
    }
    return false;
  }

  private boolean buildRoute(final List<Converter> converters,
      final Map.Entry<Class, Class> entry, final Class<?> to) {
    if (entry.getValue().equals(to)) {
      return true;
    }
    for (final Map.Entry<Class, Class> entry1 : CONVERTER_MAP.keySet()) {
      if (entry1.getKey().equals(entry.getValue())) {
        if (buildRoute(converters, entry1, to)) {
          converters.add(CONVERTER_MAP.get(entry1));
          return true;
        }
      }
    }
    return false;
  }

  public void register(final Class<?> sourceType, final Class targetType,
      final Converter converter) {
    CONVERTER_MAP.put(new AbstractMap.SimpleEntry<>(sourceType, targetType), converter);
  }

  public <S, T> Converter<S, T> getConverter(final Class<S> sourceType,
      final Class<T> targetType) {
    return CONVERTER_MAP.get(new AbstractMap.SimpleEntry<>(sourceType, targetType));
  }
}