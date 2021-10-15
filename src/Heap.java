import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.Collectors;

/**
 * A min-heap data structure and a heap-sort static method. Maintaining the heap property means that when
 * comparing parent and child elements, {@code parent.compareTo(child) <= 0} should be true. Hence,
 * the first element to come out of a heap would be the first element in a sorted list of the elements
 * in the heap (barring ties in priority). Same with the second element, the third element, and so on to the last.
 * Written by {@code cspfrederick} and {@code garethhalladay}
 */
public class Heap<E extends Comparable<? super E>> implements IPriorityQueue<E> {
    public static void main(String[] args) {
        Heap<Integer> q = new Heap<>();

        q.add(9);
        q.add(1);
        q.add(5);
        q.debugHeap();
        q.add(3);
        q.add(7);
        q.debugHeap();
        q.add(0);
        q.add(8);

        q.debugHeap();

        System.out.println(q.remove());
        q.debugHeap();
        System.out.println(q.remove());
        q.debugHeap();
        System.out.println(q.remove());

        q.debugHeap();

        q.add(4);
        q.debugHeap();
        q.add(2);
        q.debugHeap();
        q.add(4);
        q.debugHeap();

        // final testing uncomment the following line to get comprehensive testing
        final int hundred_thousand = 100000;
        final int million = 1000000;
        QueueTestProgram.queue_max_size = 10;

        QueueTestProgram.printFailedTests(hundred_thousand,
                                          PriorityQueue::new,
                                          (x) -> new Heap<>());

        // Once finished with the initial comprehensive test, try out these additional testing measures.
        // If this works for you, move on to the big leagues:
        // 1. set QueueTestProgram.queue_max_size to 100.
        // 2. change the hundred_thousand above to a million
        // 3. cross your fingers and run it again!
    }

    /**
     * the underlying priority queue, the name is a nod to the fact that while we
     * are implementing this with an array, the conceptual data structure is a complete binary tree.
     */
    private ArrayList<E> tree;

    /**
     * Initializes a newly created Heap object.
     * <p>
     * Look above... is your variable instantiated? Well, you'd better do that here.
     */
    public Heap() {
        ///*
        tree = new ArrayList<>();
        //*/
    }

    /**
     * Returns the index of the parent node.
     * @param i the current index
     * @return the index of the parent
     */
    static int parent(int i) {
        ///*
        return (i - 1) / 2;
        //*/
        //**return 0;
    }

    /**
     * Returns the index of the left child
     * @param i the current index
     * @return the index of the left child
     */
    static int lchild(int i) {
        ///*
        return i * 2 + 1;
        //*/
        //**return 0;
    }

    /**
     * Returns the index of the right child
     * @param i the current index
     * @return the index of the right child
     */
    static int rchild(int i) {
        ///*
        return lchild(i) + 1;
        //*/
        //**return 0;
    }

    /**
     * Tests if the current index is a leaf in the {@code tree}
     * @param i the current index
     * @return true if the index belongs to a leaf node, false otherwise (an internal node).
     */
    boolean isLeaf(int i) {
        return lchild(i) >= tree.size();
    }

    /**
     * Returns the index of a most priority child of a particular node.
     * If a node has just one child, that is most priority. Otherwise, the left child is
     * most priority if {@code left.compareTo(right) <= 0}, or else the right is most priority.
     * <p>
     * the index of the left child {@link #lchild(int)}
     * the index of the right child {@link #rchild(int)}
     * @param i the current index
     * @return the index of the child with the highest priority
     */
    int priorityChild(int i) {
        ///*
        int l = lchild(i);
        int r = rchild(i);
        if (r >= tree.size()) return l;
        return tree.get(l).compareTo(tree.get(r)) <= 0 ? l : r;
        //*/
        //**return 0;
    }

    /**
     * Takes the element at position {@code i} and swaps it down the tree with its largest child,
     * repeatedly, until the heap property is restored.
     * <p>
     * While the current index does not represent a leaf, compare the child
     * of greatest priority to the current element.
     * If the element in the child node is higher priority, swap it with the parent.
     * Repeat as necessary.
     * @param i the index of the current element
     */
    void swapDown(int i) {
        // YOUR CODE HERE
        ///*
        while (!isLeaf(i)) {
            int c = priorityChild(i);
            if (tree.get(i).compareTo(tree.get(c)) <= 0)
                break;
            Collections.swap(tree, i, c);
            i = c;
        }
        //*/
    }

