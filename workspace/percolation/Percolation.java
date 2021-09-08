/* *****************************************************************************
 *  Name:              Nguyen Van Dung
 *  Coursera User ID:
 *  Last modified:     08/19/2021
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private enum SiteStatus {
        OPEN(0x00),
        FULL_TOP(0x01),
        FULL_BOT(0x02),
        FULL(0x03),
        BLOCKED(0x04);
        private int value;

        SiteStatus(int value) {
            this.value = value;
        }

        public static SiteStatus fromInteger(int x) {
            switch (x) {
                case 0x00:
                    return OPEN;
                case 0x01:
                    return FULL_TOP;
                case 0x02:
                    return FULL_BOT;
                case 0x03:
                    return FULL;
            }
            return BLOCKED;
        }
    }

    private boolean isPercolate;
    private int gridSize;
    private int openSiteCounter;
    private SiteStatus[] siteArr;
    private WeightedQuickUnionUF uf;

    // creates n-by-n grid, with all sites initially BLOCKED
    public Percolation(int n) {
        if (1 > n) throw new IllegalArgumentException("size of grid has to larger than 0");
        gridSize = n;
        uf = new WeightedQuickUnionUF(gridSize * gridSize);
        siteArr = new SiteStatus[gridSize * gridSize];
        openSiteCounter = 0;
        isPercolate = false;
        for (int i = 0; i < gridSize * gridSize; i++) {
            siteArr[i] = SiteStatus.BLOCKED;
        }
    }

    // get site index from row and col
    private int getSiteIndex(int row, int col) {
        if ((1 > row) || (row > gridSize) || (1 > col) || (col > gridSize)) return -1;
        return (row - 1) * gridSize + col - 1;
    }

    private void unionSite(int index, int nbIndex) {
        // if neighbor is not blocked, then connect to component
        if (siteArr[nbIndex] != SiteStatus.BLOCKED) {
            // if root of current component or neighbor component == FULL_T/B 2*LogN
            // weird language
            SiteStatus newRootStatus = SiteStatus
                    .fromInteger(siteArr[uf.find(index)].value | siteArr[uf
                            .find(nbIndex)].value);

            // connect 2 component. 2*LogN
            uf.union(index, nbIndex);
            // set new merged root status, LogN
            siteArr[uf.find(index)] = newRootStatus;
        }
    }

    private void connectSite(int row, int col) {
        int index = getSiteIndex(row, col);
        // check if next neighbor site up/down/left/right is open
        // if yes then connect them
        int[] changes = { -1, 1 };
        for (int i : changes) {
            // up/down
            int nbIndex = getSiteIndex(row + i, col);
            if (nbIndex == -1) continue;
            unionSite(index, nbIndex);
        }
        for (int i : changes) {
            // left/right
            int nbIndex = getSiteIndex(row, col + i);
            if (nbIndex == -1) continue;
            unionSite(index, nbIndex);
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if ((1 > row) || (row > gridSize))
            throw new IllegalArgumentException(
                    String.format("row has to be in range [1,%d]", gridSize));
        if ((1 > col) || (col > gridSize))
            throw new IllegalArgumentException(
                    String.format("row has to be in range [1,%d]", gridSize));
        // get array indexes
        int index = getSiteIndex(row, col);
        if (siteArr[index] == SiteStatus.BLOCKED) {
            if (row == 1) {
                siteArr[index] = SiteStatus.FULL_TOP;
            }
            else if (row == gridSize) {
                siteArr[index] = SiteStatus.FULL_BOT;
            }
            else {
                siteArr[index] = SiteStatus.OPEN;
            }
            // edge case
            if (1 == gridSize) {
                siteArr[index] = SiteStatus.FULL;
            }

            // edge case
            openSiteCounter++;
            connectSite(row, col);
            // after connect open site with neighbor
            // check root status = FULL > percolate LogN
            if (siteArr[uf.find(index)] == SiteStatus.FULL) {
                isPercolate = true;
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if ((1 > row) || (row > gridSize))
            throw new IllegalArgumentException(
                    String.format("row has to be in range [1,%d]", gridSize));
        if ((1 > col) || (col > gridSize))
            throw new IllegalArgumentException(
                    String.format("row has to be in range [1,%d]", gridSize));
        // get array indexes
        int index = getSiteIndex(row, col);
        return (siteArr[index] != SiteStatus.BLOCKED);
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        // A full site is an open site that can be connected
        // to an open site in the top row via
        // a chain of neighboring (left, right, up, down) open sites
        if ((1 > row) || (row > gridSize))
            throw new IllegalArgumentException(
                    String.format("row has to be in range [1,%d]", gridSize));
        if ((1 > col) || (col > gridSize))
            throw new IllegalArgumentException(
                    String.format("row has to be in range [1,%d]", gridSize));
        // get array indexes - find LogN
        int temp = uf.find(getSiteIndex(row, col));
        return ((siteArr[temp] == SiteStatus.FULL_TOP) || (siteArr[temp] == SiteStatus.FULL));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSiteCounter;
    }

    // does the system percolate?
    public boolean percolates() {
        // system percolates if there is a full site in the bottom row
        return isPercolate;
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation p = new Percolation(1);
        p.open(1, 1);
        System.out.println(p.percolates());
    }
}
