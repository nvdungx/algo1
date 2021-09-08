/* *****************************************************************************
 *  Name: Nguyen Van Dung
 *  Date: 2021-08-24
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.Comparator;

public class BruteCollinearPoints {
    private final ArrayList<LineSegment> lines;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        // Throw an IllegalArgumentException if the argument to the constructor is null
        if (points == null)
            throw new IllegalArgumentException("ERROR: input point array can not be null");
        lines = new ArrayList<LineSegment>();

        // The order of growth of the running time of your program should be n4 in the worst case
        // it should use space proportional to n plus the number of line segments
        Point[] pointRef = new Point[points.length];
        for (int i = 0; i < points.length; i++) pointRef[i] = points[i];
        StdRandom.shuffle(pointRef);
        sortCheck(pointRef, 0, pointRef.length - 1);
        // examines 4 points at a time and checks whether they all lie on the same line segment,
        // returning all such line segments
        for (int p = 0; p < points.length; p++) {
            Comparator<Point> cmp = points[p].slopeOrder();
            for (int q = p + 1; q < points.length; q++) {
                for (int r = q + 1; r < points.length; r++) {
                    for (int s = r + 1; s < points.length; s++) {
                        // whether the 4 points p, q, r, and s are collinear,
                        // check whether the three slopes between p and q, between p and r, and between p and s are all equal.
                        if ((cmp.compare(points[q], points[r]) == 0) && (
                                cmp.compare(points[q], points[s]) == 0)) {
                            ArrayList<Point> temp = new ArrayList<>();
                            temp.add(points[p]);
                            temp.add(points[q]);
                            temp.add(points[r]);
                            temp.add(points[s]);
                            temp.sort(Point::compareTo);
                            lines.add(new LineSegment(temp.get(0), temp.get(temp.size() - 1)));
                        }
                    }
                }
            }
        }
    }

    private int medianOf3(Point[] points, int lo, int mid, int hi) {
        // if any point in the array is null
        if ((points[lo] == null) || (points[mid] == null) || (points[hi] == null))
            throw new IllegalArgumentException("ERROR: points elemement can not be null");
        if (points[lo].compareTo(points[mid]) > 0) {
            // lo > mid > hi
            if (points[mid].compareTo(points[hi]) > 0) return mid;
                // lo > hi > mid
            else if (points[lo].compareTo(points[hi]) > 0) return hi;
                // hi > lo > mid
            else return lo;
        }
        else {
            // lo < mid < hi
            if (points[mid].compareTo(points[hi]) < 0) return mid;
                // lo < hi < mid
            else if (points[lo].compareTo(points[hi]) > 0) return hi;
                // hi < lo < mid
            else return lo;
        }
    }

    private void exch(Point[] points, int i, int j) {
        // if any point in the array is null
        if ((points[i] == null) || (points[j] == null))
            throw new IllegalArgumentException("ERROR: points elemement can not be null");
        Point temp = points[i];
        points[i] = points[j];
        points[j] = temp;
    }

    private void sortCheck(Point[] points, int lo, int hi) {
        if (hi <= lo) return;
        int med = medianOf3(points, lo, lo + (hi - lo) / 2, hi);
        exch(points, lo, med);
        int lt = lo, i = lo + 1, gt = hi;
        while (i <= gt) {
            if ((points[i] == null) || (points[lt] == null))
                throw new IllegalArgumentException("ERROR: points elemement can not be null");
            if (points[i].compareTo(points[lt]) < 0) exch(points, lt++, i++);
            else if (points[i].compareTo(points[lt]) > 0) exch(points, i, gt--);
                // if the argument to the constructor contains a repeated point
            else throw new IllegalArgumentException("ERROR: points elemement can not be the same");
        }
        sortCheck(points, lo, lt - 1);
        sortCheck(points, gt + 1, hi);
    }

    // the number of line segments
    public int numberOfSegments() {
        return lines.size();
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] result = new LineSegment[lines.size()];
        lines.toArray(result);
        return result;
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
