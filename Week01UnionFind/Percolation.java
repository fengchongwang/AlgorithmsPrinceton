import java.lang.IllegalArgumentException;

public class Percolation {
    private final byte[] grid;
    private final int width;
    private final WeightedQuickUnionPathCompression connectionMap;
    private final int topIdx;
    private int numOpenSites;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(final int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        numOpenSites = 0;
        width = n;
        topIdx = n * n;
        grid = new byte[n * n + 1];
        connectionMap = new WeightedQuickUnionPathCompression(n * n + 1);
        for (int i = 0; i < n; i++) {
            connectionMap.union(i, topIdx);
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(final int row, final int col) {
        checkInput(row);
        checkInput(col);
        if (isOpen(row, col)) {
            return;
        }
        final int idxCur = getIndex(row, col);
        grid[idxCur] = getOpenValue(row);      
        if (row - 1 > 0) {
            if (isOpen(row - 1, col)) {
                updateGridValues(grid, idxCur, idxCur - width);
                connectionMap.union(idxCur, idxCur - width);
            }
        }
        if (col - 1 > 0) {
            if (isOpen(row, col - 1)) {
                updateGridValues(grid, idxCur, idxCur - 1);
                connectionMap.union(idxCur, idxCur - 1);
            }
        }
        if (row + 1 <= width) {
            if (isOpen(row + 1, col)) {
                updateGridValues(grid, idxCur, idxCur + width);
                connectionMap.union(idxCur, idxCur + width);
            }
        }
        if (col + 1 <= width) {
            if (isOpen(row, col + 1)) {
                updateGridValues(grid, idxCur, idxCur + 1);
                connectionMap.union(idxCur, idxCur + 1);
            }
        }
        numOpenSites++;
    }

    // is the site (row, col) open?
    public boolean isOpen(final int row, final int col) {
        checkInput(row);
        checkInput(col);
        return (grid[getIndex(row, col)] > 0);
    }

    private byte getOpenValue(final int row) {
        if (row < width) {
            return 1;
        } else {
            return 2;
        }
    }

    private void updateGridValues(byte[] arr, final int idx0, final int idx1) {
        final int idxRoot0 = connectionMap.find(idx0);
        final int idxRoot1 = connectionMap.find(idx1);
        if ((arr[idxRoot0] == 2) || (arr[idxRoot1] == 2)) {
            arr[idxRoot0] = 2;
            arr[idxRoot1] = 2;
        } else {
            arr[idxRoot0] = 1;
            arr[idxRoot1] = 1;
        }
    }

    private void printGrid() {
        System.out.println(grid[grid.length-1]);
        for (int i = 0; i < grid.length-1; i++) {
            if((i+1) % width == 0) {
                System.out.println(grid[i]);
            } else {
                System.out.print(grid[i] + "\t");
            }
        }
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
        return connectionMap.connected(getIndex(row, col), topIdx);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        if (width == 1) { // Corner case
            return isOpen(1, 1);
        }
        return grid[connectionMap.find(topIdx)] == 2;
    }

    // test client (optional)
    public static void main(final String[] args) {
        Percolation p = new Percolation(3);
        p.open(3, 1);
        p.open(1, 3);
        p.open(2, 3);
        p.open(3, 3);        
        System.out.println(p.percolates());
        p.printGrid();
    }
}

class WeightedQuickUnionPathCompression {
    private int[] paths;
    private int nNodes;    // total number of nodes
    private int[] iSize; // number of node of tree i
    private int numTree; // number of subtrees
    public WeightedQuickUnionPathCompression(final int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        nNodes = n;
        numTree = n;
        paths = new int[n];
        iSize = new int[n];
        for (int i = 0; i < n; i++) {
            paths[i] = i;
            iSize[i] = 1;
        }
    }
    public int find(int p) {
        checkInput(p);
        while (paths[p] != p) {
            paths[p] = paths[paths[p]];
            p = paths[p];
        }
        return p;
    }
    public void union(final int p, final int q) {
        checkInput(p);
        checkInput(q);
        final int rootP = find(p);
        final int rootQ = find(q);
        if (rootP == rootQ) {
            return; // Do nothing if p and q have been already connected.
        }
        if (iSize[rootQ] < iSize[rootP]) {
            paths[rootQ] = rootP;
            iSize[rootP] += iSize[rootQ];
        } else {
            paths[rootP] = rootQ;
            iSize[rootQ] += iSize[rootP];
        }
        numTree--;
    }
    public int count() {
        return numTree;
    }
    private void checkInput(final int p) {
        if (p < 0 | p >= nNodes) {
            throw new IllegalArgumentException();
        }
    }
    public boolean connected(final int p, final int q) {
        checkInput(p);
        checkInput(q);
        return (find(p) == find(q));
    }
}
