/* *****************************************************************************
 *  Name: Nguyen Van Dung
 *  Date: 2021-08-22
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private int back;
    private int size;
    private int capacity;
    private Item[] data;

    // Iterator. Each iterator must return the items in uniformly random order.
    // The order of two or more iterators to the same randomized queue must be mutually independent;
    // each iterator must maintain its own random order.
    private class RandomQueueIterator implements Iterator<Item> {
        private boolean[] iterFlag;
        private int iterSize;

        public RandomQueueIterator() {
            int cap;
            if (size != 0) cap = back;
            else cap = 1;
            iterFlag = new boolean[cap];
            iterSize = size;
            for (int i = 0; i < cap; i++) {
                if (data[i] != null) iterFlag[i] = true;
                else iterFlag[i] = false;
            }
        }

        public boolean hasNext() {
            return (iterSize != 0);
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
            int current;
            do {
                current = StdRandom.uniform(0, back);
            } while (!iterFlag[current]);
            Item item = data[current];
            iterFlag[current] = false;
            iterSize--;
            return item;
        }
    }

    // construct an empty randomized queue
    public RandomizedQueue() {
        // A randomized queue is similar to a stack or queue,
        // except that the item removed is chosen uniformly at random
        // among items in the data structure
        back = 0;
        size = 0;
        capacity = 1;
        data = (Item[]) new Object[capacity];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return (size == 0);
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    private void resize(int cap) {
        Item[] copy = (Item[]) new Object[cap];
        int j = 0;
        for (int i = 0; i < back; i++) {
            if (data[i] != null) {
                copy[j] = data[i];
                j++;
            }
        }
        back = j;
        data = copy;
        for (int i = back; i < cap; i++) {
            data[i] = null;
        }
        capacity = cap;
    }

    // add the item
    public void enqueue(Item item) {
        // Throw an IllegalArgumentException if the client calls enqueue() with a null argument.
        if (item == null) throw new IllegalArgumentException("Error: can not add null item");
        if (back == capacity) {
            // resize: double the array capacity
            if (size == capacity)
                capacity *= 2;
            else if (size == 0)
                capacity = 1;
            else if (size <= capacity / 4)
                capacity /= 2;
            resize(capacity);
            data[back++] = item;
        }
        else {
            data[back++] = item;
        }
        size++;
    }

    // remove and return a random item
    public Item dequeue() {
        // Throw a java.util.NoSuchElementException if the client calls either sample()
        // or dequeue() when the randomized queue is empty.
        if (isEmpty()) throw new NoSuchElementException("Error: can not dequeue empty queue");
        if (size <= capacity / 4) {
            if (size == 0)
                capacity = 1;
            else capacity /= 2;
            resize(capacity);
        }
        int index;
        do {
            index = StdRandom.uniform(0, back);
        } while (data[index] == null);
        Item item = data[index];
        data[index] = null;
        size--;
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        // Throw a java.util.NoSuchElementException if the client calls either sample()
        // or dequeue() when the randomized queue is empty.
        if (isEmpty()) throw new NoSuchElementException("Error: can not sample empty queue");
        int index;
        do {
            index = StdRandom.uniform(0, back);
        } while (data[index] == null);
        return data[index];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomQueueIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> ranQueue = new RandomizedQueue<>();
        for (int i = 0; i < 1000; i++) {
            ranQueue.enqueue(i);
        }
        StdOut.println();
        StdOut.printf("deque empty status: %b - %d\r\n", ranQueue.isEmpty(), ranQueue.size());
        for (int i = 0; i < 1000; i++) {
            StdOut.printf("%d ", ranQueue.dequeue());
        }
        StdOut.println();
        StdOut.printf("deque empty status: %b - %d\r\n", ranQueue.isEmpty(), ranQueue.size());
        // ranQueue.enqueue(12);
        // StdOut.printf("deque empty status: %b - %d\r\n", ranQueue.isEmpty(), ranQueue.size());
        // StdOut.println(ranQueue.dequeue());
        // StdOut.printf("deque empty status: %b - %d\r\n", ranQueue.isEmpty(), ranQueue.size());
        // ranQueue.enqueue(34);
        // ranQueue.enqueue(56);
        // StdOut.printf("deque empty status: %b - %d\r\n", ranQueue.isEmpty(), ranQueue.size());
        // StdOut.println(ranQueue.dequeue());
        // StdOut.printf("deque empty status: %b - %d\r\n", ranQueue.isEmpty(), ranQueue.size());
        // StdOut.println(ranQueue.dequeue());
        // StdOut.printf("deque empty status: %b - %d\r\n", ranQueue.isEmpty(), ranQueue.size());
        // try {
        //     StdOut.printf("deque remove first: %d\r\n", ranQueue.dequeue());
        // }
        // catch (IllegalArgumentException e) {
        //     StdOut.printf("%s\r\n", e.toString());
        // }
        // try {
        //     StdOut.printf("deque remove first: %d\r\n", ranQueue.sample());
        // }
        // catch (IllegalArgumentException e) {
        //     StdOut.printf("%s\r\n", e.toString());
        // }
        // try {
        //     Iterator<Integer> iter = ranQueue.iterator();
        //     iter.remove();
        // }
        // catch (UnsupportedOperationException e) {
        //     StdOut.printf("%s\r\n", e.toString());
        // }
        // try {
        //     Iterator<Integer> iter = ranQueue.iterator();
        //     iter.next();
        // }
        // catch (NoSuchElementException e) {
        //     StdOut.printf("%s\r\n", e.toString());
        // }
        // for (int i = 0; i < 20; i++) {
        //     ranQueue.enqueue(i);
        // }
        // printQueue(ranQueue);
        // printQueue(ranQueue);
        // for (int i = 0; i < 10; i++) {
        //     StdOut.printf("%d, ", ranQueue.sample());
        // }
        // StdOut.printf("\r\n");
        // printQueue(ranQueue);
        // for (int i = 0; i < 10; i++) {
        //     StdOut.printf("%d, ", ranQueue.dequeue());
        // }
        // StdOut.printf("\r\n");
        // printQueue(ranQueue);
        // for (int i = 0; i < 10; i++) {
        //     StdOut.printf("%d, ", ranQueue.dequeue());
        // }
    }

    private static void printQueue(RandomizedQueue<Integer> que) {
        Iterator<Integer> iter = que.iterator();
        while (iter.hasNext()) {
            StdOut.printf("%d, ", iter.next());
        }
        StdOut.printf("\r\n");
    }
}
