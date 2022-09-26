import java.util.ArrayList;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.RectHV;

public class PointSET {
    private SET<Point2D> pointset;

    // construct an empty set of points
    public PointSET() {
        this.pointset = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return this.pointset.size() == 0;
    }

    // number of points in the set
    public int size() {
        return this.pointset.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (!contains(p)) {
            this.pointset.add(p);
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return this.pointset.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D i : this.pointset) {
            i.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        ArrayList<Point2D> result = new ArrayList<Point2D>();
        for (Point2D i : this.pointset) {
            if (rect.contains(i)) {
                result.add(i);
            }
        }
        return result;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        double mindis = 3;
        Point2D minP = null;
        for (Point2D i : this.pointset) {
            if (i.distanceSquaredTo(p) < mindis) {
                mindis = i.distanceSquaredTo(p);
                minP = i;
            }
        }
        return minP;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        PointSET trial = new PointSET();
        StdOut.println("Empty: " + trial.isEmpty());
        Point2D a = new Point2D(0.2, 0.4);
        Point2D b = new Point2D(0.5, 0.6);
        trial.insert(a);
        trial.insert(b);
        StdOut.println("ContainA: " + trial.contains(a));
        Point2D c = new Point2D(0.9, 0.9);
        StdOut.println("c near: " + trial.nearest(c));
        RectHV rec = new RectHV(0.1, 0.1,0.9,0.9);
        for (Point2D i : trial.range(rec)){
            StdOut.println("rec contains: "+ i);
        }
    }
}