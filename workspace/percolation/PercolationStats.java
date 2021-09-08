/* *****************************************************************************
 *  Name:              Nguyen Van Dung
 *  Coursera User ID:
 *  Last modified:     08/19/2021
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    // perform independent trials on an n-by-n grid
    private static final double CONFIDENCE_95 = 1.96;
    private final double[] fractions;
    private double mean;
    private double stdDeviation;
    private final int trialTime;

    public PercolationStats(int n, int trials) {
        if ((1 > n) || (1 > trials)) throw new IllegalArgumentException(
                "input parameters 'n' & 'trails' have to larger than 0");
        mean = 0;
        stdDeviation = 0;
        trialTime = trials;
        fractions = new double[trials];
        for (int i = 0; i < trials; i++) {
            // Initialize all sites to be blocked
            Percolation perc = new Percolation(n);
            // Choose a site uniformly at random among all blocked sites
            // Open the site.
            while (!perc.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                perc.open(row, col);
            }
            // The fraction of sites that are opened when the system percolates
            // provides an estimate of the percolation threshold.
            fractions[i] = ((double) perc.numberOfOpenSites() / (n * n));
        }

    }

    // sample mean of percolation threshold
    public double mean() {
        // sample mean provides an estimate of the percolation threshold
        mean = StdStats.mean(fractions);
        // mean = Arrays.stream(fractions).sum() / ((double) trialTime);
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        // sample standard deviation s; measures the sharpness of the threshold.
        // double sum = 0;
        // if (mean == 0) mean = mean();
        // for (int i = 0; i < trialTime; i++) {
        //     sum += Math.pow((fractions[i] - mean), 2);
        // }
        // stdDeviation = Math.sqrt(sum / ((double) (trialTime - 1)));
        stdDeviation = StdStats.stddev(fractions);
        return stdDeviation;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        if (mean == 0) mean = mean();
        if (stdDeviation == 0) stdDeviation = stddev();
        return (mean - CONFIDENCE_95 * stdDeviation / Math.sqrt(trialTime));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        if (mean == 0) mean = mean();
        if (stdDeviation == 0) stdDeviation = stddev();
        return (mean + CONFIDENCE_95 * stdDeviation / Math.sqrt(trialTime));
    }

    // test client (see below)
    public static void main(String[] args) {
        if (args.length == 2) {
            int n = Integer.parseInt(args[0]);
            int trial = Integer.parseInt(args[1]);
            PercolationStats percState = new PercolationStats(n, trial);
            StdOut.printf("mean                     = %1$.17f\r\n", percState.mean());
            StdOut.printf("stddev                   = %1$.17f\r\n", percState.stddev());
            StdOut.printf("95 %% confidence interval = [%1$.17f, %2$.17f]\r\n",
                          percState.confidenceLo(),
                          percState.confidenceHi());
        }
    }
}
