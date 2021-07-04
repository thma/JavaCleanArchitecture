package UseCases;

import org.javatuples.Pair;

import java.util.List;
import java.util.Optional;

public interface KeyValueStore<K, V> {

  /*
  -- | a key value store specified as A GADT type
data KVS k v m a where
  ListAllKvs :: KVS k v m [(k, v)]
  GetKvs     :: k -> KVS k v m (Maybe v)
  InsertKvs  :: k -> v -> KVS k v m ()
  DeleteKvs  :: k -> KVS k v m ()
   */

  public Optional<V> get(K key);

  public List<Pair<K, V>> listAll();

  public void insert(K key, V value);

  public void delete(K key);

}
