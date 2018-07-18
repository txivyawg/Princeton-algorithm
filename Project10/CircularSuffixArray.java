
public class CircularSuffixArray {
    private static final int R = 256;
    private static final int M = 5;
    private int[] index;
    private int[] aux;
    private int n;
    private String str;

    public CircularSuffixArray(String s) {
        if (s == null) throw new NullPointerException();

        this.str = s;
        this.n = s.length();
        this.index= new int[n];
        this.aux = new int[n];
        for (int i = 0; i < n ; i ++) {
            index[i] = i;
            aux[i] = i;
        }
        MSD(0, n, 0);
    }   // circular suffix array of s

    public int length()   {
        return n;
    }                  // length of s

    public int index(int i)  {
        return index[i];
    }               // returns index of ith sorted suffix

    public static void main(String[] args) {
        String test = "ABRACADABRA!";
        CircularSuffixArray csu = new CircularSuffixArray(test);
        //csu.insertSort(0, 11, 0 );
        for(int i = 0; i< csu.length();i++){
            System.out.print(csu.index(i)+"\t");
        }
        System.out.println();
        for (int i=0; i<csu.length();i++){
            System.out.print(test.charAt(csu.index(i)));
        }
    } // unit testing (required)

    public int char2int(int start, int d) {
        return str.charAt((start + d) % n);
    }

    private void exch(int a, int b ) {
        int temp = index[a];
        index[a] = index[b];
        index[b] = temp;
    }

    private boolean less(int a, int b, int d) {
        for (int i = d; i < n; i++) {
            if (char2int(index[a], i) < char2int(index[b], i))
                return true;
            else if (char2int(index[a], i) > char2int(index[b], i))
                return false;
            else
                continue;
        }
        return false;
    }

    private void insertSort(int low, int high, int d) {
        for (int i = low + 1; i <= high ;i++) {
            for (int j = i; j > low && less(j, j - 1, d); j--) {
                exch(j, j - 1);
            }
        }
    }

    private void MSD(int low, int high, int d) {
        if (high <= low + M)
        {
            insertSort(low, high, d);
            return;
        }

        int[] count = new int[R + 1];

        for (int j = low; j < high ; j++)
            count[char2int(index[j], d) + 1]++;
        for (int j = 0; j < R ;j++)
            count[j + 1] += count[j];
        for (int j = low ; j < high ; j++)
            aux[count[char2int(index[j],d)]++] = index[j];
        for (int j = low ; j < high ; j++)
            index[j] = aux[j - low];

        for (int i = 0; i < R ; i++)
            MSD(low + count[i], low + count[i + 1] - 1, d + 1);
    }

}