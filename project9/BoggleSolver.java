import java.util.HashSet;
import java.util.Set;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Set;

public class BoggleSolver
{
    private int R = 26;
    private Node root;
    private boolean marked[][];

    private class Node {
        private boolean hasWord = false;
        private Node[] next = new Node[R];
    }

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        if (dictionary == null) throw new IllegalArgumentException();
        for (String str : dictionary) put(str);
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        if (board == null) throw new IllegalArgumentException();

        int width = board.rows();
        int high = board.cols();
        Set<String> bag = new HashSet<>();
        marked = new boolean[width][high];

        for (int i = 0; i < width;i++)
            for (int j = 0; j < high;j++) {
                char c = board.getLetter(i, j);
                dfs(board, i, j, c == 'Q' ? "QU" : "" + c, bag);
            }
        return bag;
    }



    private void dfs(BoggleBoard board, int width, int high, String prefix, Set<String> bag) {
        if (!isValidPrefix(prefix)) return;
        marked[width][high] = true;

        if (get(prefix)) bag.add(prefix);

        for (int row = width -1; row <= width +1;row++)
            for (int col = high - 1;col <= high +1;col++) {
                if (isValidIndex(board, row, col) && !marked[row][col]) {
                    char c = board.getLetter(row, col);
                    dfs(board, row, col, prefix + (c == 'Q' ? "QU" : c), bag);
                }
            }

        marked[width][high] = false;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!get(word)) return 0;
        int score = word.length();
        if (score > 7) return 11;
        if (score == 7) return 5;
        if (score == 6) return 3;
        if (score == 5) return 2;
        if (score > 2) return 1;
        return 0;
    }

    private char charAt(String s, int d) {
        return (char) (s.charAt(d) - 'A');
    }

    private boolean isValidIndex(BoggleBoard board, int i, int j) {
        return i >= 0 && i < board.rows() && j >= 0 && j < board.cols();
    }

    private boolean get(String key) {
        if (key.length() < 3) return false;
        Node x = get(root, key, 0);
        if (x == null) return false;
        else    return x.hasWord;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c =charAt(key,d);
        return get(x.next[c], key, d + 1);
    }

    private void put(String key) {
        root = put(root, key, 0);
    }

    private Node put(Node x, String key,  int d) {
        if (x == null) x = new Node();
        if (d == key.length() ) {
            x.hasWord =true;
            return x;
        }
        char c =charAt(key,d);
        x.next[c] = put(x.next[c], key, d + 1);
        return x;
    }

    private boolean isValidPrefix(String prefix) {
        Node x = root;
        for (int i = 0; i < prefix.length() && x != null; i++)
            x = x.next[charAt(prefix, i)];
        return x != null;
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