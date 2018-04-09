import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * This class represents model of percolation system.
 *
 * @author Rafael_Irgalin
 */
public class Percolation {

    /**
     * Model of n-by-n grid where if site contains value '0' then it's blocked, if '1' then it's open.
     */
    private int[][] sitesGrid;
    /**
     * Current number of open sites.
     */
    private int numberOfOpenSites;
    /**
     * The sufficient size of union–find data structure to be able to define percolation.
     */
    private final int unionFindDataStrucSize;
    /**
     * An instance of {@link WeightedQuickUnionUF} with an appropriate data structure.
     */
    private final WeightedQuickUnionUF weightedQuickUnionUF;

    /**
     * Creates n-by-n grid, with all sites blocked.
     * Calculates the sufficient size of union-find data structure and initializes an instance of
     * {@link WeightedQuickUnionUF}.
     *
     * @param n number of sites in one side of grid.
     */
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("The argument of constructor must be positive integer.");
        }
        // Initializing of n-by-n grid with all sites blocked.
        sitesGrid = new int[n][n];
        // Calculating the size of union-find data structure.
        // It has to represent all sites in grid plus two additional sites to define percolation.
        unionFindDataStrucSize = (n * n) + 2;
        weightedQuickUnionUF = new WeightedQuickUnionUF(unionFindDataStrucSize);
        // To check if the system percolates or not we have to check connection between every top and bottom site.
        // For reducing time complexity we added two additional sites, we will connect one of them with all top sites
        // and another one with all bottom sites.
        // Thereby we can define percolation by checking connection between these additional sites.
        // For example if we have 3-by-3 grid, then the union-find structure will have the following view:
        //     0
        //  0  0  0
        //  4  5  6
        // 10 10 10
        //    10
        for (int i = 1; i <= n; i++) {
            weightedQuickUnionUF.union(0, i);
            weightedQuickUnionUF.union(unionFindDataStrucSize - 1, unionFindDataStrucSize - 1 - i);
        }
    }

    /**
     * Open a site by its coordinates in grid.
     *
     * @param row an ordinal number of row in grid from 1 to n inclusively.
     * @param col an ordinal number of column in grid from 1 to n inclusively.
     */
    public void open(int row, int col) {
        if (row <= 0 || row > sitesGrid.length || col <= 0 || col > sitesGrid.length) {
            throw new IllegalArgumentException("The passed coordinates are outside of range.");
        }
        int gridRow = row - 1;
        int gridCol = col - 1;

        if (sitesGrid[gridRow][gridCol] == 1) {
            return;
        }

        sitesGrid[gridRow][gridCol] = 1;
        numberOfOpenSites++;
        int siteIndex = calculateSiteIndex(gridRow, gridCol);
        // check all
        if ((gridCol - 1) >= 0 && sitesGrid[gridRow][gridCol - 1] == 1) {
            weightedQuickUnionUF.union(siteIndex, siteIndex - 1);
        }
        if (gridCol + 1 < sitesGrid.length && sitesGrid[gridRow][gridCol + 1] == 1) {
            weightedQuickUnionUF.union(siteIndex, siteIndex + 1);
        }
        if ((gridRow - 1) >= 0 && sitesGrid[gridRow - 1][gridCol] == 1) {
            weightedQuickUnionUF.union(siteIndex, siteIndex - sitesGrid.length);
        }
        if (gridRow + 1 < sitesGrid.length && sitesGrid[gridRow + 1][gridCol] == 1) {
            weightedQuickUnionUF.union(siteIndex, siteIndex + sitesGrid.length);
        }

    }

    /**
     * Checks if a site open or not by its coordinates in grid.
     *
     * @param row an ordinal number of row in grid from 1 to n inclusively.
     * @param col an ordinal number of column in grid from 1 to n inclusively.
     * @return 'true' if site is open, 'false' otherwise.
     */
    public boolean isOpen(int row, int col) {
        if (row <= 0 || row > sitesGrid.length || col <= 0 || col > sitesGrid.length) {
            throw new IllegalArgumentException("The passed coordinates are outside of range.");
        }
        return sitesGrid[row - 1][col - 1] == 1;
    }

    /**
     * Checks if a site full or not by its coordinates in grid.
     * A site is full if it's open and connected with any site in top row.
     *
     * @param row an ordinal number of row in grid from 1 to n inclusively.
     * @param col an ordinal number of column in grid from 1 to n inclusively.
     * @return 'true' if site is full, 'false' otherwise.
     */
    public boolean isFull(int row, int col) {
        if (row <= 0 || row > sitesGrid.length || col <= 0 || col > sitesGrid.length) {
            throw new IllegalArgumentException("The passed coordinates are outside of range.");
        }
        int gridRow = row - 1;
        int gridCol = col - 1;
        return (sitesGrid[gridRow][gridCol] == 1)
                && weightedQuickUnionUF.connected(0, calculateSiteIndex(gridRow, gridCol));
    }

    /**
     * Returns the current number of open sites in grid.
     *
     * @return the current number of open sites in grid.
     */
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    /**
     * Defines whether the system percolates or not.
     *
     * @return 'true' if system percolates, 'false' otherwise.
     */
    public boolean percolates() {
        return weightedQuickUnionUF.connected(0, unionFindDataStrucSize - 1);
    }

    /**
     * Every site in grid corresponds to some site in union-find data structure.
     * This method calculates the index of site in union-find data structure by coordinates in grid.
     *
     * @param gridRow an ordinal row of column in grid from 0 to n-1 inclusively.
     * @param gridCol an ordinal number of column in grid from 0 to n-1 inclusively.
     * @return index of a site in union-find data structure.
     */
    private int calculateSiteIndex(final int gridRow, final int gridCol) {
        return (gridRow * sitesGrid.length + gridCol) + 1;
    }

}
