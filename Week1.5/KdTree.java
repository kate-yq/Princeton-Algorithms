import java.util.ArrayList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.RectHV;

public class KdTree {
    private final Node initial;
    private int count;

    private class Node {
        RectHV range;
        Point2D point;
        boolean vertical;
        Node left, right;

    }

    // construct an empty set of points
    public KdTree() {
        this.initial = new Node();
        initial.range = new RectHV(0.0, 0.0, 1.0, 1.0);
        initial.point = null;
        initial.vertical = true;
    }

    // is the set empty?
    public boolean isEmpty() {
        return this.count == 0;
    }

    // number of points in the set
    public int size() {
        return this.count;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null || (!this.initial.range.contains(p))) {
            throw new IllegalArgumentException();
        }

        if (this.initial.point == null) {
            this.initial.point = p;
            this.count++;
            initial.left = new Node();
            initial.right = new Node();
            initial.left.range = new RectHV(0, 0, p.x(), 1);
            initial.left.vertical = false;
            initial.right.range = new RectHV(p.x(), 0, 1, 1);
            initial.right.vertical = false;
            return;
        }
        Node current = this.initial;
        while (current.range.contains(p)) {
            if (current.point == null) {
                current.point = p;
                this.count++;
                current.left = new Node();
                current.right = new Node();
                if (current.vertical) {
                    current.left.range = new RectHV(current.range.xmin(), current.range.ymin(), p.x(),
                            current.range.ymax());
                    current.left.vertical = false;
                    current.right.range = new RectHV(p.x(), current.range.ymin(), current.range.xmax(),
                            current.range.ymax());
                    current.right.vertical = false;
                } else {
                    current.left.range = new RectHV(current.range.xmin(), current.range.ymin(), current.range.xmax(),
                            p.y());
                    current.left.vertical = true;
                    current.right.range = new RectHV(current.range.xmin(), p.y(), current.range.xmax(),
                            current.range.ymax());
                    current.right.vertical = true;
                }
                return;
            }
            if (current.point.equals(p)) {
                return;
            }
            if (current.vertical) {
                if (p.x() <= current.point.x()) {
                    current = current.left;
                } else {
                    current = current.right;
                }
            } else {
                if (p.y() <= current.point.y()) {
                    current = current.left;
                } else {
                    current = current.right;
                }
            }
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        Node current = this.initial;
        while (current.point != null && current.range.contains(p)) {
            if (current.point.equals(p)) {
                return true;
            }
            if (current.vertical) {
                if (p.x() <= current.point.x()) {
                    current = current.left;
                } else {
                    current = current.right;
                }
            } else {
                if (p.y() <= current.point.y()) {
                    current = current.left;
                } else {
                    current = current.right;
                }
            }
        }
        return false;
    }

    // draw all points and split lines to standard draw
    public void draw() {
        realdraw(this.initial);
    }

    private void realdraw(Node node) {
        if (node.point != null) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.point(node.point.x(), node.point.y());
            if (node.vertical) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(node.point.x(), node.range.ymin(), node.point.x(), node.range.ymax());
            } else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(node.range.xmin(), node.point.y(), node.range.xmax(), node.point.y());
            }
            realdraw(node.left);
            realdraw(node.right);
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        ArrayList<Point2D> result = new ArrayList<Point2D>();
        _range(rect, initial, result);
        return result;
    }

    private void _range(RectHV rect, Node node, ArrayList<Point2D> result) {
        if (node.range.intersects(rect) && node.point != null) {
            if (rect.contains(node.point)) {
                result.add(node.point);
            }
            _range(rect, node.left, result);
            _range(rect, node.right, result);
        }
        return;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        double mindis = 3;
        Point2D near = null;
        Point2D nearest = _nearest(p, initial, mindis, near);
        return nearest;
    }

    private Point2D _nearest(Point2D p, Node node, double mindis, Point2D near) {
        if (node.point != null) {
            if (mindis > node.point.distanceSquaredTo(p)) {
                mindis = node.point.distanceSquaredTo(p);
                near = node.point;
            }
            if (node.left.range.contains(p)) {
                near = _nearest(p, node.left, mindis, near);
                mindis = near.distanceSquaredTo(p);
                if (mindis > node.right.range.distanceSquaredTo(p)) {
                    near = _nearest(p, node.right, mindis, near);
                }
            } else {
                near = _nearest(p, node.right, mindis, near);
                mindis = near.distanceSquaredTo(p);
                if (mindis > node.left.range.distanceSquaredTo(p)) {
                    near = _nearest(p, node.left, mindis, near);
                }
            }
        }
        // if (node.range.contains(p) || mindis >= node.range.distanceSquaredTo(p)) {
        // if (mindis > node.point.distanceSquaredTo(p)) {
        // mindis = node.point.distanceSquaredTo(p);
        // near = node.point;
        // }
        // near = _nearest(p, node.left, mindis, near);
        // mindis = near.distanceSquaredTo(p);
        // near = _nearest(p, node.right, mindis, near);
        // }
        // }
        return near;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        // left blank
    }

}
