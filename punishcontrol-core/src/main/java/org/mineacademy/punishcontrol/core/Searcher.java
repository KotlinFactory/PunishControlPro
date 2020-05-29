package org.mineacademy.punishcontrol.core;

import info.debatty.java.stringsimilarity.JaroWinkler;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.mineacademy.punishcontrol.core.settings.Settings.Advanced;

@UtilityClass
public class Searcher {

  private final JaroWinkler JARO_WINKLER = new JaroWinkler();

  public List<SearchResult> search(
      @NonNull final String part,
      @NonNull final List<String> values) {
    final List<SearchResult> result = new ArrayList<>();
    for (final String value : values) {

      if (Advanced.STARTS_WITH) {
        if (value.toLowerCase().startsWith(part.toLowerCase())) {
          result.add(new SearchResult() {
            @Override
            public String result() {
              return value;
            }

            @Override
            public double similarity() {
              return 0.9;
            }
          });
          continue;
        }
      }

      final double similarity = JARO_WINKLER.similarity(part, value);
      if (similarity < Advanced.MIN_SIMILARITY) {
        continue;
      }

      result.add((new SearchResult() {
        @Override
        public String result() {
          return value;
        }

        @Override
        public double similarity() {
          return similarity;
        }
      }));
    }

    result.sort((o1, o2) -> o1.similarity() == o2.similarity() ? 1 : -1);

    return result;
  }


  public interface SearchResult {

    String result();

    double similarity();
  }
}
