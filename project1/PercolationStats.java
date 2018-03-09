import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double staT[];
    private double sta_mean;
    private double sta_stddev;
    private int trials ;

    public PercolationStats(int n, int trials)    // perform trials independent experiments on an n-by-n grid
    {
        if(n<=0) throw new IllegalArgumentException();
        if(trials<=0) throw new IllegalArgumentException();
        staT=new double[trials];
        this.trials=trials;
        int times=0;
        
        while(times<trials)
        {

            Percolation pe = new Percolation(n);
            while (!pe.percolates())
            {
                int x = StdRandom.uniform(n) + 1;
                int y = StdRandom.uniform(n) + 1;
                pe.open(x,y);
            }
            staT[times]=(double)pe.numberOfOpenSites()/((double)n*(double)n);
            times++;
        }
        this.sta_mean = StdStats.mean(staT);
        this.sta_stddev = StdStats.stddev(staT);
    }
    public double mean()                          // sample mean of percolation threshold
    {
        return this.sta_mean;
    }
    public double stddev()                        // sample standard deviation of percolation threshold
    {
        return this.sta_stddev;
    }
    public double confidenceLo()                  // low  endpoint of 95% confidence interval
    {
        return this.sta_mean-1.96*this.sta_stddev/Math.sqrt(trials);
    }
    public double confidenceHi()                  // high endpoint of 95% confidence interval
    {
        return this.sta_mean+1.96*this.sta_stddev/Math.sqrt(trials);
    }

    public static void main(String[] args)        // test client (described below)
    {
        
        PercolationStats percolationStats = new PercolationStats(20, 100);
       System.out.println("mean="+ percolationStats.mean());
       System.out.println("stddev="+ percolationStats.stddev());
       System.out.println("95%% confidence Interval="+percolationStats.confidenceLo()+"  "+ percolationStats.confidenceHi());
    }
}