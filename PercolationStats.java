import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import java.lang.Math;

public class PercolationStats {
    private int T;
    private double[] thresholds;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        this.T = trials;
        this.thresholds = new double[trials];
        for (int i = 0; i < trials; i++) {
            var percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                percolation.open(row, col);
            }
            this.thresholds[i] = (double) percolation.numberOfOpenSites() / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(this.thresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(this.thresholds);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        double m = this.mean();
        double s = this.stddev();
        return m - 1.96 * s / (Math.sqrt(this.T));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        double m = this.mean();
        double s = this.stddev();
        return m + 1.96 * s / (Math.sqrt(this.T));
    }

    // test client (see below)
    public static void main(String[] args) {
        var percolationstats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        StdOut.printf("mean = %f\n", percolationstats.mean());
        StdOut.printf("stddev = %f\n", percolationstats.stddev());
        StdOut.printf("95%% confidence interval = [%f, %f]\n", percolationstats.confidenceLo(),
                percolationstats.confidenceHi());
    }
}

// javac -cp '.:./algs4.jar' PercolationStats.java
// java -cp '.:./algs4.jar' PercolationStats