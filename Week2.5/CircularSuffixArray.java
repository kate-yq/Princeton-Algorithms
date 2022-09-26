import edu.princeton.cs.algs4.Quick3string;
import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {
    private int Length;
    private int[] indexs;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException("string is null");
        }
        this.Length = s.length();
        String[] suffix = new String[Length];
        String[] sortsuffix = new String[Length];
        for (int i = 0; i < this.Length; i++) {
            suffix[i] = s.substring(i, this.Length) + s.substring(0, i);
            sortsuffix[i] = s.substring(i, this.Length) + s.substring(0, i);
        }
        Quick3string.sort(sortsuffix);
        this.indexs = new int[this.Length];
        for (int i = 0; i < this.Length; i++) {
            for (int j = 0; j < this.Length; j++) {
                if (sortsuffix[i].equals(suffix[j])) {
                    indexs[i] = j;
                    break;
                }
            }
        }
    }

    // length of s
    public int length() {
        return this.Length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || (i > (this.Length - 1))) {
            throw new IllegalArgumentException("invalid i");
        }
        return indexs[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        String s = "ABRACADABRA!";
        CircularSuffixArray testing = new CircularSuffixArray(s);
        StdOut.println("string length = " + testing.length());
        for (int i = 0; i < testing.length(); i++) {
            StdOut.printf("index[%d]: %d \n", i, testing.index(i));
        }
    }

}