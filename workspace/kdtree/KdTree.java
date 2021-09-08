/* *****************************************************************************
 *  Name: Nguyen Van Dung
 *  Date: 09/06/2021
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

// build a BST with points in the nodes, using the x- and y-coordinates of the points as keys in strictly alternating sequence.

// boolean true    VERTICAL,
// boolean false    HORIZONTAL
public class KdTree {
    private enum GoType {
        LEFT,
        RIGHT,
        REPLACE
    }

    private class Node {
        private int mcount;
        private final boolean mtype;
        private Point2D mkey;
        private Node left;
        private Node right;

        public Node(Point2D key, boolean splitType) {
            mkey = key;
            mtype = splitType;
            mcount = 1;
            left = null;
            right = null;
        }

        public GoType compare(Point2D other) {
            if (mkey.equals(other)) return GoType.REPLACE;
            // true VERTICAL: use the x-coordinate (if the point to be inserted has
            // a smaller x-coordinate than the point at the root, go left; otherwise go right)
            if (mtype) {
                if (other.x() < mkey.x()) return GoType.LEFT;
                else return GoType.RIGHT;
            }
            else {
                // false HORIZONTAL: use the y-coordinate (if the point to be inserted has
                // a smaller y-coordinate than the point in the node, go left; otherwise go right);
                if (other.y() < mkey.y()) return GoType.LEFT;
                else return GoType.RIGHT;
            }
        }

        public GoType checkRect(RectHV rect) {
            // vertical line
            if (mtype) {
                if (rect.xmax() < mkey.x()) return GoType.LEFT;
                else if (rect.xmin() > mkey.x()) return GoType.RIGHT;
                else return GoType.REPLACE; // intersect
            }
            // horizontal line
            else {
                if (rect.ymax() < mkey.y()) return GoType.LEFT;
                else if (rect.ymin() > mkey.y()) return GoType.RIGHT;
                else return GoType.REPLACE;
            }
        }

        public RectHV lineRect() {
            if (mtype) return new RectHV(mkey.x(), 0, mkey.x(), 1);
            else return new RectHV(0, mkey.y(), 1, mkey.y());
        }
    }

    private Node root;

    public KdTree() {
        root = null;
    }

    // is the set empty?
    public boolean isEmpty() {
        return (size() == 0);
    }

    // number of points in the set
    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null) return 0;
        else return x.mcount;
    }

    // at the root we use the x-coordinate (if the point to be inserted
    // has a smaller x-coordinate than the point at the root, go left; otherwise go right);
    // then at the next level, we use the y-coordinate (if the point to be inserted
    // has a smaller y-coordinate than the point in the node, go left; otherwise go right);
    // then at the next level the x-coordinate, and so forth.
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        // Throw an IllegalArgumentException if any argument is null.
        if (p == null) throw new IllegalArgumentException("ERROR: input arg can not be null");
        // root node = VERTICAL split
        root = insert(root, p, true);
    }

    private Node insert(Node x, Point2D p, boolean type) {
        if (x == null) return new Node(p, type);
        GoType temp = x.compare(p);
        if (temp == GoType.LEFT) x.left = insert(x.left, p, !x.mtype);
        else if (temp == GoType.RIGHT) x.right = insert(x.right, p, !x.mtype);
        else {
            x.mkey = p;
        }
        x.mcount = 1 + size(x.left) + size(x.right);
        return x;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("ERROR: input arg can not be null");
        Node x = root;
        while (x != null) {
            GoType temp = x.compare(p);
            if (temp == GoType.LEFT) x = x.left;
            else if (temp == GoType.RIGHT) x = x.right;
            else return true;
        }
        return false;
    }

    // draw all points to standard draw
    public void draw() {
        // draw all of the points to standard draw in black and the subdivisions in red (for vertical splits)
        // and blue (for horizontal splits)
        // level traversal
        draw(root, 0, 0, 1, 1);
    }

    private void draw(Node x, double x0, double y0, double x1, double y1) {
        if (x != null) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            StdDraw.point(x.mkey.x(), x.mkey.y());
            // true VERTICAL
            StdDraw.setPenRadius(0.001);
            if (x.mtype) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(x.mkey.x(), y0, x.mkey.x(), y1);
                // StdOut.printf("RED %8.6f %8.6f %8.6f %8.6f\n", x.mkey.x(), y0, x.mkey.x(), y1);
                draw(x.left, Math.min(x0, x.mkey.x()), y0, Math.min(x.mkey.x(), x1), y1);
                draw(x.right, Math.max(x0, x.mkey.x()), y0, Math.max(x.mkey.x(), x1), y1);
            }
            // false HORIZONTAL
            else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(x0, x.mkey.y(), x1, x.mkey.y());
                // StdOut.printf("BLUE %8.6f %8.6f %8.6f %8.6f\n", x0, x.mkey.y(), x1, x.mkey.y());
                draw(x.left, x0, Math.min(y0, x.mkey.y()), x1, Math.min(y1, x.mkey.y()));
                draw(x.right, x0, Math.max(y0, x.mkey.y()), x1, Math.max(y1, x.mkey.y()));
            }
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        // pruning rule: if the query rectangle does not intersect the rectangle corresponding to a node,
        // there is no need to explore that node (or its subtrees).
        if (rect == null) throw new IllegalArgumentException("ERROR: input arg can not be null");
        Queue<Point2D> queue = new Queue<>();
        range(queue, rect, root);
        return queue;
    }

    private void range(Queue<Point2D> q, RectHV rect, Node x) {
        if (x != null) {
            GoType temp = x.checkRect(rect);
            // if rect contains point
            if (rect.contains(x.mkey)) q.enqueue(x.mkey);
            // if rect on the left
            if (temp == GoType.LEFT) range(q, rect, x.left);
                // if rect on the right
            else if (temp == GoType.RIGHT) range(q, rect, x.right);
                // if rect intersect
            else {
                range(q, rect, x.left);
                range(q, rect, x.right);
            }
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("ERROR: input arg can not be null");
        // Nearest-neighbor search. To find a closest point to a given query point, start at the root
        return nearest(root, root.mkey, p);
    }

    private Point2D nearest(Node x, Point2D currentChamp, Point2D query) {
        if (x == null) return currentChamp;
        // check distance between current node's point to query point
        double currentDist = x.mkey.distanceSquaredTo(query);
        double minDist = currentChamp.distanceSquaredTo(query);
        if (currentDist < minDist) currentChamp = x.mkey;
        // StdOut.printf("%f, %f - current Champ %s\n", currentDist, minDist, currentChamp.toString());
        // recursive search left/bottom (if it contains closer point)
        GoType goType = x.compare(query);
        Point2D returnChamp;
        // organize method so it begin by searching for query point.
        if (goType == GoType.LEFT) {
            // StdOut.println("GO LEFT");
            returnChamp = nearest(x.left, currentChamp, query);
            // StdOut.printf("return champ %s\n", returnChamp.toString());
        }
        else if (goType == GoType.RIGHT) {
            // StdOut.println("GO RIGHT");
            returnChamp = nearest(x.right, currentChamp, query);
            // StdOut.printf("return champ %s\n", returnChamp.toString());
        }
        else return query;
        // recursively search in both subtrees using the following pruning rule
        // if the closest point discovered so far is closer than the distance between
        // the query point and the rectangle corresponding to a node
        // recursive search right/top (if it contains closer point)/ skip if previous has found new point
        if (returnChamp.distanceSquaredTo(query) < x.lineRect().distanceSquaredTo(query))
            return returnChamp;
        else {
            currentChamp = returnChamp;
            if (goType == GoType.LEFT) {
                // StdOut.println("GO RIGHT - EXTRA");
                returnChamp = nearest(x.right, currentChamp, query);
                // StdOut.printf("return champ %s\n", returnChamp.toString());
            }
            else {
                // StdOut.println("GO LEFT - EXTRA");
                returnChamp = nearest(x.left, currentChamp, query);
                // StdOut.printf("return champ %s\n", returnChamp.toString());
            }
            return returnChamp;
        }
    }

    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
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
                kdtree.nearest(query).draw();
                query = null;
            }
            // draw in blue the nearest neighbor (using kd-tree algorithm)
            kdtree.draw();
            StdDraw.show();
            StdDraw.pause(40);
        }
    }
}
