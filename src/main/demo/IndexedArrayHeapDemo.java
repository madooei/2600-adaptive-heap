package demo;

import adaptiveheap.AdaptivePriorityQueue;
import adaptiveheap.IndexedArrayHeap;

/** A demo of IndexedArrayHeap on the chapter's emergency room trace. */
public final class IndexedArrayHeapDemo {

  private IndexedArrayHeapDemo() {
    // This class should not be instantiated!
  }

  public static void main(String[] args) {
    AdaptivePriorityQueue<String, Integer> er = new IndexedArrayHeap<>();

    // Five patients arrive. A smaller level is more urgent.
    er.add("Amy", 1);
    er.add("Bob", 2);
    er.add("Cal", 4);
    er.add("Dan", 5);
    er.add("Eve", 3);

    // Expect Amy, at level 1.
    System.out.println("next patient: " + er.peek());

    // A more urgent patient arrives. Expect Fox, at level 0.
    er.add("Fox", 0);
    System.out.println("next patient: " + er.peek());

    // Fox is seen. Expect false, and then Amy again.
    er.remove();
    System.out.println("is Fox still waiting? " + er.contains("Fox"));
    System.out.println("next patient: " + er.peek());

    // Amy's condition improves while she waits. Expect Bob, at level 2.
    er.changePriority("Amy", 6);
    System.out.println("next patient: " + er.peek());

    // Expect 5.
    System.out.println("patients waiting: " + er.size());
  }
}
