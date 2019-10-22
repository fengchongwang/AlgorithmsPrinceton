public class Percolation {
    
    private boolean[][] grid;
    private int width;
    private WeightedQuickUnionPathCompression connectionMap;
    private int topIdx;
    private int bottomIdx;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        width = n;
        topIdx = n*n;
        bottomIdx = n*n + 1;
        grid = new boolean[n][n];
        connectionMap = new WeightedQuickUnionPathCompression(n*n + 2);
        for (int i=0; i<n; i++) {
            connectionMap.union(i, topIdx);
        }
        for (int i=n*(n-1); i<n*n; i++) {
            connectionMap.union(i, bottomIdx);
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        grid[row - 1][col - 1] = true;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        return grid[row - 1][col - 1];
    }

    private int getIndex(int row, int col) {
        return (row - 1)*width + col - 1;
    }

    private void checkInput(int row) {
        if (row <= 0 | row > width) throw new IllegalArgumentException();
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        checkInput(row);
        checkInput(col);
        return connectionMap.connected(topIdx, getIndex(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return connectionMap.count();
    }

    // does the system percolate?
    public boolean percolates() {
        return connectionMap.connected(topIdx, bottomIdx);
    }

    // test client (optional)
    public static void main(String[] args) {

    }
}