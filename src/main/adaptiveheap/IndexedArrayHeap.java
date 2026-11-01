package adaptiveheap;

import java.util.NoSuchElementException;
import map.Map;
import map.TreeMap;

/**
 * A binary heap implementation of the AdaptivePriorityQueue ADT, stored in an
 * array in level order, with an index map from each item to the slot it
 * occupies.
 *
 * <p>The item type is Comparable only because the index map is a TreeMap, which
 * keeps its keys in order. The heap itself orders the entries by their
 * priorities.
 *
 * @param <T> the type of items in this priority queue
 * @param <P> the type of priorities
 */
public class IndexedArrayHeap<T extends Comparable<T>, P extends Comparable<P>>
    implements AdaptivePriorityQueue<T, P> {

  // An item together with its priority.
  private static final class Entry<T, P> {
    T item;
    P priority;

    Entry(T item, P priority) {
      this.item = item;
      this.priority = priority;
    }
  }

  private Entry<T, P>[] heap;     // the complete tree, stored in level order
  private Map<T, Integer> index;  // each item to the slot it occupies
  private int size;

  @SuppressWarnings("unchecked")
  public IndexedArrayHeap() {
    // TODO: Implement me
    throw new UnsupportedOperationException("TODO: Implement me");
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public T peek() {
    // TODO: Implement me
    throw new UnsupportedOperationException("TODO: Implement me");
  }

  @Override
  public void add(T item, P priority) {
    // TODO: Implement me
    throw new UnsupportedOperationException("TODO: Implement me");
  }

  @Override
  public void remove() {
    // TODO: Implement me
    throw new UnsupportedOperationException("TODO: Implement me");
  }

  @Override
  public boolean contains(T item) {
    // TODO: Implement me
    throw new UnsupportedOperationException("TODO: Implement me");
  }

  @Override
  public void changePriority(T item, P priority) {
    // TODO: Implement me
    throw new UnsupportedOperationException("TODO: Implement me");
  }

  // Returns a negative number if a has higher priority than b, zero if they
  // tie, and a positive number if a has lower priority.
  private int compare(Entry<T, P> a, Entry<T, P> b) {
    // TODO: Implement me
    throw new UnsupportedOperationException("TODO: Implement me");
  }

  // The parent of the node at index i. Pre: i > 0.
  private int parent(int i) {
    return (i - 1) / 2;
  }

  // The left child of the node at index i.
  private int leftChild(int i) {
    return 2 * i + 1;
  }

  // The right child of the node at index i.
  private int rightChild(int i) {
    return 2 * i + 2;
  }

  // Exchanges the entries at indices i and j, and records their new slots in
  // the index map.
  private void swap(int i, int j) {
    // TODO: Implement me
    throw new UnsupportedOperationException("TODO: Implement me");
  }

  // Moves the entry at index i up until it does not outrank its parent.
  private void swim(int i) {
    while (i > 0 && compare(heap[i], heap[parent(i)]) < 0) {
      swap(i, parent(i));
      i = parent(i);
    }
  }

  // Moves the entry at index i down until no child outranks it.
  private void sink(int i) {
    while (leftChild(i) < size) {  // stop at a leaf
      int best = leftChild(i);
      int right = rightChild(i);
      if (right < size && compare(heap[right], heap[best]) < 0) {
        best = right;
      }
      if (compare(heap[best], heap[i]) >= 0) {
        break;  // no child outranks the entry
      }
      swap(i, best);
      i = best;
    }
  }

  // Doubles the capacity.
  @SuppressWarnings("unchecked")
  private void grow() {
    Entry<T, P>[] bigger = (Entry<T, P>[]) new Entry[heap.length * 2];
    for (int i = 0; i < size; i++) {
      bigger[i] = heap[i];
    }
    heap = bigger;
  }
}
