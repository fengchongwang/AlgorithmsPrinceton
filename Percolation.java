public class Percolation {
    private final boolean[][] grid;
    private final int width;
    private final WeightedQuickUnionPathCompression connectionMap;
    private final int topIdx;
    private final int bottomIdx;
    private int numOpenSites;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(final int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        numOpenSites = 0;
        width = n;
        topIdx = n * n;
        bottomIdx = n * n + 1;
        grid = new boolean[n][n];
        connectionMap = new WeightedQuickUnionPathCompression(n * n + 2);
        for (int i = 0; i < n; i++) {
            connectionMap.union(i, topIdx);
        }
        for (int i = n * (n - 1); i < n * n; i++) {
            connectionMap.union(i, bottomIdx);
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(final int row, final int col) {
        checkInput(row);
        checkInput(col);
        if (isOpen(row, col)) {
            return;
        }
        grid[row - 1][col - 1] = true;
        final int idxCur = getIndex(row, col);
        if (row - 1 > 0) {
            if (isOpen(row - 1, col)) {
                connectionMap.union(idxCur, idxCur - width);
            }
        }
        if (col - 1 > 0) {
            if (isOpen(row, col - 1)) {
                connectionMap.union(idxCur, idxCur - 1);
            }
        }
        if (row + 1 <= width) {
            if (isOpen(row + 1, col)) {
                connectionMap.union(idxCur, idxCur + width);
            }
        }
        if (col + 1 <= width) {
            if (isOpen(row, col + 1)) {
                connectionMap.union(idxCur, idxCur + 1);
            }
        }
        numOpenSites++;
    }

    // is the site (row, col) open?
    public boolean isOpen(final int row, final int col) {
        checkInput(row);
        checkInput(col);
        return grid[row - 1][col - 1];
    }

    private int getIndex(final int row, final int col) {
        return (row - 1) * width + col - 1;
    }

    private void checkInput(final int row) {
        if (row <= 0 | row > width) {
            throw new IllegalArgumentException();
        }
    }

    // is the site (row, col) full?
    public boolean isFull(final int row, final int col) {
        checkInput(row);
        checkInput(col);
        if (row == 1) { // Corner case
            return true;
        }
        return connectionMap.connected(topIdx, getIndex(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        if (width == 1) { // Corner case
            return isFull(1, 1);
        }
        return connectionMap.connected(topIdx, bottomIdx);
    }

    // test client (optional)
    public static void main(final String[] args) {
        Percolation p = new Percolation(3);
        p.open(1, 3);
        p.open(2, 3);
        p.open(3, 3);
        p.open(3, 1);
        System.out.println(p.isFull(3, 1));
    }
}
