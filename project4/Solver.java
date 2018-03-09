import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.Stack;

public class Solver {

    private MinPQ<Node> open = new MinPQ<Node>(new Comparator<Node>() {
        @Override
        public int compare(Node o1, Node o2) {
            if (o1.priority < o2.priority)
                return -1;
            else if (o1.priority > o2.priority)
                return  1;
            else
                return 0;
        }
    });

    int move =-1;
    boolean issolve = false;
    Node currentNode;

    private class Node implements Comparable<Node>{
        int move;
        boolean isTwin;
        Board board;
        Node parent;
        int priority;

        public Node(Board board, Node parent, boolean isTwin, int move) {
            this.board = board;
            this.parent = parent;
            this.isTwin = isTwin;
            this.move = move;
            priority = this.board.manhattan() + this.move;
        }

        public int compareTo(Node that)
        {
            if (this.priority > that.priority) {
                return 1;
            }
            else if (this.priority < that.priority) {
                return -1;
            }
            else
                return 0;
        }
    }


    public Solver(Board initial) {
        if (initial == null)
            throw new NullPointerException();
        currentNode = new Node(initial,null,false,0);
        Node twinNode = new Node(initial.twin(),null,true,0);
       // open.insert(twinNode);
        open.insert(currentNode);


        while (true) {
            Node currentNode = open.delMin();
            if (currentNode.board.isGoal() ) {
                if (!currentNode.isTwin) {
                    issolve = true;
                    break;
                }
                else
                {
                    issolve = false;
                    move = -1;
                    break;
                }
            }
            for (Board neigh : currentNode.board.neighbors()) {
                Node neighNode = new Node(neigh, currentNode, currentNode.isTwin, currentNode.move + 1);
                if (currentNode.parent == null) {
                    open.insert(neighNode);
                }
                else if ( !currentNode.parent.equals(neigh) ) {
                    open.insert(neighNode);
                }

            }

        }
    }           // find a solution to the initial board (using the A* algorithm)

    public boolean isSolvable() {
        return issolve;
    }            // is the initial board solvable?

    public int moves() {
        return move;
    }                     // min number of moves to solve initial board; -1 if unsolvable

    public Iterable<Board> solution() {
        Stack<Board> solution = new Stack<Board>();
        while (currentNode != null) {
            solution.push(currentNode.board);
            currentNode = currentNode.parent;
        }

        return solution;
    }      // sequence of boards in a shortest solution; null if unsolvable

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    } // solve a slider puzzle (given below)

}
