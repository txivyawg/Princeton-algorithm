import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.Stack;
import java.lang.Math;

public class Board {
        private char[][] blocks;
        private int dimension;

        public Board(int[][] blocks) {
            this.dimension = blocks.length;
            this.blocks = new char[dimension][dimension];

            for (int i = 0;i<dimension;i++) {
                for (int j = 0;j<dimension;j++) {
                    this.blocks[i][j] =(char) blocks[i][j];
                }
            }
        }           // construct a board from an n-by-n array of blocks
        // (where blocks[i][j] = block in row i, column j)

        public int dimension() {
            return dimension;
        }                 // board dimension n

        public int hamming() {
            int num =0;
            for (int i = 0;i<dimension;i++) {
                for (int j =0;j<dimension;j++) {
                    if ( i +j != dimension*2 -1 && blocks[i][j] != i * dimension + j+1 ) {
                        num++;
                    }
                }
            }
            return num;
        }                   // number of blocks out of place

        public int manhattan() {
            int distance = 0;
            for (int i = 0;i<dimension;i++) {
                for (int j = 0;j<dimension;j++) {
                    if (blocks[i][j] != 0) {
                        int col = (blocks[i][j] - 1) % dimension;
                        int row = (blocks[i][j] - col - 1) / dimension;
                        distance += (((col > j) ? (col - j) : (j - col)) +
                                ((row > i) ? (row - i) : (i - row)));
                    }
                }
            }
            return distance;
        }                 // sum of Manhattan distances between blocks and goal

        public boolean isGoal() {
            return this.hamming() ==0;
        }                // is this board the goal board?

        public Board twin() {
            int[][] twinBoard = new int[dimension][dimension];
            for (int i=0; i<dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    twinBoard[i][j] = blocks[i][j];
                }
            }
                if (twinBoard[0][0] != 0 && twinBoard[0][1] != 0){
                    exch(twinBoard,0,0,0,1);
                }else{
                    exch(twinBoard,1,0,1,1);
                }

            return new Board(twinBoard);
        }                    // a board that is obtained by exchanging any pair of blocks

        public boolean equals(Object y) {
            if (this == y)
                return true;
            if (y == null)
                return false;
            if (this.getClass() != y.getClass())
                return false;
            Board that = (Board) y;
            if(this.dimension != that.dimension)
                return false;
            for (int i=0; i<dimension; i++){
                for (int j=0; j<dimension; j++){
                    if (this.blocks[i][j] !=that.blocks[i][j]){
                        return false;
                    }
                }
            }
            return true;
        }        // does this board equal y?

    public Iterable<Board> neighbors() // all neighboring boards
    {
        int blankRow = 0;
        int blankCol = 0;
        Stack<Board> neighbours = new Stack<Board>();

        int[][] clone = new int[dimension][dimension];

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                clone[i][j] = (int) blocks[i][j];

                if (clone[i][j] == 0) {
                    blankRow = i;
                    blankCol = j;
                }
            }
        }

        if (blankCol != 0) {
            exch(clone, blankRow, blankCol - 1, blankRow, blankCol);
            neighbours.push(new Board(clone));
            exch(clone, blankRow, blankCol - 1, blankRow, blankCol);
        }

        if (blankCol != (dimension - 1)) {
            exch(clone, blankRow, blankCol + 1, blankRow, blankCol);
            neighbours.push(new Board(clone));
            exch(clone, blankRow, blankCol + 1, blankRow, blankCol);
        }

        if (blankRow != 0) {
            exch(clone, blankRow - 1, blankCol, blankRow, blankCol);
            neighbours.push(new Board(clone));
            exch(clone, blankRow - 1, blankCol, blankRow, blankCol);
        }

        if (blankRow != (dimension - 1)) {
            exch(clone, blankRow + 1, blankCol, blankRow, blankCol);
            neighbours.push(new Board(clone));
        }

        return neighbours;
    }

    // 这种exch的写法是错误的,因为是传递的是复制的值
//        private void exch(int a, int b) {
//            int temp = a;
//            a= b;
//            b =temp;
//    }

    private void exch(int[][] array, int i, int j, int a, int b) {
        int temp = array[i][j];
        array[i][j] = array[a][b];
        array[a][b] = temp;
    }

    public String toString() // string representation of this board (in the output format specified below)
    {
        StringBuilder s = new StringBuilder();
        s.append(dimension + "\n");
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                s.append(String.format("%2d ", (int) blocks[i][j]));
            }

            s.append("\n");
        }

        return s.toString();
    }        // string representation of this board (in the output format specified below)

        public static void main(String[] args) {
            // read in the board specified in the filename
            In in = new In(args[0]);
            int n = in.readInt();
            int[][] tiles = new int[n][n];

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    tiles[i][j] = in.readInt();
                }
            }

            // solve the slider puzzle
            Board initial = new Board(tiles);
            StdOut.printf("hamming:%d manhattan:%d \n", initial.hamming(),
                    initial.manhattan());
            StdOut.println("dim:" + initial.dimension());
            StdOut.println(initial.toString());
            StdOut.println("goal:" + initial.isGoal());
            StdOut.println("twin:\n" + initial.twin().toString());

            StdOut.println("neighbours:");

            for (Board s : initial.neighbors()) {
                StdOut.println(s.toString());
            }
        }
        } // unit tests (not graded)
