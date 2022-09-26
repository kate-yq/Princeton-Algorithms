import java.util.Iterator;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] array;
    private int count, head, tail;

    // construct an empty randomized queue
    public RandomizedQueue() {
        this.array = (Item[]) new Object[2];
        this.count = 0;
        this.head = 0;
        this.tail = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return count == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return count;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        this.array[tail] = item;
        tail++;
        count++;
        if (tail == array.length) {
            Item[] copy = (Item[]) new Object[tail * 2];
            for (int i = 0; i < tail; i++) {
                copy[i] = array[i];
            }
            this.array = copy;
        }
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        int i = StdRandom.uniform(this.head, this.tail);
        Item a = array[i];
        if (i < head + (tail - head) / 2) {
            for (int j = i; j > head; j--) {
                array[j] = array[j - 1];
            }
            array[head] = null;
            head++;
            count--;
        } else {
            for (int j = i; j < tail - 1; j++) {
                array[j] = array[j + 1];
            }
            array[tail - 1] = null;
            tail--;
            count--;
        }
        if (count < array.length / 4) {
            Item[] copy = (Item[]) new Object[array.length / 2];
            for (int k = 0; k < array.length / 2; k++) {
                copy[k] = array[k];
            }
            this.array = copy;
            head = 0;
            tail = count;
        }
        return a;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        int i = StdRandom.uniform(this.head, this.tail);
        return array[i];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RQIterator();
    }

    private class RQIterator implements Iterator<Item> {
        private int num = 0;
        private int current = 0;
        private int tempHead = head;
        private int tempTail = tail;
        private Item[] temp = array;

        public boolean hasNext() {
            return this.num != count;
        }

        public Item next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            this.current = StdRandom.uniform(tempHead, tempTail);
            Item b = temp[current];
            temp[current] = temp[tempHead];
            temp[tempHead] = b;
            tempHead++;
            num++;
            return b;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args){
        RandomizedQueue<String> testRQ = new RandomizedQueue<String>();
        StdOut.println(testRQ.isEmpty());
        testRQ.enqueue("a");
        testRQ.enqueue("b");
        testRQ.enqueue("c");
        testRQ.enqueue("d");
        testRQ.enqueue("e");
        StdOut.print("Iterator 1: ");
        for (String s : testRQ) {
            StdOut.print(s+"  ");
        }
        StdOut.print("\n");
        StdOut.printf("sample %s \n", testRQ.sample());
        StdOut.printf("size %d \n", testRQ.size());
        StdOut.printf("remove %s \n", testRQ.dequeue());
        StdOut.printf("sample %s \n", testRQ.sample());
        StdOut.printf("size %d \n", testRQ.size());
        StdOut.printf("remove %s \n", testRQ.dequeue());
        StdOut.print("Iterator 2: ");
        Iterator<String> i = testRQ.iterator();
        while(i.hasNext()){
            String s = i.next();
            StdOut.print(s+"  ");
        }
        StdOut.print("\n");
    }
}