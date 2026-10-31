package stack;

import java.util.NoSuchElementException;

/**
 * A Stack is a collection that supports last-in-first-out (LIFO) access.
 *
 * @param <T> the type of elements in this stack.
 */
public interface Stack<T> {

  /**
   * Pushes an item onto the top of this stack.
   *
   * @param item the item to be pushed onto this stack.
   * @throws IllegalArgumentException if the item is null.
   */
  void push(T item);

  /**
   * Removes the item at the top of this stack.
   *
   * @throws NoSuchElementException if this stack is empty.
   */
  void pop();

  /**
   * Returns the item at the top of this stack without removing it.
   *
   * @return the item at the top of this stack.
   * @throws NoSuchElementException if this stack is empty.
   */
  T top();

  /**
   * Returns true if this stack contains no elements.
   *
   * @return true if this stack is empty, false otherwise.
   */
  boolean isEmpty();
}
