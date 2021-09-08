/* *****************************************************************************
 *  Name: Nguyen Van Dung
 *  Date: 2021-08-22
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private int size;
    // front>back>
    private Node front = null;
    private Node back = null;

    private class Node {
        Item item;
        Node next;
        Node pre;

        public Node(Item inputItem, Node preNode, Node nextNode) {
            item = inputItem;
            pre = preNode;
            next = nextNode;
        }
    }

    private class QueueIterator implements Iterator<Item> {
        private Node current = front;

        public boolean hasNext() {
            return (current != null);
        }

        public void remove() {
            // Throw an UnsupportedOperationException if the client calls the remove() method in the iterator.
            throw new UnsupportedOperationException(
                    "Error: iterator does not support remove operation");
        }

        public Item next() {
            // Throw a java.util.NoSuchElementException if the client calls the
            // next() method in the iterator when there are no more items to return.
            if (!hasNext()) throw new NoSuchElementException("Error: no more items available");
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    // construct an empty deque
    public Deque() {
        // Performance requirements.  Your deque implementation must support each deque
        // operation (including construction) in constant worst-case time.
        // A deque containing n items must use at most 48n + 192 bytes of memory.
        // Additionally, your iterator implementation must support
        // each operation (including construction) in constant worst-case time.
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return (front == null);
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        // Throw an IllegalArgumentException if the client calls either addFirst() or addLast() with a null argument.
        if (item == null) throw new IllegalArgumentException("Error: can not add null item");
        // get old front of queue
        // Node oldFront = front;
        // update new front node
        if (isEmpty()) {
            // first item in queue
            front = new Node(item, null, null);
            back = front;
        }
        // preNode of front always null
        else {
            front.pre = new Node(item, null, front);
            front = front.pre;
        }
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException("Error: can not add null item");
        if (isEmpty()) {
            // back, front = new item
            back = new Node(item, null, null);
            front = back;
        }
        else {
            // next back = new item
            back.next = new Node(item, back, null);
            back = back.next;
        }
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        // Throw a java.util.NoSuchElementException if the client calls either removeFirst() or removeLast when the deque is empty.
        if (isEmpty()) throw new NoSuchElementException("Error: can not dequeue empty queue");
        Item item = front.item;
        front = front.next;
        if (front == null) back = null;
        else front.pre = null;
        size--;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Error: can not dequeue empty queue");
        Item item = back.item;
        back = back.pre;
        if (back == null) front = null;
        else back.next = null;
        size--;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new QueueIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> deque = new Deque<>();
        StdOut.printf("deque empty status: %b - %d\r\n", deque.isEmpty(), deque.size());
        try {
            StdOut.printf("deque remove first: %d\r\n", deque.removeFirst());
        }
        catch (NoSuchElementException e) {
            StdOut.printf("%s\r\n", e.toString());
        }
        try {
            StdOut.printf("deque remove first: %d\r\n", deque.removeLast());
        }
        catch (NoSuchElementException e) {
            StdOut.printf("%s\r\n", e.toString());
        }
        try {
            Iterator<String> iter = deque.iterator();
            iter.remove();
        }
        catch (UnsupportedOperationException e) {
            StdOut.printf("%s\r\n", e.toString());
        }
        try {
            Iterator<String> iter = deque.iterator();
            iter.next();
        }
        catch (NoSuchElementException e) {
            StdOut.printf("%s\r\n", e.toString());
        }
        printDeque(deque);
        deque.addFirst("123");
        printDeque(deque);
        deque.addFirst("456");
        printDeque(deque);
        deque.addLast("789");
        deque.addLast("abc");
        printDeque(deque);
        StdOut.printf("remove %s\r\n", deque.removeFirst());
        StdOut.printf("remove %s\r\n", deque.removeFirst());
        StdOut.printf("remove %s\r\n", deque.removeLast());
        StdOut.printf("remove %s\r\n", deque.removeLast());

        deque.addLast("789");
        deque.addLast("abc");
        StdOut.printf("remove %s\r\n", deque.removeFirst());
        StdOut.printf("remove %s\r\n", deque.removeFirst());
        try {
            StdOut.printf("remove %s\r\n", deque.removeLast());
        }
        catch (IllegalArgumentException e) {
            StdOut.printf("%s\r\n", e.toString());
        }
    }

    private static void printDeque(Deque<String> deque) {
        int index = 1;
        for (String item : deque) {
            StdOut.printf("item[%d]: %s\r\n", index, item);
            index++;
        }
    }

}
