package org.mineacademy.punishcontrol.core.setting;

import de.leonhard.storage.util.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.val;

/**
 * Platform independent version of the Replacer
 */
@Getter
@Accessors(fluent = true)
public final class Replacer {

  public static final String DELIMITER = "\n";
  private final List<String> messages;

  //Messages to replace
  private final List<String> variables = new ArrayList<>();
  private final List<Object> replacements = new ArrayList<>();

  // ----------------------------------------------------------------------------------------------------
  // Static Factory methods
  // ----------------------------------------------------------------------------------------------------
  public static Replacer of(@NonNull final String... messages) {
    return new Replacer(Arrays.asList(messages));
  }

  public static Replacer of(@NonNull final List<String> messages) {
    return new Replacer(messages);
  }

  public static Replacer ofMultilineString(
      @NonNull final String multilineString) {
    return new Replacer(Arrays.asList(multilineString.split(DELIMITER)));
  }

  private Replacer(List<String> messages) {
    // Cloning our messages
    this.messages = new ArrayList<>(messages);
  }

  public Replacer find(@NonNull final String... variables) {
    this.variables.clear();
    this.variables.addAll(Arrays.asList(variables));
    return this;
  }

  public Replacer replace(@NonNull final Object... replacements) {
    this.replacements.clear();

    this.replacements.addAll(Arrays.asList(replacements));
    return this;
  }

  public Replacer set(final int index, final String message) {
    messages.set(index, message);
    return this;
  }

  /**
   * Attempts to replace key:value pairs automatically
   */
  public Replacer replaceAll(@NonNull final Object... associativeArray) {
    variables.clear();
    replacements.clear();

    //Even: Value
    for (int i = 0; i < associativeArray.length; i++)
      if (i % 2 == 0) {
        //Odd: Key
        final val raw = associativeArray[i];
        Valid.checkBoolean(
            raw instanceof String,
            "Expected String at " + raw + ", got " + raw.getClass()
                .getSimpleName());
        variables.add((String) raw);
      } else
        replacements.add(associativeArray[i]);
    return this;
  }

  public String[] replacedMessage() {
    Valid.checkBoolean(
        replacements.size() == variables.size(),
        "Variables " + variables.size()
        + " != replacements " + replacements.size(),
        "Variables: " + variables.toString(),
        "Replacments: " + replacements
    );

    // Join and replace as 1 message for maximum performance
    String message = String.join(DELIMITER, messages);

    for (int i = 0; i < variables.size(); i++) {
      String found = variables.get(i);
      { // Auto insert brackets
        if (!found.startsWith("{"))
          found = "{" + found;

        if (!found.endsWith("}"))
          found = found + "}";
      }
      final Object rep = i <= replacements.size() ? replacements.get(i) : null;

      message = message.replace(found, rep == null ? "" : rep.toString());
    }

    return message.split(DELIMITER);
  }

  public String replacedMessageJoined() {
    return String.join(DELIMITER, replacedMessage());
  }
}
