import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    private static final int R = 256;

    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray array = new CircularSuffixArray(s);
        int d = s.length() - 1;

        for (int i = 0; i < array.length(); i++){
            if (array.index(i) == 0){
                BinaryStdOut.write(i, 32);
                break;
            }
        }

        for (int i = 0; i < array.length(); i++){
            BinaryStdOut.write(array.char2int(array.index(i),d),8) ;
        }

        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String str = BinaryStdIn.readString();
        int len = str.length();
        char[] t = str.toCharArray();
        int[] next = new int[len];
        int[] count = new int[R + 1];

        for (int i = 0; i < len; i++)
            count[t[i]+1]++;
        for (int i = 0; i < 256; i++)
            count[i+1] += count[i];
        for (int i = 0; i < len; i++)
            next[count[t[i]]++] = i;

        for (int i = 0; i < len; i ++){
            BinaryStdOut.write(t[next[first]]);
            first = next[first];
        }
    }

    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args){
        if (args.length == 0) return;
        if ("-".equals(args[0]))
            transform();
        else if ("+".equals(args[0]))
            inverseTransform();
    }
}