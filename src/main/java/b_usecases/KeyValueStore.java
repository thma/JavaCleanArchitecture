package b_usecases;

import org.javatuples.Pair;

import java.util.List;
import java.util.Optional;

public interface KeyValueStore<K, V> {

  public Optional<V> get(K key);

  public List<Pair<K, V>> listAll();

  public void insert(K key, V value);

  public void delete(K key);

}
