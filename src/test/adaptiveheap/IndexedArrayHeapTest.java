package adaptiveheap;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import map.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests for IndexedArrayHeap. Some tests check the AdaptivePriorityQueue
 * contract through the public methods. The others read the private heap array
 * and index map by reflection, to check the slots the notes draw and that every
 * item's map entry names the slot it occupies.
 */
public class IndexedArrayHeapTest {

  private IndexedArrayHeap<String, Integer> heap;

  @BeforeEach
  public void setup() {
    heap = new IndexedArrayHeap<>();
  }

  @Test
  public void newHeapIsEmpty() {
    assertEquals(0, heap.size());
  }

  @Test
  public void peekOnEmptyHeapThrows() {
    try {
      heap.peek();
      fail("expected NoSuchElementException when peeking an empty queue");
    } catch (NoSuchElementException e) {
      return;
    }
  }

  @Test
  public void removeOnEmptyHeapThrows() {
    try {
      heap.remove();
      fail("expected NoSuchElementException when removing from an empty queue");
    } catch (NoSuchElementException e) {
      return;
    }
  }

  @Test
  public void containsFindsAnAddedItem() {
    heap.add("Amy", 5);
    assertTrue(heap.contains("Amy"));
  }

  @Test
  public void containsIsFalseForAnAbsentItem() {
    heap.add("Amy", 5);
    assertFalse(heap.contains("Bob"));
  }

  @Test
  public void containsIsFalseAfterTheItemIsRemoved() {
    heap.add("Amy", 5);
    heap.add("Bob", 8);
    heap.remove();
    assertFalse(heap.contains("Amy"));
  }

  @Test
  public void containsIsTrueForTheItemLeftAfterRemove() {
    heap.add("Amy", 5);
    heap.add("Bob", 8);
    heap.remove();
    assertTrue(heap.contains("Bob"));
  }

  @Test
  public void decreaseKeyMovesTheItemToTheFront() {
    heap.add("Jane", 4);
    heap.add("Bob", 2);
    heap.changePriority("Jane", 1);
    assertEquals("Jane", heap.peek());
  }

  @Test
  public void changeToTheSamePriorityKeepsTheOrder() {
    heap.add("Amy", 1);
    heap.add("Bob", 2);
    heap.changePriority("Bob", 2);
    assertEquals(listOf("Amy", "Bob"), drain(heap));
  }

  @Test
  public void itemsMayShareAPriority() {
    heap.add("Amy", 2);
    heap.add("Bob", 2);
    assertEquals(2, drain(heap).size());
  }

  @Test
  public void removeRecordsTheRootSlotOfAnItemThatDoesNotSink() {
    heap.add("Amy", 1);
    heap.add("Bob", 2);
    heap.remove();
    assertIndexMapMatchesHeap(heap);
  }

  @Test
  public void peekReturnsTheSmallestPriority() {
    heap.add("Amy", 5);
    heap.add("Bob", 8);
    heap.add("Cal", 3);
    assertEquals("Cal", heap.peek());
  }

  @Test
  public void sizeCountsTheAddedItems() {
    heap.add("Amy", 5);
    heap.add("Bob", 8);
    heap.add("Cal", 3);
    assertEquals(3, heap.size());
  }

  @Test
  public void removeServesItemsInPriorityOrder() {
    heap.add("Amy", 5);
    heap.add("Bob", 8);
    heap.add("Cal", 3);
    heap.add("Dan", 6);
    heap.add("Eve", 1);
    assertEquals(listOf("Eve", "Cal", "Amy", "Dan", "Bob"), drain(heap));
  }

  @Test
  public void increaseKeyMovesTheItemBack() {
    heap.add("Amy", 1);
    heap.add("Bob", 2);
    heap.add("Cal", 3);
    heap.changePriority("Amy", 9);
    assertEquals(listOf("Bob", "Cal", "Amy"), drain(heap));
  }

  // The five patients the notes start from: Amy 1, Bob 2, Cal 4, Dan 5, Eve 3.
  private void addFivePatients() {
    heap.add("Amy", 1);
    heap.add("Bob", 2);
    heap.add("Cal", 4);
    heap.add("Dan", 5);
    heap.add("Eve", 3);
  }

