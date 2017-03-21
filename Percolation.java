import edu.princeton.cs.algs4.WeightedQuickUnionUF;
/**
 * 
 */

/**
 * @author wang
 *
 */
public class Percolation {
	private final WeightedQuickUnionUF perc;
	private final boolean[][] grid;
	private final int size;
	private final int virtualTopIndex = 0;
	private final int virtualBottomIndex;
	
	/**	
	 * Create n-by-n grid, with all sites blocked
	 * 
	 * @param n
	 */
	public Percolation(int n) {
		if (n <= 0) {
			throw new IllegalArgumentException(Integer.toString(n));
		}
		size = n;
		virtualBottomIndex = size*size + 1;
		perc = new WeightedQuickUnionUF(virtualBottomIndex + 1); // 0-indexed 1-D array
		grid = new boolean[size][size]; // 1-indexed 2-D array
	}
	
	/**
	 * Judge whether the input row and col indices are in the correct range.
	 * 
	 * @param row index of row for 1-indexed two dimensional array
	 * @param col index of col for 1-indexed two dimensional array
	 */
	private void checkIndexRange(int row, int col) {
		if (row < 1 || col < 1 || row > size || col > size) {
			throw new IndexOutOfBoundsException();
		}
	}
	
	/**
	 * Convert index pair in 1-indexed 2-D array {@code grid} to
	 *  one index in the 0-indexed 1-D array {@code perc}
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	private int toArrayIndex(int row, int col) {
		return this.size*(row-1)+col;
	}
	
	/**
	 * Open a site (row, col) if it is not open already
	 * 
	 * @param row
	 * @param col
	 */
	public void open(int row, int col) {
		checkIndexRange(row, col);
		grid[row-1][col-1] = true;
		int curr = toArrayIndex(row, col);
		// Check upper grid
		if (row >= 2 && grid[row-2][col-1]) {
			perc.union(curr, curr-size);
		}
		else if (row == 1) {
			perc.union(curr, 0);
		}
		// Check lower grid
		if (row <= size-1 && grid[row][col-1]) {
			perc.union(curr, curr+size);
		}
		else if (row == size) {
			perc.union(curr, size*size+1);
		}
		// Check left grid
		if (col >= 2 && grid[row-1][col-2]) {
			perc.union(curr, curr-1);
		}
		// Check right grid
		if (col <= size-1 && grid[row-1][col]) {
			perc.union(curr, curr+1);
		}
	}
	
	/**
	 * Is site (row, col) open?
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean isOpen(int row, int col) {
		checkIndexRange(row, col);
		return grid[row-1][col-1];
	}
	
	/**
	 * Is site (row, col) full?
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean isFull(int row, int col) {
		checkIndexRange(row, col);
		return perc.connected(0, toArrayIndex(row, col));
	}
	
	/**
	 * Return the number of open sites
	 * 
	 * @return
	 */
	public int numberOfOpenSites() {
		int count = 0;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (grid[i][j]) {
					count++;
				}
			}
		}
		return count;
	}
	
	/**
	 * Whether the system percolate
	 * @return
	 */
	public boolean percolates() {
		
		return perc.connected(virtualTopIndex, virtualBottomIndex);
	}
	
//	public static void main(String[] args) {
//		// test client
//		Percolation pc = new Percolation(5);
//		pc.open(1, 3);
//		pc.open(2, 4);
//		pc.open(5, 1);
//		pc.open(4, 4);
//		pc.open(4, 3);
//		pc.open(3, 4);
//		pc.open(4, 1);
//		pc.open(4, 2);
//		pc.open(2, 3);
//		System.out.println(pc.percolates());
//	}
}
