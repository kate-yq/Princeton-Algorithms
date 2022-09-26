import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args){
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> client = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            var temp = StdIn.readString();
            client.enqueue(temp);
        }
        for (int i=1; i<=k; i++){
            StdOut.println(client.dequeue());
        }
    }
 }