  @Test
  public void fivePatientsFillTheSlotsInOrder() {
    addFivePatients();
    assertSlots(heap, listOf("Amy", "Bob", "Cal", "Dan", "Eve"), 1, 2, 4, 5, 3);
  }

  @Test
  public void fivePatientsAreRecordedInTheMap() {
    addFivePatients();
    assertIndexMapMatchesHeap(heap);
  }

  @Test
  public void addSwimsTheNewItem() {
    addFivePatients();
    heap.add("Fox", 0);
    assertSlots(heap, listOf("Fox", "Bob", "Amy", "Dan", "Eve", "Cal"), 0, 2, 1, 5, 3, 4);
  }

  @Test
  public void addRecordsTheNewSlotsInTheMap() {
    addFivePatients();
    heap.add("Fox", 0);
    assertIndexMapMatchesHeap(heap);
  }

  @Test
  public void removeSinksTheMovedItem() {
    addFivePatients();
    heap.add("Fox", 0);
    heap.remove();
    assertSlots(heap, listOf("Amy", "Bob", "Cal", "Dan", "Eve"), 1, 2, 4, 5, 3);
  }

  @Test
  public void removeRecordsTheNewSlotsInTheMap() {
    addFivePatients();
    heap.add("Fox", 0);
    heap.remove();
    assertIndexMapMatchesHeap(heap);
  }

  @Test
  public void removeClearsTheVacatedSlot() {
    addFivePatients();
    heap.remove();
    assertNull(entries(heap)[4]);
  }

  @Test
  public void decreaseKeySwimsTheItem() {
    addFivePatients();
    heap.changePriority("Dan", 1);
    assertSlots(heap, listOf("Amy", "Dan", "Cal", "Bob", "Eve"), 1, 1, 4, 2, 3);
  }

  @Test
  public void decreaseKeyRecordsTheNewSlotsInTheMap() {
    addFivePatients();
    heap.changePriority("Dan", 1);
    assertIndexMapMatchesHeap(heap);
  }

  @Test
  public void increaseKeySinksTheItem() {
    addFivePatients();
    heap.changePriority("Amy", 6);
    assertSlots(heap, listOf("Bob", "Eve", "Cal", "Dan", "Amy"), 2, 3, 4, 5, 6);
  }

  @Test
  public void increaseKeyRecordsTheNewSlotsInTheMap() {
    addFivePatients();
    heap.changePriority("Amy", 6);
    assertIndexMapMatchesHeap(heap);
  }

  @Test
  public void increaseKeyToATieWithAChildLeavesTheItemInPlace() {
    addFivePatients();
    heap.changePriority("Amy", 2);
    assertSlots(heap, listOf("Amy", "Bob", "Cal", "Dan", "Eve"), 2, 2, 4, 5, 3);
  }

  @Test
  public void addToAFullArrayDoublesItsCapacity() {
    int capacity = entries(heap).length;
    for (int i = 0; i <= capacity; i++) {  // one more than fits
      heap.add("item" + i, i);
    }
    assertEquals(2 * capacity, entries(heap).length);
  }

  @Test
  public void addToAFullArrayKeepsTheMapCorrect() {
    int capacity = entries(heap).length;
    for (int i = 0; i <= capacity; i++) {  // one more than fits
      heap.add("item" + i, i);
    }
    assertIndexMapMatchesHeap(heap);
  }

  // Forty adds, then a change to every third item's priority.
  private void addAndChangeManyItems() {
    for (int i = 0; i < 40; i++) {
      heap.add("item" + i, (i * 17) % 23);
    }
    for (int i = 0; i < 40; i += 3) {
      heap.changePriority("item" + i, (i * 7) % 29);
    }
  }

  @Test
  public void manyOperationsKeepTheMapCorrect() {
    addAndChangeManyItems();
    assertIndexMapMatchesHeap(heap);
  }

  @Test
  public void manyOperationsServeInPriorityOrder() {
    addAndChangeManyItems();
    List<Integer> served = new ArrayList<>();
    while (heap.size() > 0) {
      served.add(priorityOf(entries(heap)[0]));
      heap.remove();
    }
    List<Integer> sorted = new ArrayList<>(served);
    Collections.sort(sorted);
    assertEquals(sorted, served);
  }

