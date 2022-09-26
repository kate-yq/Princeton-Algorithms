// still cannot throw error if input a non-DAG

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.StdOut;

public class WordNet {
    private final Digraph graph;
    private final ArrayList<String> byid; // use id to fast locate noun
    private final RedBlackBST<String, ArrayList<Integer>> bynoun; // use noun to search existance or id

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        String line = "";
        this.byid = new ArrayList<String>();
        this.bynoun = new RedBlackBST<String, ArrayList<Integer>>();
        // read synsets and record in BST & Array
        try {
            // parsing a CSV file into BufferedReader class constructor
            BufferedReader br = new BufferedReader(new FileReader(synsets));
            while ((line = br.readLine()) != null) {
                String[] synset = line.split(","); // use comma as separator
                String[] noun = synset[1].split(" "); // use " " as separator
                for (int i = 0; i < noun.length; i++) {
                    if (!isNoun(noun[i])) {
                        ArrayList<Integer> location = new ArrayList<>();
                        location.add(Integer.parseInt(synset[0]));
                        this.bynoun.put(noun[i], location);
                    } else {
                        ArrayList<Integer> location = bynoun.get(noun[i]);
                        location.add(Integer.parseInt(synset[0]));
                        this.bynoun.put(noun[i], location);
                    }
                }
                this.byid.add(synset[1]);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.graph = new Digraph(byid.size());

        // read hypernyms to create edges on graph
        int count = 0;
        try {
            // parsing a CSV file into BufferedReader class constructor
            BufferedReader br = new BufferedReader(new FileReader(hypernyms));
            while ((line = br.readLine()) != null) {
                String[] hypernym = line.split(","); // use comma as separator
                if (hypernym.length==1){
                    count++;
                    continue;
                }
                for (int i = 1; i < hypernym.length; i++) {
                    this.graph.addEdge(Integer.parseInt(hypernym[0]), Integer.parseInt(hypernym[i]));
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // check if input is a rooted DAG, throw error if not.
        if (count != 1){
            throw new IllegalArgumentException("not rooted");
        }
        DirectedCycle checkcycle = new DirectedCycle(this.graph);
        if (checkcycle.hasCycle()) {
            throw new IllegalArgumentException("not DAG");
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return this.bynoun.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        }
        return this.bynoun.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null || (!isNoun(nounA)) || (!isNoun(nounB))) {
            throw new IllegalArgumentException();
        }
        if (nounA == nounB) {
            return 0;
        }
        ArrayList<Integer> a = this.bynoun.get(nounA);
        ArrayList<Integer> b = this.bynoun.get(nounB);
        // use SAP iterale to find
        SAP finddis = new SAP(this.graph);
        int dis = finddis.length(a, b);
        return dis;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA
    // and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null || (!isNoun(nounA)) || (!isNoun(nounB))) {
            throw new IllegalArgumentException();
        }
        ArrayList<Integer> a = this.bynoun.get(nounA);
        ArrayList<Integer> b = this.bynoun.get(nounB);

        // use SAP iterale to find
        SAP findanc = new SAP(this.graph);
        int ancestor = findanc.length(a, b);
        return byid.get(ancestor);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wd = new WordNet("synset.txt", "hypernyms.txt");
        StdOut.println("contain 1530s:  " + wd.isNoun("1530s"));
        StdOut.println("contain xxx:  " + wd.isNoun("xxx"));
        StdOut.println("distance between 1530s and 1750s:  " + wd.distance("1530s", "1750s"));
        StdOut.println("parent of 1530s and 1750s:  " + wd.sap("1530s", "1750s"));
    }
}