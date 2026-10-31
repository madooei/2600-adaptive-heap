package map;

/**
 * A Map is a collection of key-value pairs with distinct keys and no order.
 * A map is iterable over its keys: an enhanced for loop visits every key, in no particular order.
 *
 * @param <K> the type of keys in this map.
 * @param <V> the type of values in this map.
 */
public interface Map<K, V> extends Iterable<K> {

  /**
   * Associates the given value with the given key. If the key is already
   * present, its value is replaced with the given value.
   *
   * @param key the key to add or update.
   * @param value the value to associate with the key.
   * @return true if the key was not previously present, false if it was.
   * @throws IllegalArgumentException if the key or the value is null.
   */
  boolean put(K key, V value);

  /**
   * Returns the value associated with the given key, or null if the key is not
   * present.
   *
   * @param key the key to look up.
   * @return the value associated with the key, or null if the key is absent.
   * @throws IllegalArgumentException if the key is null.
   */
  V get(K key);

  /**
   * Removes the given key and its value from this map if the key is present.
   *
   * @param key the key to remove.
   * @return true if the key was removed, false if it was not present.
   * @throws IllegalArgumentException if the key is null.
   */
  boolean remove(K key);

  /**
   * Returns true if this map contains the given key.
   *
   * @param key the key to look for.
   * @return true if the key is present, false otherwise.
   * @throws IllegalArgumentException if the key is null.
   */
  boolean containsKey(K key);

  /**
   * Returns the number of key-value pairs in this map.
   *
   * @return the number of pairs in this map.
   */
  int size();

  /**
   * Returns an iterable over the key-value pairs in this map. It visits each
   * pair once, in no particular order.
   *
   * @return an iterable over the key-value pairs in this map.
   */
  Iterable<Map.Entry<K, V>> entries();

  /**
   * A read-only view of one key-value pair.
   *
   * @param <K> the type of the key in this pair.
   * @param <V> the type of the value in this pair.
   */
  interface Entry<K, V> {

    /**
     * Returns the key of this pair.
     *
     * @return the key of this pair.
     */
    K key();

    /**
     * Returns the value of this pair.
     *
     * @return the value of this pair.
     */
    V value();
  }
}
