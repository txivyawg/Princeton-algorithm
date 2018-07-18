import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.BinaryStdIn;
import java.util.LinkedList;

public class MoveToFront {
    private static final int R = 256;
    private static LinkedList<Character> dir;

    private static  void init() {
        dir = new LinkedList<>();
        for (char i = 0; i < R; i++) {
            dir.add((int)i, i);
        }
    }

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        init();
        while(!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            int index = dir.indexOf(c);
            BinaryStdOut.write(index, 8);
            dir.remove(index);
            dir.add(0, c);
        }

        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        init();
        while(!BinaryStdIn.isEmpty()) {
            int index = BinaryStdIn.readInt(8);
            char c = dir.get(index);
            BinaryStdOut.write(c, 8);
            dir.remove(index);
            dir.add(0, c);
        }

        BinaryStdOut.close();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args.length == 0) return;
        if ("-".equals(args[0]))
            encode();
        else if ("+".equals(args[0]))
            decode();
    }
}