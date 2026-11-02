package adaptiveheap;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * The contract test suite for the AdaptivePriorityQueue ADT, written against
 * the AdaptivePriorityQueue interface alone. A concrete subclass supplies
 * createQueue() to pick the implementation.
 */
public abstract class AdaptivePriorityQueueTest {

  private AdaptivePriorityQueue<String, Integer> queue;

  protected abstract AdaptivePriorityQueue<String, Integer> createQueue();

  @BeforeEach
  public void setup() {
    queue = createQueue();
  }

  @Test
  public void newQueueIsEmpty() {
    assertEquals(0, queue.size());
  }

  @Test
  public void peekOnEmptyQueueThrows() {
    try {
      queue.peek();
      fail("expected NoSuchElementException when peeking an empty queue");
    } catch (NoSuchElementException e) {
      return;
    }
  }

  @Test
  public void removeOnEmptyQueueThrows() {
    try {
      queue.remove();
      fail("expected NoSuchElementException when removing from an empty queue");
    } catch (NoSuchElementException e) {
      return;
    }
  }

  @Test
  public void containsFindsAnAddedItem() {
    queue.add("Amy", 5);
    assertTrue(queue.contains("Amy"));
  }

  @Test
  public void containsIsFalseForAnAbsentItem() {
    queue.add("Amy", 5);
    assertFalse(queue.contains("Bob"));
  }

  @Test
  public void containsIsFalseAfterTheItemIsRemoved() {
    queue.add("Amy", 5);
    queue.add("Bob", 8);
    queue.remove();
    assertFalse(queue.contains("Amy"));
  }

  @Test
  public void containsIsTrueForTheItemLeftAfterRemove() {
    queue.add("Amy", 5);
    queue.add("Bob", 8);
    queue.remove();
    assertTrue(queue.contains("Bob"));
  }

  @Test
  public void decreaseKeyMovesTheItemToTheFront() {
    queue.add("Jane", 4);
    queue.add("Bob", 2);
    queue.changePriority("Jane", 1);
    assertEquals("Jane", queue.peek());
  }

  @Test
  public void changeToTheSamePriorityKeepsTheOrder() {
    queue.add("Amy", 1);
    queue.add("Bob", 2);
    queue.changePriority("Bob", 2);
    assertEquals(listOf("Amy", "Bob"), drain(queue));
  }

  @Test
  public void itemsMayShareAPriority() {
    queue.add("Amy", 2);
    queue.add("Bob", 2);
    assertEquals(2, drain(queue).size());
  }

  @Test
  public void peekReturnsTheSmallestPriority() {
    queue.add("Amy", 5);
    queue.add("Bob", 8);
    queue.add("Cal", 3);
    assertEquals("Cal", queue.peek());
  }

  @Test
  public void sizeCountsTheAddedItems() {
    queue.add("Amy", 5);
    queue.add("Bob", 8);
    queue.add("Cal", 3);
    assertEquals(3, queue.size());
  }

  @Test
  public void removeServesItemsInPriorityOrder() {
    queue.add("Amy", 5);
    queue.add("Bob", 8);
    queue.add("Cal", 3);
    queue.add("Dan", 6);
    queue.add("Eve", 1);
    assertEquals(listOf("Eve", "Cal", "Amy", "Dan", "Bob"), drain(queue));
  }

  @Test
  public void increaseKeyMovesTheItemBack() {
    queue.add("Amy", 1);
    queue.add("Bob", 2);
    queue.add("Cal", 3);
    queue.changePriority("Amy", 9);
    assertEquals(listOf("Bob", "Cal", "Amy"), drain(queue));
  }

  @Test
  public void addNullThrows() {
    try {
      queue.add(null, 1);
      fail("expected IllegalArgumentException when adding null");
    } catch (IllegalArgumentException e) {
      return;
    }
  }

  @Test
  public void addNullPriorityThrows() {
    try {
      queue.add("Amy", null);
      fail("expected IllegalArgumentException when adding with a null priority");
    } catch (IllegalArgumentException e) {
      return;
    }
  }

  @Test
  public void addDuplicateThrows() {
    queue.add("Amy", 5);
    try {
      queue.add("Amy", 2);
      fail("expected IllegalArgumentException when adding an item twice");
    } catch (IllegalArgumentException e) {
      return;
    }
  }

  @Test
  public void addDuplicateKeepsTheSize() {
    queue.add("Amy", 5);
    try {
      queue.add("Amy", 2);
    } catch (IllegalArgumentException e) {
      // expected
    }
    assertEquals(1, queue.size());
  }

  @Test
  public void changePriorityOfNullThrows() {
    try {
      queue.changePriority(null, 1);
      fail("expected IllegalArgumentException when changing the priority of null");
    } catch (IllegalArgumentException e) {
      return;
    }
  }

  @Test
  public void changePriorityToNullThrows() {
    queue.add("Amy", 5);
    try {
      queue.changePriority("Amy", null);
      fail("expected IllegalArgumentException when the new priority is null");
    } catch (IllegalArgumentException e) {
      return;
    }
  }

  @Test
  public void changePriorityOfAbsentItemThrows() {
    queue.add("Amy", 5);
    try {
      queue.changePriority("Bob", 1);
      fail("expected NoSuchElementException when the item is not in the queue");
    } catch (NoSuchElementException e) {
      return;
    }
  }

  @Test
  public void containsNullThrows() {
    try {
      queue.contains(null);
      fail("expected IllegalArgumentException when asking for null");
    } catch (IllegalArgumentException e) {
      return;
    }
  }

  // Removes every item, highest priority first, and returns them in that order.
  private static List<String> drain(AdaptivePriorityQueue<String, Integer> queue) {
    List<String> served = new ArrayList<>();
    while (queue.size() > 0) {
      served.add(queue.peek());
      queue.remove();
    }
    return served;
  }

  private static List<String> listOf(String... items) {
    List<String> list = new ArrayList<>();
    for (String item : items) {
      list.add(item);
    }
    return list;
  }
}
