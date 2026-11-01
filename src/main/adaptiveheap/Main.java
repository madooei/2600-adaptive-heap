package adaptiveheap;

/**
 * Runs the chapter's trace: five patients are added, a more urgent patient
 * arrives and is seen, and one patient's condition improves while waiting.
 */
public class Main {

  public static void main(String[] args) {
    AdaptivePriorityQueue<String, Integer> er = new IndexedArrayHeap<>();
    er.add("Amy", 1);
    er.add("Bob", 2);
    er.add("Cal", 4);
    er.add("Dan", 5);
    er.add("Eve", 3);
    System.out.println("Next patient: " + er.peek());  // Amy, at level 1

    er.add("Fox", 0);
    System.out.println("Next patient: " + er.peek());  // Fox, at level 0

    er.remove();  // Fox is seen
    System.out.println("Is Fox still waiting? " + er.contains("Fox"));
    System.out.println("Next patient: " + er.peek());  // Amy again

    er.changePriority("Amy", 6);  // Amy's condition improves
    System.out.println("Next patient: " + er.peek());  // Bob, at level 2
    System.out.println("Patients waiting: " + er.size());
  }
}
