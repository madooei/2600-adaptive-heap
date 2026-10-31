# The Adaptive Priority Queue

An adaptive priority queue lets an item's priority change while the item waits in the queue. `IndexedHeap` implements it as a binary heap with an index map beside it. The map sends each item to the slot it occupies, so `changePriority` and `contains` find an item with one lookup instead of a scan. A JUnit suite checks the contract, then reads the heap array and the index map through reflection to check that they agree after every operation.

## Prerequisites

- JDK 17+
- The JUnit jar is already vendored in `lib/`; there is nothing to download.

## Repository layout

```plaintext
code/
  README.md
  .gitignore
  lib/
    junit-platform-console-standalone-6.1.0.jar
  src/
    main/
      adaptiveheap/
        AdaptivePriorityQueue.java      # the AdaptivePriorityQueue ADT
        IndexedHeap.java                # a binary heap with an index map
        Main.java                       # demo entry point (the chapter's trace)
      map/
        Map.java                        # the Map ADT
        TreeMap.java                    # the AVL tree map from the TreeMap chapter
      stack/
        Stack.java                      # the Stack ADT, used by the map's iterator
        LinkedStack.java                # linked implementation of Stack
    test/
      adaptiveheap/
        IndexedHeapTest.java            # the contract, plus the heap and map by reflection
  scripts/
    run.sh                              # compile and run the demo (adaptiveheap.Main)
    test.sh                             # compile and run the JUnit tests
```

## How to compile and run

- `scripts/test.sh` — compiles everything and runs the full JUnit suite.
- `scripts/test.sh adaptiveheap.IndexedHeapTest` — compiles everything and runs only that test class.
- `scripts/run.sh` — compiles everything and runs the `Main` demo.

## What's here

- `adaptiveheap.AdaptivePriorityQueue<T>` — the ADT: `add(item, priority)`, `peek`, `remove`, `changePriority`, `contains`, and `size`. A smaller priority number means a higher priority, and the items must be distinct.
- `adaptiveheap.IndexedHeap<T>` — the binary heap from the binary heap chapter, storing an entry (an item and its priority) in each slot, with a `TreeMap` from each item to its slot. `swap` keeps the map correct for `swim` and `sink`, and `add` and `remove` update it at their ends.
- `adaptiveheap.Main` — runs the chapter's trace: four adds, a `changePriority`, and a `remove`.
- `map.Map<K, V>`, `map.TreeMap<K, V>`, `stack.Stack<T>`, and `stack.LinkedStack<T>` — the map from the TreeMap chapter and the stack its iterator uses, copied unchanged so this code compiles and runs on its own.
- `adaptiveheap.IndexedHeapTest` — tests the contract through the public methods, then checks the slots the notes draw and the index map by reading them through reflection.
