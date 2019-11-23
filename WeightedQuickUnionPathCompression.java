public class WeightedQuickUnionPathCompression {
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
        if (iSize[rootP] < iSize[rootQ]) {
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
