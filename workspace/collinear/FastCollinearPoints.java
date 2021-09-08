/* *****************************************************************************
 *  Name: Nguyen Van Dung
 *  Date: 2021-08-24
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.Comparator;

public class FastCollinearPoints {
    private final ArrayList<LineSegment> lines;

    // finds all line segments containing 4 points
    public FastCollinearPoints(Point[] points) {
        // Throw an IllegalArgumentException if the argument to the constructor is null
        if (points == null)
            throw new IllegalArgumentException("ERROR: input point array can not be null");
        lines = new ArrayList<LineSegment>();
        ArrayList<Point> endpoint = new ArrayList<>();
        ArrayList<Double> endpointSlop = new ArrayList<>();
        Point[] pointRef = new Point[points.length];
        for (int i = 0; i < points.length; i++) pointRef[i] = points[i];

        StdRandom.shuffle(pointRef);
        sortCheck(pointRef, 0, pointRef.length - 1);

        // order of growth of the running time of your program should be n2 log n in the worst case
        // it should use space proportional to n plus the number of line segments
        // O(n)
        // Given a point p, the following method determines
        // whether p participates in a set of [4 or more] collinear points.
        for (int i = 0; i < pointRef.length; i++) {
            // p as the origin
            Point p = points[i];
            Comparator<Point> originSlop = p.slopeOrder();
            // StdOut.printf("ORIGIN %s\r\n", p.toString());
            // stack for lo and hi index of partition
            Stack<Integer> loStack = new Stack<>();
            Stack<Integer> hiStack = new Stack<>();
            loStack.push(0);
            hiStack.push(pointRef.length - 1);

            while (!hiStack.isEmpty()) {
                // O(nlogn) selection
                // point root has n-1 slop val to other point
                int lo = loStack.pop(), hi = hiStack.pop();
                // if number of elements for partion < 3 (not able to create a 4 point collinear, skip
                if (hi <= lo + 2) continue;
                // get median point
                // int med = medianOf3(points, lo, lo + (hi - lo) / 2, hi);
                // exch(points, lo, med);
                // init less than, greater than index
                // has to check lo point incase of point[lo] == root p >
                if (pointRef[lo].compareTo(p) == 0) lo++;
                int lt = lo, gt = hi;
                // j counter
                int j = lt + 1;
                ArrayList<Point> collinear = new ArrayList<>();
                // slop(i,lt) as pivot
                // store pivot point
                collinear.add(pointRef[lo]);
                // StdOut.printf("origin:%s pivot:%s slop:%f\r\n", p.toString(), pointRef[lo].toString(),
                //               p.slopeTo(pointRef[lo]));
                while (j <= gt) {
                    // slop(i,j) < slop(i,lt)
                    if (originSlop.compare(pointRef[j], pointRef[lt]) < 0)
                        exch(pointRef, lt++, j++);
                        // slop(i,j) > slop(i,lt)
                    else if (originSlop.compare(pointRef[j], pointRef[lt]) > 0)
                        exch(pointRef, gt--, j);
                        // slop(i,j) == slop(i, lt)
                    else {
                        // store point j
                        // StdOut.printf("origin:%s pivot:%s slop:%f\r\n", p.toString(),
                        //               pointRef[j].toString(), p.slopeTo(pointRef[j]));
                        collinear.add(pointRef[j]);
                        j++;
                    }
                }
                // segments() should include each maximal line segment containing 4 (or more) points exactly once.
                // check if collinear array is equal or larger than 3 + root p
                if (collinear.size() >= 3) {
                    double slopValue = p.slopeTo(collinear.get(0));
                    collinear.add(p);
                    collinear.sort(Point::compareTo);
                    if ((!endpoint.contains(collinear.get(0))) || (!endpointSlop
                            .contains(slopValue))) {
                        endpointSlop.add(slopValue);
                        endpoint.add(collinear.get(0));
                        lines.add(new LineSegment(collinear.get(0),
                                                  collinear.get(collinear.size() - 1)));
                    }
                    break;
                }
                if (hi > gt + 1) {
                    loStack.push(gt + 1);
                    hiStack.push(hi);
                }
                if (lt - 1 > lo) {
                    loStack.push(lo);
                    hiStack.push(lt - 1);
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

    private void exch(Point[] points, int i, int j) {
        Point temp = points[i];
        points[i] = points[j];
        points[j] = temp;
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
