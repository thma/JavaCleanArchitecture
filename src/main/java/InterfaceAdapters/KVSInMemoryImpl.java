package InterfaceAdapters;

import UseCases.KeyValueStore;
import org.javatuples.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class KVSInMemoryImpl<K, V> implements KeyValueStore<K, V> {

  private final Map<K, V> inMemoryMap = new HashMap<K, V>();

  @Override
  public Optional<V> get(K key) {
    var value = inMemoryMap.get(key);
    if (value == null) {
      return Optional.empty();
    } else {
      return Optional.of(value);
    }
  }

  @Override
  public List<Pair<K, V>> listAll() {
    return inMemoryMap.entrySet().stream()
        .map(entry -> new Pair<K, V>(entry.getKey(), entry.getValue()))
        .collect(Collectors.toList());
  }

  @Override
  public void insert(K key, V value) {
    inMemoryMap.put(key, value);
  }

  @Override
  public void delete(K key) {
    inMemoryMap.remove(key);
  }
}
