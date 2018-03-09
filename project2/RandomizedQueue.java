import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;

import edu.princeton.cs.algs4.StdOut;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int head, tail;
    private Item[] queue;
    private int N;

    public RandomizedQueue() {
        queue = (Item[]) new Object[1];
        head = 0;
        tail = 0;
        N = 0;
    }// construct an empty randomized queue

    public boolean isEmpty() {
        return N == 0;
    }                 // is the randomized queue empty?

    public int size() {
        return N;
    }// return the number of items on the randomized queue

    private void resize(int max) {
        Item[] temp = (Item[]) new Object[max];
        for(int i = 0; i <  N; i++)
            temp[i] = queue[(head + i) % queue.length];
        queue = temp;
        head = 0;
        tail = N;
    }

    public void enqueue(Item item) {
        if(item == null)
            throw new java.lang.IllegalArgumentException();
        if(N ==  queue.length)
            resize(2*queue.length);
        queue[tail++] = item;
        if (tail == queue.length) tail = 0;
        N++;
    }// add the item

    public Item dequeue() {
        if (isEmpty())
            throw new java.util.NoSuchElementException();
        int index = (head + StdRandom.uniform(N)) % queue.length;
        Item item = queue[index];
        if (tail == 0) {
            tail = queue.length - 1;
            queue[index] = queue[tail];
            queue[tail] = null;
        } else {
            queue[index] = queue[--tail];
            queue[tail] = null;
        }
        N--;

        if(N>0 && N==queue.length/4) resize(queue.length/2);
        return item;
    }                    // remove and return a random item

    public Item sample() {
        if (isEmpty())
            throw new java.util.NoSuchElementException();
        int index = (head + StdRandom.uniform(N)) % queue.length;
        return queue[index];
    }// return a random item (but do not remove it)

    public Iterator<Item> iterator() {
        return new RQIterator();
    }         // return an independent iterator over items in random order

    private class RQIterator implements Iterator<Item> {
        private int index = 0;
        private Item[] r;
        public RQIterator() {
            r = (Item[]) new Object[N];
            for(int i=0; i<N; i++)
                r[i] = queue[i];
            StdRandom.shuffle(r);
        }
        public boolean hasNext() {
            return index < N;
        }
        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
        public Item next() {
            if(!hasNext()) throw new java.util.NoSuchElementException();
            Item item = r[index++];
            return item;
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
        rq.enqueue(434);
        rq.dequeue();
        rq.enqueue(398);
        rq.dequeue();
    }
}