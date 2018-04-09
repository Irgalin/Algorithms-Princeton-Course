import edu.princeton.cs.algs4.StdRandom;

/**
 * This class allows performing the Monte Carlo simulation.
 *
 * @author Rafael_Irgalin
 */
public class PercolationStats {

    /**
     * Results of each percolation experiment (percolation threshold).
     */
    private final double[] percolationResults;
    /**
     * Sample mean of percolation threshold.
     */
    private double mean;
    /**
     * Sample standard deviation of percolation threshold.
     */
    private double stddev;

    /**
     * Initializes a percolation system with n-by-n grid and perform trials independent experiments on it.
     *
     * @param n      the number of sites in one side to initialize percolation system.
     * @param trials the number of experiments that have to be performed on the created percolation system.
     */
    public PercolationStats(int n, int trials) {
        percolationResults = new double[trials];
        for (int i = 0; i < trials; i++) {
            // Initializing a percolation system with n-by-n grid.
            Percolation percolation = new Percolation(n);
            // Randomly opening sites in grid until the system starts to percolate.
            while (!percolation.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                if (!percolation.isOpen(row, col)) {
                    percolation.open(row, col);
                }
            }
            // Calculating percolation threshold and saving it in the results.
            percolationResults[i] = (double) percolation.numberOfOpenSites() / (n * n);
        }
    }

    /**
     * Calculates and returns sample mean of percolation threshold.
     *
     * @return sample mean of percolation threshold.
     */
    public double mean() {
        double sum = 0;
        for (double result : percolationResults) {
            sum += result;
        }
        mean = sum / percolationResults.length;
        return mean;
    }

    /**
     * Calculates and returns sample standard deviation of percolation threshold.
     *
     * @return sample standard deviation of percolation threshold.
     */
    public double stddev() {
        double sum = 0;
        for (double result : percolationResults) {
            sum = sum + Math.pow(Math.abs(result - mean), 2);
        }
        stddev = Math.pow(sum / (percolationResults.length - 1), 2);
        return stddev;
    }

    /**
     * Calculates and returns low endpoint of 95% confidence interval.
     *
     * @return low endpoint of 95% confidence interval.
     */
    public double confidenceLo() {
        return mean - (1.96 * stddev) / Math.sqrt(percolationResults.length);

    }

    /**
     * Calculates and returns high endpoint of 95% confidence interval.
     *
     * @return high endpoint of 95% confidence interval.
     */
    public double confidenceHi() {
        return mean + (1.96 * stddev) / Math.sqrt(percolationResults.length);
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(n, trials);
        System.out.println("mean                    = " + percolationStats.mean());
        System.out.println("stddev                  = " + percolationStats.stddev());
        System.out.println("95% confidence interval = [" + percolationStats.confidenceLo() + ", "
                + percolationStats.confidenceHi() + "]");
    }


}
