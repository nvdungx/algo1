/* *****************************************************************************
 *  Name: Nguyen Van Dung
 *  Date: 2021-08-22
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        if (args.length == 1) {
            int numberItem = Integer.parseInt(args[0]);
            // StdOut.printf("get %d items\r\n", numberItem);
            RandomizedQueue<String> queue = new RandomizedQueue<>();
            while (!StdIn.isEmpty()) {
                String temp = StdIn.readString();
                queue.enqueue(temp);
                // StdOut.println(temp);
                // Reads a sequence of strings from standard input using StdIn.readString();
                // and prints exactly k of them, uniformly at random. Print each item from the sequence at most once.
            }
            while (numberItem != 0) {
                StdOut.println(queue.dequeue());
                numberItem--;
            }
        }
    }
}
