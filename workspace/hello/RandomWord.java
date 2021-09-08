/* *****************************************************************************
 *  Name:              Nguyen Van Dung
 *  Coursera User ID:
 *  Last modified:     August 18, 2021
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        String chimp = "";
        String temp;
        int counter = 1;
        while (!StdIn.isEmpty()) {
            temp = StdIn.readString();
            if (temp.equals("!")) break;
            if (StdRandom.bernoulli(1.0 / counter)) {
                chimp = temp;
            }
            // StdOut.printf("%d:%s\r\n", counter, temp);
            counter++;
        }
        StdOut.println(chimp);
    }
}
