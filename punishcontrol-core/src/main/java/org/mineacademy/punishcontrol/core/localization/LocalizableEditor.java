package org.mineacademy.punishcontrol.core.localization;


import de.leonhard.storage.Yaml;
import de.leonhard.storage.util.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.NonNull;
import org.mineacademy.punishcontrol.core.DaggerCoreComponent;
import org.mineacademy.punishcontrol.core.providers.ExceptionHandler;
import org.mineacademy.punishcontrol.core.setting.Replacer;

public final class LocalizableEditor {

  private final Yaml localization;
  private final ExceptionHandler exceptionHandler;
  private final Localizable localizable;
  private final List<String> value;

  public static LocalizableEditor.Builder builder() {
    return DaggerCoreComponent.create().localizableEditorBuilder();
  }

  public LocalizableEditor(
      Yaml localization,
      ExceptionHandler exceptionHandler,
      Localizable localizable) {
    this.localization = localization;
    this.exceptionHandler = exceptionHandler;
    this.localizable = localizable;
    this.value = new ArrayList<>(localizable.value());
  }

  public boolean canMultiline() {
    return localizable.rawValue() instanceof Collection<?>
           || localizable.rawValue() instanceof String[]
           || localizable.rawValue() instanceof Replacer;
  }

  public void set(final int index, final String string) {
    value.add(index, string);
  }

  public void add(final String element) {
    Valid.checkBoolean(
        !canMultiline(),
        "Can't add line on non-multi line Localizable");
    value.add(element);
  }

  public void save() {
    try {
      save0();
    } catch (final Throwable throwable) {
      exceptionHandler.saveError(
          throwable,
          "LocalizableEditor.save(): Exception while saving Localizable",
          "Value: " + localizable.value(),
          "Field: '" + localizable.field().getName() + "'",
          "Class: '" + localizable.clazz().getName() + "'");
    }
  }

  private void save0() throws IllegalAccessException {

    final Object rawValue = localizable.rawValue();

    if (rawValue == null)
      return;

    if (rawValue instanceof String)
      localizable.field().set(null, value.get(0));

    if (rawValue instanceof Collection<?>)
      localizable.field().set(null, value);

    if (rawValue instanceof String[])
      localizable.field().set(null, value.toArray(new String[0]));

    if (rawValue instanceof Replacer)
      localizable.field().set(null, Replacer.of(value));

    localization.set(localizable.path(), value);
  }

  public static final class Builder {

    private final Yaml localization;
    private final ExceptionHandler exceptionHandler;

    @Inject
    public Builder(
        @Named("localization") @NonNull final Yaml localization,
        @NonNull final ExceptionHandler exceptionHandler) {
      this.localization = localization;
      this.exceptionHandler = exceptionHandler;
    }

    public LocalizableEditor build(@NonNull final Localizable localizable) {
      return new LocalizableEditor(localization, exceptionHandler, localizable);
    }
  }
}
