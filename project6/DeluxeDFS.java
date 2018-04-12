import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;

public class DeluxeDFS {
    private boolean[] marked;
    private int[] edgeTo;
    private int[] disTo;
    private static final int INFINITY = Integer.MAX_VALUE;

    public DeluxeDFS(Digraph G, int s) {
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        disTo = new int[G.V()];
        for (int v = 0;v < G.V();v++) {
            disTo[v] = INFINITY;
        }
        bfs(G,s);
    }
    public DeluxeDFS(Digraph G, Iterable<Integer> sources) {
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        disTo = new int[G.V()];
        for (int v = 0;v < G.V();v++) {
            disTo[v] = INFINITY;
        }
        bfs(G,sources);
    }

    private void bfs(Digraph G, int s) {
        Queue<Integer> queue = new Queue<>();
        marked[s] = true;
        disTo[s] = 0;
        queue.enqueue(s);

        queue = helper(queue, G);
    }

    private void bfs(Digraph G, Iterable<Integer> sources) {
        Queue<Integer> queue = new Queue<>();
        for (int s : sources) {
            marked[s] = true;
            disTo[s] = 0;
            queue.enqueue(s);
        }

        queue = helper(queue, G);
    }

    private Queue<Integer> helper(Queue<Integer> queue, Digraph G) {
        while (!queue.isEmpty()) {
            int v = queue.dequeue();
            for (int w : G.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    disTo[w] = disTo[v] +1;
                    queue.enqueue(w);
                }
            }
        }
        return  queue;
    }

    public boolean hasPathTo(int v) {
        return marked[v];
    }

    public int disTo(int v) {
        return disTo[v];
    }

    public Iterable<Integer> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<Integer> path = new Stack<Integer>();
        int x;
        for (x = v; disTo[x] != 0; x = edgeTo[x])
            path.push(x);
        path.push(x);
        return path;
    }

    public boolean[] getMarked() {
        return this.marked;
    }

}
