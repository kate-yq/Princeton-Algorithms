import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    private int count = 0;
    private List<LineSegment> list = new ArrayList<>();

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
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

        for (int i = 0; i < points.length - 3; i++) {
            for (int j = i + 1; j < points.length - 2; j++) {
                for (int k = j + 1; k < points.length - 1; k++) {
                    double s1 = points[i].slopeTo(points[j]);
                    double s2 = points[i].slopeTo(points[k]);
                    if (s1 == s2) {
                        for (int kk = k + 1; kk < points.length; kk++) {
                            double s3 = points[i].slopeTo(points[kk]);
                            if (s3 == s2) {
                                this.count++;
                                Point[] temp = new Point[4];
                                temp[0] = points[i];
                                temp[1] = points[j];
                                temp[2] = points[k];
                                temp[3] = points[kk];
                                Arrays.sort(temp);
                                this.list.add(new LineSegment(temp[0], temp[3]));
                            }
                        }
                    }
                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return this.count;
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
