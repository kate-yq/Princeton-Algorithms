// don't consider non-DAG

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private final Digraph graph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.graph = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || w < 0 || v >= this.graph.V() || w >= this.graph.V()) {
            throw new IllegalArgumentException();
        }
        if (v == w) {
            return 0;
        }
        // BFS v and id all related point
        boolean[] checkv = new boolean[this.graph.V()];
        int[] disTOv = new int[this.graph.V()];
        Queue<Integer> bfsv = new Queue<Integer>();
        bfsv.enqueue(v);
        checkv[v] = true;
        disTOv[v] = 0;
        while (!bfsv.isEmpty()) {
            int i = bfsv.dequeue();
            for (int j : graph.adj(i)) {
                if (!checkv[j]) {
                    bfsv.enqueue(j);
                    checkv[j] = true;
                    disTOv[j] = disTOv[i] + 1;
                }
            }
        }
        // BFS w and see if cross with a-tree and mark down min distence
        int min = graph.V();
        if (checkv[w]) {
            min = disTOv[w];
        }
        boolean[] checkw = new boolean[this.graph.V()];
        int[] disTOw = new int[this.graph.V()];
        Queue<Integer> bfsw = new Queue<Integer>();
        bfsw.enqueue(w);
        checkw[w] = true;
        disTOw[w] = 0;
        while (!bfsw.isEmpty()) {
            int i = bfsw.dequeue();
            for (int j : graph.adj(i)) {
                if (!checkw[j]) {
                    bfsw.enqueue(j);
                    checkw[j] = true;
                    disTOw[j] = disTOw[i] + 1;
                }
                if (checkv[j] && (disTOv[j] + disTOw[j]) < min) {
                    min = disTOv[j] + disTOw[j];
                }
            }
            if (disTOw[i] >= min) {
                break;
            }
        }
        if (min == graph.V()) {
            return -1;
        }
        return min;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path;
    // -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || w < 0 || v >= this.graph.V() || w >= this.graph.V()) {
            throw new IllegalArgumentException();
        }
        if (v == w) {
            return v;
        }
        // BFS v and id all related point
        boolean[] checkv = new boolean[this.graph.V()];
        int[] disTOv = new int[this.graph.V()];
        Queue<Integer> bfsv = new Queue<Integer>();
        bfsv.enqueue(v);
        checkv[v] = true;
        disTOv[v] = 0;
        while (!bfsv.isEmpty()) {
            int i = bfsv.dequeue();
            for (int j : graph.adj(i)) {
                if (!checkv[j]) {
                    bfsv.enqueue(j);
                    checkv[j] = true;
                    disTOv[j] = disTOv[i] + 1;
                }
            }
        }
        // BFS b and see if cross with a-tree and mark down min distence
        int min = graph.V();
        int out = 0;
        if (checkv[w]) {
            out = w;
            min = disTOv[w];
        }
        boolean[] checkw = new boolean[this.graph.V()];
        int[] disTOw = new int[this.graph.V()];
        Queue<Integer> bfsw = new Queue<Integer>();
        bfsw.enqueue(w);
        checkw[w] = true;
        disTOw[w] = 0;
        while (!bfsw.isEmpty()) {
            int i = bfsw.dequeue();
            for (int j : graph.adj(i)) {
                if (!checkw[j]) {
                    bfsw.enqueue(j);
                    checkw[j] = true;
                    disTOw[j] = disTOw[i] + 1;
                }
                if (checkv[j] && (disTOv[j] + disTOw[j]) < min) {
                    min = disTOv[j] + disTOw[j];
                    out = j;
                }
            }
            if (disTOw[i] >= min) {
                break;
            }
        }
        if (min == graph.V()) {
            return -1;
        }
        return out;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in
    // w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }
        for (Integer V : v) {
            if (V == null || V < 0 || V >= this.graph.V()) {
                throw new IllegalArgumentException();
            }
        }
        for (Integer W : w) {
            if (W == null || W < 0 || W >= this.graph.V()) {
                throw new IllegalArgumentException();
            }
        }
        int min = graph.V();
        for (Integer i : v) {
            for (Integer j : w) {
                if ((length(i, j) != -1) && (length(i, j) < min)) {
                    min = length(i, j);
                }
            }
        }
        if (min == graph.V()) {
            return -1;
        } else {
            return min;
        }
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such
    // path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }
        for (Integer V : v) {
            if (V == null || V < 0 || V >= this.graph.V()) {
                throw new IllegalArgumentException();
            }
        }
        for (Integer W : w) {
            if (W == null || W < 0 || W >= this.graph.V()) {
                throw new IllegalArgumentException();
            }
        }
        int min = graph.V();
        int sap = 0;
        for (Integer i : v) {
            for (Integer j : w) {
                if ((length(i, j) != -1) && (length(i, j) < min)) {
                    min = length(i, j);
                    sap = ancestor(i, j);
                }
            }
        }
        if (min == graph.V()) {
            return -1;
        } else {
            return sap;
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