    /**
     * Performs the swapUp operation to place a newly inserted element
     * in its correct place so that the heap maintains the heap property.
     * <p>
     * While the current element is not the root, compare it with
     * its parent element. If it is of greater priority, then swap the elements.
     * Repeat as necessary.
     * @param i the index of the current element
     */
    void swapUp(int i) {
        // YOUR CODE HERE
        ///*
        while (i > 0) {
            int p = parent(i);
            if (tree.get(p).compareTo(tree.get(i)) <= 0)
                break;
            Collections.swap(tree, i, p);
            i = p;
        }
        //*/
    }

    /**
     * Store the given element in this heap.
     * @param e the element to add
     * @return true (since a Heap is unbounded, and false would mean it didn't have room)
     */
    @Override
    public boolean offer(E e) {
        ///*
        tree.add(e);
        swapUp(tree.size() - 1);
        return true;
        //*/
        //**return false;
    }

    /**
     * Remove and return a most priority element from this heap.
     * @return null if empty, else a most priority element
     */
    @Override
    public E poll() {
        ///*
        if (tree.isEmpty())
            return null;
        int last = tree.size() - 1;
        Collections.swap(tree, 0, last);
        E ret = tree.remove(last);
        swapDown(0);
        return ret;
        //*/
        //**return null;
    }

    /**
     * Return (but don't remove) a most priority element from this heap.
     * @return null if empty, else a most priority element
     */
    @Override
    public E peek() {
        ///*
        if (tree.isEmpty())
            return null;
        return tree.get(0);
        //*/
        //**return null;
    }

    /**
     * Return the number of elements stored in this heap.
     * @return the size of the heap
     */
    @Override
    public int size() {
        ///*
        return tree.size();
        //*/
        //**return 0;
    }

    /**
     * Remove all elements from this heap. After, it is empty.
     */
    @Override
    public void clear() {
        // YOUR CODE HERE
        ///*
        tree.clear();
        //*/
    }

    /**
     * Determine if this heap contains a given element.
     * @param o object to be checked for containment in this queue
     * @return true if it is contained, false otherwise
     */
    @Override
    public boolean contains(Object o) {
        ///*
        return tree.contains(o);
        //*/
        //**return false;
    }

    /**
     * Return a sorted list of the elements of the {@code elems} collection.
     * This method is implemented by the heap sort algorithm: build a heap, extract all elements.
     * @param elems Source collection of elements
     * @param <E> element type to be sorted
     * @return sorted list of original elements
     */
    public static <E extends Comparable<? super E>>
    ArrayList<E> heapSorted(ArrayList<E> elems) {
        ArrayList<E> ret = new ArrayList<>();
        Heap<E> h = new Heap<>(elems);
        while (!h.isEmpty())
            ret.add(h.poll());
        return ret;
    }

    @Override
    public String toString(){
        return String.format("[%s]", String.join(", ",
                tree.stream().map(Object::toString).collect(Collectors.toList())));
    }

    /**
     * A method to help you debug your Heap class. This will loop through your array and print out each
     * node with the corresponding child nodes.
     */
    public void debugHeap(){
        if(size() == 1) {
            System.out.printf("PARENT: %s\n", tree.get(0));
            return;
        }
        int last_internal_node = parent(tree.size() - 1);
        System.out.println("Heap debug:");
        for (int i = 0; i <= last_internal_node; i++ ) {
            int l = lchild(i);
            int r = rchild(i);
            if (r >= tree.size())
                System.out.printf(" PARENT: %s LEFT CHILD: %s\n",
                        tree.get(i), tree.get(l));
            else
                System.out.printf(" PARENT: %s LEFT CHILD: %s RIGHT CHILD: %s\n",
                        tree.get(i), tree.get(l), tree.get(r));
        }
    }

    /**
     * Create a heap from the given elements.
     * <p>
     * Repeated calls to {@code offer} would be a correct implementation.
     * Instead uses an algorithm that runs in time linear to the
     * number of elements. This is a small (but neat!) efficiency.
     * @param elems the elements that will initially populate the heap
     */
    public Heap(Collection<E> elems) {
        tree = new ArrayList<E>(elems);
        int lastInternal = parent(tree.size() - 1);
        for (int i = lastInternal; i >= 0; i--)
            swapDown(i);
    }
}

