package map;

import java.util.Iterator;
import java.util.NoSuchElementException;
import stack.LinkedStack;
import stack.Stack;

/**
 * An AVL tree implementation of the Map ADT.
 *
 * @param <K> the type of keys in this map.
 * @param <V> the type of values in this map.
 */
public class TreeMap<K extends Comparable<K>, V> implements Map<K, V> {

  private Node<K, V> root;
  private int size;

  private static class Node<K, V> implements Map.Entry<K, V> {
    K key;
    V value;
    Node<K, V> left;
    Node<K, V> right;
    int height;

    Node(K key, V value) {
      this.key = key;
      this.value = value;
      height = 0;  // a new node is a leaf
    }

    @Override
    public K key() {
      return key;
    }

    @Override
    public V value() {
      return value;
    }
  }

  public TreeMap() {
    root = null;
    size = 0;
  }

  @Override
  public V get(K key) {
    if (key == null) {
      throw new IllegalArgumentException();
    }
    Node<K, V> node = find(root, key);
    if (node == null) {
      return null;
    }
    return node.value;
  }

  @Override
  public boolean containsKey(K key) {
    if (key == null) {
      throw new IllegalArgumentException();
    }
    return find(root, key) != null;
  }

  // Returns the node holding key in the subtree rooted at node, or null if
  // key is not there.
  private Node<K, V> find(Node<K, V> node, K key) {
    if (node == null) {
      return null;
    }

    int cmp = key.compareTo(node.key);
    if (cmp < 0) {
      return find(node.left, key);
    } else if (cmp > 0) {
      return find(node.right, key);
    } else {
      return node;
    }
  }

  @Override
  public boolean put(K key, V value) {
    if (key == null || value == null) {
      throw new IllegalArgumentException();
    }
    boolean isNew = !containsKey(key);
    root = put(root, key, value);
    if (isNew) {
      size++;
    }
    return isNew;
  }

  // Puts key and value in the subtree rooted at node and returns that
  // subtree's new root. Replaces the value if key is already there.
  private Node<K, V> put(Node<K, V> node, K key, V value) {
    if (node == null) {
      return new Node<>(key, value);
    }

    int cmp = key.compareTo(node.key);
    if (cmp < 0) {
      node.left = put(node.left, key, value);
    } else if (cmp > 0) {
      node.right = put(node.right, key, value);
    } else {
      node.value = value;  // no new node, so the shape did not change
      return node;
    }
    updateHeight(node);
    return rebalance(node);
  }

  @Override
  public boolean remove(K key) {
    if (key == null) {
      throw new IllegalArgumentException();
    }
    if (!containsKey(key)) {
      return false;
    }
    root = remove(root, key);
    size--;
    return true;
  }

  // Removes key from the subtree rooted at node and returns that subtree's
  // new root. Assumes key is in the subtree.
  private Node<K, V> remove(Node<K, V> node, K key) {
    int cmp = key.compareTo(node.key);
    if (cmp < 0) {
      node.left = remove(node.left, key);
    } else if (cmp > 0) {
      node.right = remove(node.right, key);
    } else if (node.left == null) {
      return node.right;
    } else if (node.right == null) {
      return node.left;
    } else {
      Node<K, V> successor = minNode(node.right);
      node.key = successor.key;
      node.value = successor.value;
      node.right = remove(node.right, successor.key);
    }
    updateHeight(node);
    return rebalance(node);
  }

  // Assumes node is not null. Returns the node with the smallest key in the
  // subtree rooted at node.
  private Node<K, V> minNode(Node<K, V> node) {
    while (node.left != null) {
      node = node.left;
    }
    return node;
  }

  @Override
  public int size() {
    return size;
  }

  // Returns the cached height of node. The empty tree has height -1.
  private int height(Node<K, V> node) {
    if (node == null) {
      return -1;
    }
    return node.height;
  }

  // Assumes node is not null.
  private int balanceFactor(Node<K, V> node) {
    return height(node.left) - height(node.right);
  }

  // Assumes node is not null and its children's cached heights are correct.
  private void updateHeight(Node<K, V> node) {
    node.height = 1 + Math.max(height(node.left), height(node.right));
  }

  // Assumes node is not null, its children are balanced, and its cached
  // height is correct. Returns the root of the subtree after rebalancing.
  private Node<K, V> rebalance(Node<K, V> node) {
    int bf = balanceFactor(node);
    if (bf > 1) {
      if (balanceFactor(node.left) < 0) {
        return leftRightRotation(node);
      }
      return rightRotation(node);
    }
    if (bf < -1) {
      if (balanceFactor(node.right) > 0) {
        return rightLeftRotation(node);
      }
      return leftRotation(node);
    }
    return node;
  }

  // Assumes root.left is not null. Returns the new root of the subtree.
  private Node<K, V> rightRotation(Node<K, V> root) {
    Node<K, V> child = root.left;
    root.left = child.right;
    child.right = root;
    updateHeight(root);  // root is now below child
    updateHeight(child);
    return child;
  }

  // Assumes root.right is not null. Returns the new root of the subtree.
  private Node<K, V> leftRotation(Node<K, V> root) {
    Node<K, V> child = root.right;
    root.right = child.left;
    child.left = root;
    updateHeight(root);  // root is now below child
    updateHeight(child);
    return child;
  }

  // Assumes root.left.right is not null. Returns the new root of the subtree.
  private Node<K, V> leftRightRotation(Node<K, V> root) {
    root.left = leftRotation(root.left);
    return rightRotation(root);
  }

  // Assumes root.right.left is not null. Returns the new root of the subtree.
  private Node<K, V> rightLeftRotation(Node<K, V> root) {
    root.right = rightRotation(root.right);
    return leftRotation(root);
  }

  @Override
  public Iterator<K> iterator() {
    return new KeyIterator();
  }

  @Override
  public Iterable<Map.Entry<K, V>> entries() {
    return new Entries();
  }

  // Walks the tree in-order, returning each node's key.
  private class KeyIterator implements Iterator<K> {
    private Iterator<Map.Entry<K, V>> nodes = new EntryIterator();

    @Override
    public boolean hasNext() {
      return nodes.hasNext();
    }

    @Override
    public K next() {
      return nodes.next().key();
    }
  }

  // The Iterable that entries() returns. Each call to iterator() starts a new traversal.
  private class Entries implements Iterable<Map.Entry<K, V>> {
    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
      return new EntryIterator();
    }
  }

  // Walks the tree in-order, returning each node as a read-only entry. Keeps
  // the nodes still to visit on a stack.
  private class EntryIterator implements Iterator<Map.Entry<K, V>> {
    private Stack<Node<K, V>> stack = new LinkedStack<>();

    EntryIterator() {
      pushLeftSpine(root);
    }

    // Pushes node and every node on the path of left children below it.
    private void pushLeftSpine(Node<K, V> node) {
      while (node != null) {
        stack.push(node);
        node = node.left;
      }
    }

    @Override
    public boolean hasNext() {
      return !stack.isEmpty();
    }

    @Override
    public Map.Entry<K, V> next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      Node<K, V> node = stack.top();
      stack.pop();
      pushLeftSpine(node.right);
      return node;
    }
  }
}
