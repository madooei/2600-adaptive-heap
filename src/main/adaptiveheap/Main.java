package adaptiveheap;

/**
 * Runs the chapter's trace: four patients are added, one of them becomes more
 * urgent while waiting, and the most urgent patient is seen.
 */
public class Main {

  public static void main(String[] args) {
    AdaptivePriorityQueue<String> er = new IndexedHeap<>();
    er.add("Amy", 5);
    er.add("Bob", 8);
    er.add("Cal", 3);
    er.add("Dan", 6);
    System.out.println("Next patient: " + er.peek());  // Cal, at level 3

    er.changePriority("Bob", 2);  // Bob gets worse while he waits
    System.out.println("Next patient: " + er.peek());  // Bob, now at level 2

    er.remove();  // Bob is seen
    System.out.println("Is Bob still waiting? " + er.contains("Bob"));
    System.out.println("Next patient: " + er.peek());  // Cal again
    System.out.println("Patients waiting: " + er.size());
  }
}
