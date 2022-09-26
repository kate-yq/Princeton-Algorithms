import java.util.Iterator;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
    private Node first, last;
    private int count;

    private class Node {
        Item item;
        Node next;
        Node prev;
    }

    // construct an empty deque
    public Deque() {
        this.first = null;
        this.last = null;
        this.count = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return count == 0;
    }

    // return the number of items on the deque
    public int size() {
        return this.count;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (isEmpty()) {
            this.first = new Node();
            this.first.item = item;
            this.last = this.first;
        } else {
            Node oldfirst = this.first;
            this.first = new Node();
            this.first.item = item;
            this.first.next = oldfirst;
            oldfirst.prev = this.first;
        }
        count++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (isEmpty()) {
            this.first = new Node();
            this.first.item = item;
            this.last = this.first;
        } else {
            Node oldlast = this.last;
            this.last = new Node();
            this.last.item = item;
            oldlast.next = this.last;
            this.last.prev = oldlast;
        }
        count++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        Item a = this.first.item;
        this.first = this.first.next;
        count--;
        if (!isEmpty()){
            this.first.prev = null;
        } else {
            this.last = null;
        }
        return a;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        Item b = this.last.item;
        this.last = this.last.prev;
        count--;
        if (!isEmpty()){
            this.last.next = null;
        } else {
            this.first = null;
        }
        return b;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            Item c = current.item;
            current = current.next;
            return c;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> testDeque = new Deque<String>();
        StdOut.println(testDeque.isEmpty());
        testDeque.addFirst("head1");
        testDeque.addLast("end1");
        testDeque.addFirst("head2");
        testDeque.addLast("end2");
        for (String s : testDeque) {
            StdOut.println(s);
        }
        var r = testDeque.removeFirst();
        StdOut.printf("remove %s \n", r);
        for (String s : testDeque) {
            StdOut.println(s);
        }
        r = testDeque.removeLast();
        StdOut.printf("remove %s \n", r);
        for (String s : testDeque) {
            StdOut.println(s);
        }
        r = testDeque.removeLast();
        StdOut.printf("remove %s \n", r);
        for (String s : testDeque) {
            StdOut.println(s);
        }
        r = testDeque.removeLast();
        StdOut.printf("remove %s \n", r);
        for (String s : testDeque) {
            StdOut.println(s);
        }
        StdOut.println(testDeque.size());
    }
}
