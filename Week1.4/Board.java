import java.util.ArrayList;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Board {
    private int[][] tile;
    private final int n;
    private int draw1col, draw1row, draw2col, draw2row;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.n = tiles.length;
        this.tile = new int[n][n];
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                this.tile[row][col] = tiles[row][col];
            }
        }

        // draw 2 non-empty tile and swap
        this.draw1col = StdRandom.uniform(n);
        this.draw2col = StdRandom.uniform(n);
        this.draw1row = StdRandom.uniform(n);
        this.draw2row = StdRandom.uniform(n);
        while ((tiles[draw1row][draw1col] == tiles[draw2row][draw2col])
                || (tiles[draw1row][draw1col] == 0)
                || (tiles[draw2row][draw2col] == 0)) {
            draw1col = StdRandom.uniform(n);
            draw1row = StdRandom.uniform(n);
            draw2col = StdRandom.uniform(n);
            draw2row = StdRandom.uniform(n);
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder outcome = new StringBuilder();
        outcome.append(this.n);
        for (int row = 0; row < this.n; row++) {
            outcome.append("\n");
            for (int col = 0; col < this.n; col++) {
                outcome.append(tile[row][col]);
                outcome.append("  ");
            }
        }
        return outcome.toString();
    }

    // board dimension n
    public int dimension() {
        return this.n;
    }

    // number of tiles out of place
    public int hamming() {
        int ham = -1;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (tile[row][col] != (row * n + col + 1)) {
                    ham++;
                }
            }
        }
        return ham;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int man = 0;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (tile[row][col] != 0) {
                    man = man + Math.abs((tile[row][col] - 1) / n - row) + Math.abs((tile[row][col] - 1) % n - col);
                }
            }
        }
        return man;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return manhattan() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }
        if (!(y.getClass() == this.getClass())) {
            return false;
        }
        Board that = (Board) y;
        if (this.dimension() != that.dimension()) {
            return false;
        }
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (tile[row][col] != that.tile[row][col]) {
                    return false;
                }
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {

        int spacecol = 0;
        int spacerow = 0;
        Board neighberU = new Board(this.tile);
        Board neighberD = new Board(this.tile);
        Board neighberL = new Board(this.tile);
        Board neighberR = new Board(this.tile);
        ArrayList<Board> result = new ArrayList<Board>();

        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (tile[row][col] == 0) {
                    spacerow = row;
                    spacecol = col;
                    break;
                }
            }
        }

        if (spacerow > 0) {
            neighberU.tile[spacerow][spacecol] = this.tile[spacerow - 1][spacecol];
            neighberU.tile[spacerow - 1][spacecol] = 0;
            result.add(neighberU);
        }

        if (spacerow < n - 1) {
            neighberD.tile[spacerow][spacecol] = this.tile[spacerow + 1][spacecol];
            neighberD.tile[spacerow + 1][spacecol] = 0;
            result.add(neighberD);
        }

        if (spacecol > 0) {
            neighberL.tile[spacerow][spacecol] = this.tile[spacerow][spacecol - 1];
            neighberL.tile[spacerow][spacecol - 1] = 0;
            result.add(neighberL);
        }

        if (spacecol < n - 1) {
            neighberR.tile[spacerow][spacecol] = this.tile[spacerow][spacecol + 1];
            neighberR.tile[spacerow][spacecol + 1] = 0;
            result.add(neighberR);
        }

        return result;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board twin = new Board(this.tile);
        twin.tile[draw1row][draw1col] = this.tile[draw2row][draw2col];
        twin.tile[draw2row][draw2col] = this.tile[draw1row][draw1col];
        return twin;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] sample1 = {
                { 1, 3, 2, },
                { 5, 4, 6, },
                { 7, 8, 0, },
        };
        Board sampleB1 = new Board(sample1);

        StdOut.println(sampleB1.toString());
        StdOut.printf("n = %d \n", sampleB1.dimension());
        StdOut.printf("hamming = %d \n", sampleB1.hamming());
        StdOut.printf("manhattan = %d \n", sampleB1.manhattan());
        if (sampleB1.isGoal()) {
            StdOut.println("goal");
        } else {
            StdOut.println("not goal");
        }
        StdOut.println("twin: " + sampleB1.twin());
        for (Board neighbor : sampleB1.neighbors()) {
            StdOut.print("neighbor " + neighbor + "\n");
        }
    }

}