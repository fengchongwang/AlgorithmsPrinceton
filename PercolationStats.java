/******************************************************************************
 *  Name:    Wang
 *  NetID:   wang
 *
 *  Partner Name:    N/A
 *  Partner NetID:   N/A
 *  Partner Precept: N/A
 * 
 *  Description:  Modeling Percolation using an N-by-N grid and Union-Find data 
 *                structures to determine the threshold.
 ******************************************************************************/
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import java.lang.IllegalArgumentException;

public class PercolationStats {
	private double[] res;
	private int numTrials;
	private int width;
	
	/**
	 * Perform trials independent experiments on an n-by-n grid.
	 * 
	 * @param n
	 * @param trials
	 */
	public PercolationStats(int n, int trials) {
		if (trials <= 0 || n <= 0) {
			throw new IllegalArgumentException();
		}
		Percolation pcl;
		numTrials = trials;
		width = n;
		int[] perm;
		int rowIndex;
		int colIndex;
		res = new double[trials];
		for (int i = 0; i < trials; i++) {
			pcl = new Percolation(width);
			perm = StdRandom.permutation(width*width);
			int j;
			for (j = 0; !pcl.percolates(); j++) {
				rowIndex = (perm[j] + 1)/width + 1;
				colIndex = perm[j] + 1 - (rowIndex - 1)*width;
				pcl.open(rowIndex+1, colIndex);
			}
			res[i] = ((double) j)/(width*width);
		}		
	}
	
	/**
	 * Sample mean of percolation threshold.
	 * @return
	 */
	public double mean() {
		return StdStats.mean(this.res);
	}
	
	/**
	 * Sample standard deviation of percolation threshold.
	 * 
	 * @return
	 */
	public double stddev() {
		return StdStats.stddev(this.res);
	}
	
	/**
	 * Low  endpoint of 95% confidence interval.
	 * 
	 * @return
	 */
	public double confidenceLo() {
		return this.mean()-1.96*this.stddev()/Math.sqrt(numTrials);
	}
	
	/**
	 * High endpoint of 95% confidence interval.
	 * 
	 * @return
	 */
	public double confidenceHi() {
		return this.mean()+1.96*this.stddev()/Math.sqrt(numTrials);
	}

    public static void main(String[] args) {
	    PercolationStats ps;
	    ps = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
	    System.out.println("mean = "+Double.toString(ps.mean()));
	    System.out.println("stddev = "+Double.toString(ps.stddev()));
	    System.out.println("95% confidence interval = ["+
	    		Double.toString(ps.confidenceLo())+", "+Double.toString(ps.confidenceHi())+"]");	   
    }
}
