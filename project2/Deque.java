import java.util.Iterator;


public class Deque<Item> implements Iterable<Item> {
    private int N;
    private Node first;
    private Node last;

    private class Node {
        private Item node;
        private Node next;
        private Node past;
    }

    public Deque() {
        first = null;
        last = null;
        N = 0;
    }                           // construct an empty deque

    public boolean isEmpty() {
        return N == 0;
    }                 // is the deque empty?

    public int size() {
        return N;
    }                        // return the number of items on the deque

    public void addFirst(Item item) {
        if(item == null) throw new java.lang.IllegalArgumentException();
        Node temp = first;
        first = new Node();
        first.node = item;
        first.next = temp;
        if (isEmpty())
            last = first;
        else temp.past = first;
        N++;
    }          // add the item to the front

    public void addLast(Item item) {
        if(item == null) throw new java.lang.IllegalArgumentException();
        Node temp = last;
        last = new Node();
        last.node = item;
        last.past = temp;
        if (isEmpty())
            first = last;
        else temp.next = last;
        N++;
    }           // add the item to the end

    public Item removeFirst() {
        if(isEmpty())
            throw new java.util.NoSuchElementException("Queue underflow");
        Item temp = first.node;
        first = first.next;
        N--;
        if(isEmpty())
            last=null;
        else first.past = null;
        return temp;
    }                // remove and return the item from the front

    public Item removeLast() {
        if(isEmpty())
            throw new java.util.NoSuchElementException("Queue underflow");
        Item temp = last.node;
        last = last.past;
        N--;
        if(isEmpty())
            first=null;
        else last.next = null;
        return temp;
    }                 // remove and return the item from the end

    public Iterator<Item> iterator() {
        return new ListIterator();
    }         // return an iterator over items in order from front to end

    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if(!hasNext())
                throw new java.util.NoSuchElementException();
            Item item = current.node;
            current = current.next;
            return item;
        }

        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<Integer>();
        deque.addFirst(0);
        deque.addFirst(1);
        deque.addFirst(2);
        deque.removeFirst() ;
        deque.removeFirst() ;
        deque.removeFirst();
    }
}
