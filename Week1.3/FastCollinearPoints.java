import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    private int count = 0;
    private List<LineSegment> list = new ArrayList<>();

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        // check repeat
        for (int a = 0; a < points.length; a++) {
            if (points[a] == null) {
                throw new IllegalArgumentException();
            }
            for (int b = a + 1; b < points.length; b++) {
                if (points[b].compareTo(points[a]) == 0) {
                    throw new IllegalArgumentException();
                }
            }
        }
        // main
        for (int i = 0; i < points.length; i++) {
            // copy points
            Point[] remainingpoints = new Point[points.length - 1];
            if (i == 0) {
                for (int it = 0; it < points.length - 1; it++) {
                    remainingpoints[it] = points[it + 1];
                }
            } else {
                for (int it1 = 0; it1 < i; it1++) {
                    remainingpoints[it1] = points[it1];
                }
                for (int it2 = i; it2 < points.length - 1; it2++) {
                    remainingpoints[it2] = points[it2 + 1];
                }
            }
            // sort remainings by slope, then check if any ajacent is equal.
            Arrays.sort(remainingpoints, points[i].slopeOrder());
            int m = 0;
            while (m < remainingpoints.length - 1) {
                int n = m + 1;
                while (n < remainingpoints.length) {
                    if (points[i].slopeTo(remainingpoints[m]) == points[i].slopeTo(remainingpoints[n])) {
                        n++;
                    } else {
                        break;
                    }
                }
                // check if more than 3 slopes are equal
                if (n - m > 2) {
                    Point[] aux = new Point[n - m + 1];
                    aux[0] = points[i];
                    for (int k = 1; k < aux.length; k++) {
                        aux[k] = remainingpoints[m - 1 + k];
                    }
                    Arrays.sort(aux);
                    LineSegment temp = new LineSegment(aux[0], aux[aux.length - 1]);
                    // check duplicate. If not, then add to the list
                    boolean found = false;
                    for (LineSegment line : list) {
                        if (line.toString().equals(temp.toString())) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        this.list.add(temp);
                        count++;
                    }
                }
                m = n;
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return count;
    }

    // the line segments
    public LineSegment[] segments() {
        return this.list.toArray(LineSegment[]::new);
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