  @Test
  public void addNullThrows() {
    try {
      heap.add(null, 1);
      fail("expected IllegalArgumentException when adding null");
    } catch (IllegalArgumentException e) {
      return;
    }
  }

  @Test
  public void addNullPriorityThrows() {
    try {
      heap.add("Amy", null);
      fail("expected IllegalArgumentException when adding with a null priority");
    } catch (IllegalArgumentException e) {
      return;
    }
  }

  @Test
  public void addDuplicateThrows() {
    heap.add("Amy", 5);
    try {
      heap.add("Amy", 2);
      fail("expected IllegalArgumentException when adding an item twice");
    } catch (IllegalArgumentException e) {
      return;
    }
  }

  @Test
  public void addDuplicateKeepsTheSize() {
    heap.add("Amy", 5);
    try {
      heap.add("Amy", 2);
    } catch (IllegalArgumentException e) {
      // expected
    }
    assertEquals(1, heap.size());
  }

  @Test
  public void changePriorityOfNullThrows() {
    try {
      heap.changePriority(null, 1);
      fail("expected IllegalArgumentException when changing the priority of null");
    } catch (IllegalArgumentException e) {
      return;
    }
  }

  @Test
  public void changePriorityToNullThrows() {
    heap.add("Amy", 5);
    try {
      heap.changePriority("Amy", null);
      fail("expected IllegalArgumentException when the new priority is null");
    } catch (IllegalArgumentException e) {
      return;
    }
  }

  @Test
  public void changePriorityOfAbsentItemThrows() {
    heap.add("Amy", 5);
    try {
      heap.changePriority("Bob", 1);
      fail("expected NoSuchElementException when the item is not in the queue");
    } catch (NoSuchElementException e) {
      return;
    }
  }

  @Test
  public void containsNullThrows() {
    try {
      heap.contains(null);
      fail("expected IllegalArgumentException when asking for null");
    } catch (IllegalArgumentException e) {
      return;
    }
  }

  // Removes every item, highest priority first, and returns them in that order.
  private static List<String> drain(IndexedArrayHeap<String, Integer> heap) {
    List<String> served = new ArrayList<>();
    while (heap.size() > 0) {
      served.add(heap.peek());
      heap.remove();
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

  // Checks the items and priorities in the filled slots, in index order.
  private static void assertSlots(IndexedArrayHeap<String, Integer> heap,
      List<String> items, int... priorities) {
    Object[] entries = entries(heap);
    assertEquals(items.size(), heap.size());
    for (int i = 0; i < heap.size(); i++) {
      assertEquals(items.get(i), itemOf(entries[i]));
      assertEquals(priorities[i], priorityOf(entries[i]));
    }
  }

  // Checks that every item's map entry is the slot it occupies.
  private static void assertIndexMapMatchesHeap(IndexedArrayHeap<String, Integer> heap) {
    Object[] entries = entries(heap);
    Map<String, Integer> index = indexMap(heap);
    assertEquals(heap.size(), index.size());
    for (int i = 0; i < heap.size(); i++) {
      assertEquals(Integer.valueOf(i), index.get(itemOf(entries[i])));
    }
  }

  private static Object[] entries(IndexedArrayHeap<String, Integer> heap) {
    return (Object[]) readField(heap, "heap");
  }

  @SuppressWarnings("unchecked")
  private static Map<String, Integer> indexMap(IndexedArrayHeap<String, Integer> heap) {
    return (Map<String, Integer>) readField(heap, "index");
  }

  private static String itemOf(Object entry) {
    return (String) readField(entry, "item");
  }

  private static int priorityOf(Object entry) {
    return (Integer) readField(entry, "priority");
  }

  // Reads a private field of an object by reflection.
  private static Object readField(Object object, String name) {
    try {
      Field field = object.getClass().getDeclaredField(name);
      field.setAccessible(true);
      return field.get(object);
    } catch (ReflectiveOperationException e) {
      throw new RuntimeException(e);
    }
  }
}
