package adaptiveheap;

import java.util.NoSuchElementException;

/**
 * An AdaptivePriorityQueue serves items in priority order, where a smaller
 * priority number means higher priority, and lets an item's priority be changed
 * after it is added. The items must be distinct: the queue holds no two equal
 * items, so an item always refers to exactly one element.
 *
 * @param <T> the type of items in this priority queue
 */
public interface AdaptivePriorityQueue<T> {
  /**
   * Adds an item to this priority queue with the given priority.
   * @param item the item to add
   * @param priority the item's priority; a smaller number is higher priority
   * @throws IllegalArgumentException if the item is null, or is already in this
   *     priority queue
   */
  void add(T item, int priority);

  /**
   * Returns, without removing, the highest-priority item in this priority queue.
   * @return the highest-priority item
   * @throws NoSuchElementException if this priority queue is empty
   */
  T peek();

  /**
   * Removes the highest-priority item from this priority queue. To inspect that
   * item, call peek before removing it.
   * @throws NoSuchElementException if this priority queue is empty
   */
  void remove();

  /**
   * Changes the priority of an item already in this priority queue, moving it to
   * its new place in the order.
   * @param item the item whose priority to change
   * @param priority the new priority; a smaller number is higher priority
   * @throws IllegalArgumentException if the item is null
   * @throws NoSuchElementException if the item is not in this priority queue
   */
  void changePriority(T item, int priority);

  /**
   * Reports whether the given item is in this priority queue.
   * @param item the item to look for
   * @return true if the item is in this priority queue, false otherwise
   * @throws IllegalArgumentException if the item is null
   */
  boolean contains(T item);

  /**
   * Returns the number of items in this priority queue.
   * @return the number of items in this priority queue
   */
  int size();
}
