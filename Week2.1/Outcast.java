import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet wordnet;

    public Outcast(WordNet wordnet) { // constructor takes a WordNet object
        if (wordnet == null) {
            throw new IllegalArgumentException();
        }
        this.wordnet = wordnet;
    }

    public String outcast(String[] nouns) { // given an array of WordNet nouns, return an outcast
        for (String s : nouns) {
            if (s == null || (!wordnet.isNoun(s))) {
                throw new IllegalArgumentException();
            }
        }
        int max = 0;
        String out = nouns[0];
        for (int i = 0; i < nouns.length; i++) {
            int temp = 0;
            for (int j = 0; j < nouns.length; j++) {
                temp = temp + wordnet.distance(nouns[i], nouns[j]);
            }
            if (temp > max) {
                max = temp;
                out = nouns[i];
            }
        }
        return out;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}