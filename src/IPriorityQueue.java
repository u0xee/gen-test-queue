import java.util.NoSuchElementException;

/**
 * A priority queue is different from a regular queue because it respects priority among the elements.
 *
 * The head of a priority queue is an element of greatest priority among all elements within the queue.
 * The queue retrieval operations poll, remove, peek, and element access the element at the head of the queue.
 */
public interface IPriorityQueue<E extends Comparable<? super E>> extends IQueue<E> {
    /**
     * Retrieves and removes an element of greatest priority, which is
     * the head of the priority queue.
     *
     * For example:
     * A queue that prioritizes Strings by max length would return the longest String.
     * A queue that prioritizes Integers by min value would return the minimum number.
     * @return the element of greatest priority, or null if this queue is empty
     */
    E poll();

    /**
     * Retrieves, but does not remove, an element of greatest priority, which
     * is the head of the priority queue.
     * @return the element of greatest priority, or null if this queue is empty
     */
    E peek();

    /**
     * Inserts the specified element into this queue.
     * @param e the element to add
     * @return true if the element was added to this priority queue, else false
     */
    boolean offer(E e);

    // The add method is similar to the offer method except for exception handling.
    // Instead of returning false, this method does not have a return value, instead
    // it should throw a new IllegalStateException.
    default void add(E item){
        boolean result = offer(item);
        if(!result) throw new IllegalStateException("Queue is full");
    }

    // The remove method is similar to the poll method except for exception handling.
    // Instead of returning null, this method should throw a new NoSuchElementException.
    default E remove(){
        E result = poll();
        if (result == null) throw new NoSuchElementException("Queue is empty.");
        return result;
    }

    // The element method is similar to the peek method except for exception handling.
    // Instead of returning a null value, this method should throw a NoSuchElementException.
    default E element(){
        E result = peek();
        if (result == null) throw new NoSuchElementException("Queue is empty.");
        return result;
    }

    // This method should utilize the size method to return the correct result.
    default boolean isEmpty() {
        return size() == 0;
    }
}
