import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF weightedQuickUnionUF;
    private int N;
    private boolean[] grid;
    private int count;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        this.weightedQuickUnionUF = new WeightedQuickUnionUF(n * n + 2);
        this.grid = new boolean[n * n + 2];
        this.count = 0;
        this.N = n;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        int n = this.N;
        if (row <= 0 || col <= 0 || row > n || col > n) {
            throw new IllegalArgumentException();
        }
        int location = (row - 1) * n + col;
        if (!grid[location]) {
            grid[location] = true;
            this.count++;
        }

        if (row == 1) {
            this.weightedQuickUnionUF.union(0, location);
        }
        if (row == n) {
            this.weightedQuickUnionUF.union(n * n + 1, location);
        }

        if (row < n && grid[location + n]) {
            this.weightedQuickUnionUF.union(location, location + n);
        }
        if (row > 1 && grid[location - n]) {
            this.weightedQuickUnionUF.union(location, location - n);
        }
        if (col > 1 && grid[location - 1]) {
            this.weightedQuickUnionUF.union(location, location - 1);
        }
        if (col < n && grid[location + 1]) {
            this.weightedQuickUnionUF.union(location, location + 1);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row <= 0 || col <= 0 || row > this.N || col > this.N) {
            throw new IllegalArgumentException();
        }
        int location = (row - 1) * this.N + col;
        return grid[location];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row <= 0 || col <= 0 || row > this.N || col > this.N) {
            throw new IllegalArgumentException();
        }
        int location = (row - 1) * this.N + col;
        return this.weightedQuickUnionUF.find(0) == this.weightedQuickUnionUF.find(location);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return this.count;
    }

    // does the system percolate?
    public boolean percolates() {
        int n = this.N;
        return this.weightedQuickUnionUF.find(0) == this.weightedQuickUnionUF.find(n * n + 1);
    }

    // test client (optional)
    public static void main(String[] args) {

    }
}