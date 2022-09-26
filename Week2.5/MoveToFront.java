import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256;

    // apply move-to-front encoding, reading from standard input and writing to
    // standard output
    public static void encode() {
        char[] ASCIItable = new char[R];
        for (int i = 0; i < R; i++) {
            ASCIItable[i] = (char) i;
        }

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            int output = location(c, ASCIItable);
            ASCIItable = move(output, ASCIItable);
            BinaryStdOut.write(output, 8);
        }
        BinaryStdOut.close();
    }

    // find the corresponding index of given char
    // separate as private function to be more readable
    private static int location(char x, char[] table) {
        for (int i = 0; i < table.length; i++) {
            if (x == table[i]) {
                return i;
            }
        }
        throw new IllegalArgumentException("no such element");
    }

    // move the char of give index to the front
    private static char[] move(int index, char[] table) {
        char temp = table[index];
        for (int i = index; i > 0; i--) {
            table[i] = table[i - 1];
        }
        table[0] = temp;
        return table;
    }

    // apply move-to-front decoding, reading from standard input and writing to
    // standard output
    public static void decode() {
        char[] ASCIItable = new char[R];
        for (int i = 0; i < R; i++) {
            ASCIItable[i] = (char) i;
        }

        while (!BinaryStdIn.isEmpty()) {
            int index = BinaryStdIn.readChar();
            char output = ASCIItable[index];
            ASCIItable = move(index, ASCIItable);
            BinaryStdOut.write(output);
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        } else if (args[0].equals("+")) {
            decode();
        } else {
            throw new IllegalArgumentException("invalid instruction");
        }
    }

}
