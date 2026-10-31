package stack;

import java.util.NoSuchElementException;

/**
 * A node-backed implementation of the Stack ADT.
 *
 * @param <T> the type of elements in this stack.
 */
public class LinkedStack<T> implements Stack<T> {

  private Node<T> head;  // the top of the stack, or null when empty

  private static class Node<T> {
    T value;
    Node<T> next;

    Node(T value) {
      this.value = value;
    }
  }

  public LinkedStack() {
    head = null;
  }

  @Override
  public void push(T item) {
    if (item == null) {
      throw new IllegalArgumentException();
    }
    Node<T> newNode = new Node<>(item);
    newNode.next = head;
    head = newNode;
  }

  @Override
  public void pop() {
    if (isEmpty()) {
      throw new NoSuchElementException();
    }
    head = head.next;
  }

  @Override
  public T top() {
    if (isEmpty()) {
      throw new NoSuchElementException();
    }
    return head.value;
  }

  @Override
  public boolean isEmpty() {
    return head == null;
  }
}
