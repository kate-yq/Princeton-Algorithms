import java.util.ArrayList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private Node out;

    private class Node implements Comparable<Node> {
        int key;
        int step;
        Board bd;
        Node parent;

        public int compareTo(Node that) {
            if (this.key < that.key) {
                return -1;
            }
            if (this.key > that.key) {
                return 1;
            }
            return 0;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        Node initialNode = new Node();
        initialNode.key = initial.manhattan();
        initialNode.bd = initial;
        initialNode.step = 0;

        MinPQ<Node> minPQ = new MinPQ<Node>();
        minPQ.insert(initialNode);

        // create a twin and run simontaniously
        Node twininitial = new Node();
        twininitial.bd = initial.twin();
        twininitial.key = twininitial.bd.manhattan();
        twininitial.step = 0;

        MinPQ<Node> twinPQ = new MinPQ<Node>();
        twinPQ.insert(twininitial);

        // solve
        this.out = minPQ.delMin();
        Node twinout = twinPQ.delMin();
        while (!(out.bd.isGoal() || twinout.bd.isGoal())) {
            for (Board neighbor : out.bd.neighbors()) {
                if (out.parent != null) {
                    if (!neighbor.equals(out.parent.bd)) {
                        Node add = new Node();
                        add.parent = out;
                        add.step = add.parent.step + 1;
                        add.bd = neighbor;
                        add.key = add.bd.manhattan() + add.step;
                        minPQ.insert(add);
                    }
                } else {
                    Node add = new Node();
                    add.parent = out;
                    add.step = add.parent.step + 1;
                    add.bd = neighbor;
                    add.key = add.bd.manhattan() + add.step;
                    minPQ.insert(add);
                }
            }
            for (Board neighbor : twinout.bd.neighbors()) {
                if (twinout.parent != null) {
                    if (!neighbor.equals(twinout.parent.bd)) {
                        Node twinadd = new Node();
                        twinadd.parent = twinout;
                        twinadd.step = twinadd.parent.step + 1;
                        twinadd.bd = neighbor;
                        twinadd.key = twinadd.bd.manhattan() + twinadd.step;
                        twinPQ.insert(twinadd);
                    }
                } else {
                    Node twinadd = new Node();
                    twinadd.parent = twinout;
                    twinadd.step = twinadd.parent.step + 1;
                    twinadd.bd = neighbor;
                    twinadd.key = twinadd.bd.manhattan() + twinadd.step;
                    twinPQ.insert(twinadd);
                }
            }
            this.out = minPQ.delMin();
            twinout = twinPQ.delMin();
            if (out.bd.isGoal() || twinout.bd.isGoal()) {
                break;
            }
        }

    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return this.out.bd.isGoal();
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!this.out.bd.isGoal()) {
            return -1;
        }
        return this.out.step;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!this.out.bd.isGoal()) {
            return null;
        }
        Node in = this.out;
        edu.princeton.cs.algs4.Stack<Board> route = new edu.princeton.cs.algs4.Stack<Board>();
        route.push(in.bd);
        while (in.parent != null) {
            route.push(in.parent.bd);
            in = in.parent;
        }
        ArrayList<Board> result = new ArrayList<Board>();
        while (!route.isEmpty()) {
            result.add(route.pop());
        }
        return result;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable()) {
            StdOut.println("No solution possible");
        } else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
