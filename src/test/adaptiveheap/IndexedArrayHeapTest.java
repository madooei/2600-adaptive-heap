package adaptiveheap;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import map.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/** Runs the AdaptivePriorityQueue contract suite against IndexedArrayHeap. */
public class IndexedArrayHeapTest extends AdaptivePriorityQueueTest {

  @Override
  protected AdaptivePriorityQueue<String, Integer> createQueue() {
    return new IndexedArrayHeap<>();
  }

  @Test
  public void removeRecordsTheRootSlotOfAnItemThatDoesNotSink() {
    IndexedArrayHeap<String, Integer> heap = new IndexedArrayHeap<>();
    heap.add("Amy", 1);
    heap.add("Bob", 2);
    heap.remove();
    assertIndexMapMatchesHeap(heap);
  }

  @Test
  public void fivePatientsFillTheSlotsInOrder() {
    IndexedArrayHeap<String, Integer> heap = fivePatients();
    assertSlots(heap, listOf("Amy", "Bob", "Cal", "Dan", "Eve"), 1, 2, 4, 5, 3);
  }

  @Test
  public void fivePatientsAreRecordedInTheMap() {
    IndexedArrayHeap<String, Integer> heap = fivePatients();
    assertIndexMapMatchesHeap(heap);
  }

  @Test
  public void addSwimsTheNewItem() {
    IndexedArrayHeap<String, Integer> heap = fivePatients();
    heap.add("Fox", 0);
    assertSlots(heap, listOf("Fox", "Bob", "Amy", "Dan", "Eve", "Cal"), 0, 2, 1, 5, 3, 4);
  }

  @Test
  public void addRecordsTheNewSlotsInTheMap() {
    IndexedArrayHeap<String, Integer> heap = fivePatients();
    heap.add("Fox", 0);
    assertIndexMapMatchesHeap(heap);
  }

  @Test
  public void removeSinksTheMovedItem() {
    IndexedArrayHeap<String, Integer> heap = fivePatients();
    heap.add("Fox", 0);
    heap.remove();
    assertSlots(heap, listOf("Amy", "Bob", "Cal", "Dan", "Eve"), 1, 2, 4, 5, 3);
  }

  @Test
  public void removeRecordsTheNewSlotsInTheMap() {
    IndexedArrayHeap<String, Integer> heap = fivePatients();
    heap.add("Fox", 0);
    heap.remove();
    assertIndexMapMatchesHeap(heap);
  }

  @Test
  public void removeClearsTheVacatedSlot() {
    IndexedArrayHeap<String, Integer> heap = fivePatients();
    heap.remove();
    assertNull(entries(heap)[4]);
  }

  @Test
  public void decreaseKeySwimsTheItem() {
    IndexedArrayHeap<String, Integer> heap = fivePatients();
    heap.changePriority("Dan", 1);
    assertSlots(heap, listOf("Amy", "Dan", "Cal", "Bob", "Eve"), 1, 1, 4, 2, 3);
  }

  @Test
  public void decreaseKeyRecordsTheNewSlotsInTheMap() {
    IndexedArrayHeap<String, Integer> heap = fivePatients();
    heap.changePriority("Dan", 1);
    assertIndexMapMatchesHeap(heap);
  }

  @Test
  public void increaseKeySinksTheItem() {
    IndexedArrayHeap<String, Integer> heap = fivePatients();
    heap.changePriority("Amy", 6);
    assertSlots(heap, listOf("Bob", "Eve", "Cal", "Dan", "Amy"), 2, 3, 4, 5, 6);
  }

  @Test
  public void increaseKeyRecordsTheNewSlotsInTheMap() {
    IndexedArrayHeap<String, Integer> heap = fivePatients();
    heap.changePriority("Amy", 6);
    assertIndexMapMatchesHeap(heap);
  }

  @Test
  public void increaseKeyToATieWithAChildLeavesTheItemInPlace() {
    IndexedArrayHeap<String, Integer> heap = fivePatients();
    heap.changePriority("Amy", 2);
    assertSlots(heap, listOf("Amy", "Bob", "Cal", "Dan", "Eve"), 2, 2, 4, 5, 3);
  }

  @Test
  public void addToAFullArrayDoublesItsCapacity() {
    IndexedArrayHeap<String, Integer> heap = new IndexedArrayHeap<>();
    int capacity = entries(heap).length;
    for (int i = 0; i <= capacity; i++) {  // one more than fits
      heap.add("item" + i, i);
    }
    assertEquals(2 * capacity, entries(heap).length);
  }

  @Test
  public void addToAFullArrayKeepsTheMapCorrect() {
    IndexedArrayHeap<String, Integer> heap = new IndexedArrayHeap<>();
    int capacity = entries(heap).length;
    for (int i = 0; i <= capacity; i++) {  // one more than fits
      heap.add("item" + i, i);
    }
    assertIndexMapMatchesHeap(heap);
  }

  @Test
  public void manyOperationsKeepTheMapCorrect() {
    IndexedArrayHeap<String, Integer> heap = manyChangedItems();
    assertIndexMapMatchesHeap(heap);
  }

  @Test
  public void manyOperationsServeInPriorityOrder() {
    IndexedArrayHeap<String, Integer> heap = manyChangedItems();
    List<Integer> served = new ArrayList<>();
    while (heap.size() > 0) {
      served.add(priorityOf(entries(heap)[0]));
      heap.remove();
    }
    List<Integer> sorted = new ArrayList<>(served);
    Collections.sort(sorted);
    assertEquals(sorted, served);
  }

  // The five patients the notes start from: Amy 1, Bob 2, Cal 4, Dan 5, Eve 3.
  private static IndexedArrayHeap<String, Integer> fivePatients() {
    IndexedArrayHeap<String, Integer> heap = new IndexedArrayHeap<>();
    heap.add("Amy", 1);
    heap.add("Bob", 2);
    heap.add("Cal", 4);
    heap.add("Dan", 5);
    heap.add("Eve", 3);
    return heap;
  }

  // Forty adds, then a change to every third item's priority.
  private static IndexedArrayHeap<String, Integer> manyChangedItems() {
    IndexedArrayHeap<String, Integer> heap = new IndexedArrayHeap<>();
    for (int i = 0; i < 40; i++) {
      heap.add("item" + i, (i * 17) % 23);
    }
    for (int i = 0; i < 40; i += 3) {
      heap.changePriority("item" + i, (i * 7) % 29);
    }
    return heap;
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
