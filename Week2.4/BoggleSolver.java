import java.util.ArrayList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {
    private final Trie26 _dictionary;
    private boolean[][] visited;
    private int xdimensions, ydimensions;
    private static int[] xdis = { -1, 0, 1, -1, 1, -1, 0, 1 };
    private static int[] ydis = { -1, -1, -1, 0, 0, 1, 1, 1 };

    // Initializes the data structure using the given array of strings as the
    // dictionary.
    // (You can assume each word in the dictionary contains only the uppercase
    // letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        this._dictionary = new Trie26();
        for (String word : dictionary) {
            this._dictionary.put(word, scoreOf(word));
        }
    }

    private class VisitedChar {
        private int x;
        private int y;

        private VisitedChar(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {

        ArrayList<String> validwords = new ArrayList<String>();
        this.xdimensions = board.rows();
        this.ydimensions = board.cols();
        this.visited = new boolean[this.xdimensions][this.ydimensions];
        int level = 0;

        VisitedChar vchar = new VisitedChar(0, 0);
        for (int i = 0; i < this.xdimensions; i++) {
            for (int j = 0; j < this.ydimensions; j++) {
                StringBuilder buildword = new StringBuilder();
                vchar.x = i;
                vchar.y = j;
                visited[i][j] = true;
                getValidWords(board, validwords, vchar, buildword, level + 1);
                visited[i][j] = false;
            }
        }
        return validwords;
    }

    // need a private function to find all un-visited pile
    private void getValidWords(BoggleBoard board, ArrayList<String> validWord, VisitedChar vchar,
            StringBuilder substring, int level) {
        if (board.getLetter(vchar.x, vchar.y) == 'Q') {
            substring.append("QU");
            level++;
        } else {
            substring.append(board.getLetter(vchar.x, vchar.y));
        }
        String word = substring.toString();
        if (!_dictionary.containsSubstring(word)) {
            return;
        }
        if (_dictionary.contains(word) && !validWord.contains(word) && level > 2) {
            validWord.add(word);
        }

        int newx = 0;
        int newy = 0;
        VisitedChar newvchar = new VisitedChar(0, 0);
        for (int i = 0; i < 8; i++) {
            newx = vchar.x + xdis[i];
            newy = vchar.y + ydis[i];
            if (validIndex(newx, newy) && !visited[newx][newy]) {
                visited[newx][newy] = true;
                newvchar.x = newx;
                newvchar.y = newy;
                getValidWords(board, validWord, newvchar, substring, level + 1);
                if (substring.length() > level)
                    substring.delete(level, substring.length());
                visited[newx][newy] = false;
            }
        }
    }

    private boolean validIndex(int x, int y) {
        if (x < 0 || x >= xdimensions || y < 0 || y >= ydimensions)
            return false;
        else
            return true;
    }

    // Returns the score of the given word if it is in the dictionary, zero
    // otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!_dictionary.contains(word)) {
            return 0;
        }
        int length = word.length();
        if (length < 3) {
            return 0;
        } else if (length < 5) {
            return 1;
        } else if (length == 5) {
            return 2;
        } else if (length == 6) {
            return 3;
        } else if (length == 7) {
            return 5;
        } else {
            return 11;
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
