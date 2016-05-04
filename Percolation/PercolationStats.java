import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    
    private double[] arr;
    private int trial;
    
    public PercolationStats(int N, int T) {     // perform T independent experiments on an N-by-N grid
        if(T<1 || N<1){
            throw new java.lang.IllegalArgumentException("invalid size");
        }
        trial = T;
        arr = new double[T];
        for (int i =0;i<T;i++){
            Percolation perc = new Percolation(N);
            double count = 0;
            while(!perc.percolates()){
                int row = StdRandom.uniform(1,N+1);
                int column = StdRandom.uniform(1,N+1);
                if (!perc.isOpen(row,column)){
                    count+=1.0;
                    perc.open(row,column);
                }
            }
            arr[i] = count/(N*N);
            
        }
    }
    
    
    public double mean() {                     // sample mean of percolation threshold
        return StdStats.mean(arr);
    }
    public double stddev() {                   // sample standard deviation of percolation threshold
        return StdStats.stddev(arr);
    }
    public double confidenceLo() {             // low  endpoint of 95% confidence interval
        if(trial==1){
            return StdStats.mean(arr);
        }
        else{
            return mean()-1.96*stddev()/Math.sqrt(trial);
        }
    }
    public double confidenceHi() {             // high endpoint of 95% confidence interval
        if(trial==1){
            return StdStats.mean(arr);
        }
        else{
            return mean()+1.96*stddev()/Math.sqrt(trial);
        }    
    }

    public static void main(String[] args) {   // test client (described below)
        int a = 10;
        int b = 30; //default values for arguements
        if (args.length==2){
            a = Integer.parseInt(args[0]);
            b = Integer.parseInt(args[1]);
        }
        PercolationStats test = new PercolationStats(a,b);
        System.out.println("mean =" + test.mean());
        System.out.println("stddev =" + test.stddev());
        System.out.println("95%% confidence interval =" + test.confidenceLo()+","+ test.confidenceHi());
        
        
    }
}