package org.grimjo.macrocore.game.utils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ImmutabilityUtils {

  public static <K, V> Map<K, List<V>> freezeMap(Map<K, List<V>> input) {
    if (input == null || input.isEmpty()) {
      return Map.of();
    }
    return input.entrySet().stream()
        .collect(Collectors.toUnmodifiableMap(
            Map.Entry::getKey,
            entry -> List.copyOf(entry.getValue())
        ));
  }

  public static <K, V> SortedMap<K, List<V>> freezeSortedMap(SortedMap<K, List<V>> input) {
    if (input == null || input.isEmpty()) {
      return Collections.emptySortedMap();
    }

    TreeMap<K, List<V>> frozenTree = new TreeMap<>(input.comparator());

    input.forEach((key, list) -> frozenTree.put(key, List.copyOf(list)));

    return Collections.unmodifiableSortedMap(frozenTree);
  }

  public static <T> List<T> freezeList(List<T> input) {
    return (input == null || input.isEmpty()) ? List.of() : List.copyOf(input);
  }
}
