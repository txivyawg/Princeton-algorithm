import edu.princeton.cs.algs4.*;

public class Percolation {
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF uf_backwash;
    private int n;
    private boolean[] ifopen;
    private int count = 0;

    public Percolation(int n)                // create n-by-n grid, with all sites blocked
    {
        if (n<=0) throw new IllegalArgumentException("n is<=0");
        uf= new WeightedQuickUnionUF((n+1)*n+n+1);
        uf_backwash = new WeightedQuickUnionUF(n*n+n+1);
        this.n = n;
        ifopen = new boolean[(n+ 1) * (n) + n + 1];
        for(int i=0*n+1;i<=0*n+n;i++)
        {
            uf.union(0 * n + 1, i);
            uf_backwash.union(0 * n + 1, i);
            ifopen[i]=true;
            uf.union((n+1)*n+1, (n+1)*n+i);
            ifopen[(n+1)*n+i] = true;
        }
    }

    public    void open(int row, int col)    // open site (row, col) if it is not open already
        {
            if (row<1||row>n) throw new IllegalArgumentException("row index out of bounds");
            if (col<1||col>n)  throw new IllegalArgumentException("column index  out of bounds");

            if (! this.isOpen(row, col) )
            {
                count++;
                ifopen[row*n+col] = true;

                if (ifopen[n*(row-1)+col])
                {
                    uf.union(n * (row - 1) + col, n * row + col);
                    uf_backwash.union(n * (row - 1) + col, n * row + col);
                }

                if (ifopen[n*(row+1)+col])
                {
                    uf.union(n * (row + 1) + col, n * row + col);
                    if(row != n)
                        uf_backwash.union(n * (row +1) + col, n * row + col);
                }

                if(col != 1 && ifopen[n*row+col-1] )
                {
                    uf.union(n*row+col-1,n*row+col);
                    uf_backwash.union(n*row+col-1,n*row+col);
                }

                if(col != n && ifopen[n*row+col+1] )
                {
                    uf.union(n*row+col+1,n*row+col);
                    uf_backwash.union(n*row+col+1,n*row+col);
                }
            }
        }

        public boolean isOpen(int row, int col)  // is site (row, col) open?
        {
            if (row<1||row>n) throw new IllegalArgumentException("row index out of bounds");
            if(col<1||col>n)  throw new IllegalArgumentException("column index  out of bounds");
            return ifopen[row*n+col];
        }

        public boolean isFull(int row, int col)  // is site (row, col) full?
        {
            if (row<1||row>n) throw new IllegalArgumentException("row index out of bounds");
            if(col<1||col>n)  throw new IllegalArgumentException("column index  out of bounds");
            return uf_backwash.connected(n*row+col,0*n+1);
        }

        public int numberOfOpenSites()       // number of open sites
        {
            return count;
        }

        public boolean percolates()              // does the system percolate?
        {
            return uf.connected(0*n+1,(n+1)*n+1);
        }

       public static void main(String[] args){  
               int N = StdIn.readInt();  
               Percolation pe=new Percolation(N);  
               pe.open(1, 1);  
               pe.open(2, 1);  
               System.out.println(pe.percolates()); 
       }
    }
