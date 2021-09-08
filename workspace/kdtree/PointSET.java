/* *****************************************************************************
 *  Name: Nguyen Van Dung
 *  Date: 09/06/2021
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Iterator;

// represents a set of points in the unit square
// Implement the following API by using a red–black BST:
//  (all points have x- and y-coordinates between 0 and 1)
public class PointSET {
    // use either SET or java.util.TreeSet; do not implement your own red–black BST.
    private final SET<Point2D> unit;

    // construct an empty set of points
    public PointSET() {
        unit = new SET<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return unit.isEmpty();
    }

    // number of points in the set
    public int size() {
        return unit.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        // time proportional to the logarithm of the number of points in the set in the worst case
        // Throw an IllegalArgumentException if any argument is null.
        if (p == null) throw new IllegalArgumentException("ERROR: input arg can not be null");
        unit.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        // time proportional to the logarithm of the number of points in the set in the worst case
        if (p == null) throw new IllegalArgumentException("ERROR: input arg can not be null");
        return unit.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : unit) {
            p.draw();
        }
        StdDraw.show();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        // time proportional to the number of points in the set.
        if (rect == null) throw new IllegalArgumentException("ERROR: input arg can not be null");
        Queue<Point2D> result = new Queue<>();
        for (Point2D p : unit) {
            if (rect.contains(p)) result.enqueue(p);
        }
        return result;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        // time proportional to the number of points in the set.
        if (p == null) throw new IllegalArgumentException("ERROR: input arg can not be null");
        Iterator<Point2D> temp = unit.iterator();
        Point2D nearest = null;
        if (temp.hasNext()) {
            nearest = temp.next();
        }
        if (nearest != null) {
            while (temp.hasNext()) {
                Point2D next = temp.next();
                if (p.distanceSquaredTo(nearest) > p.distanceSquaredTo(next))
                    nearest = next;
            }
        }
        return nearest;
    }

    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        PointSET brute = new PointSET();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            brute.insert(p);
        }

        // process nearest neighbor queries
        StdDraw.enableDoubleBuffering();
        Point2D query = null;
        while (true) {
            if (StdDraw.isMousePressed()) {
                double x = StdDraw.mouseX();
                double y = StdDraw.mouseY();
                query = new Point2D(x, y);
                StdDraw.setPenRadius(0.05);
                StdDraw.setPenColor(StdDraw.GREEN);
                query.draw();
            }
            if (query != null) {
                StdDraw.setPenRadius(0.05);
                StdDraw.setPenColor(StdDraw.BLUE);
                brute.nearest(query).draw();
                query = null;
            }
            // draw in blue the nearest neighbor (using kd-tree algorithm)
            brute.draw();
            StdDraw.show();
            StdDraw.pause(40);
        }
    }
}
