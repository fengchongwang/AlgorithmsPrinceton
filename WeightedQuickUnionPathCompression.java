import java.util.ArrayList;

public class WeightedQuickUnionPathCompression {
    private int[] paths;
    private int size;    // total number of nodes
    private int[] iSize;// number of node of tree i
    private int numTree; // number of subtrees
    
    public WeightedQuickUnionPathCompression(int n) {
        if (n < 0) throw new IllegalArgumentException();
        size = n;
        numTree = n;
        paths = new int[n];
        for (int i=0; i<n; i++) {
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
    public void union(int p, int q) {
        checkInput(p);
        checkInput(q);
        int rootP = find(p);
        int rootQ = find(q);
        if (rootP == rootQ) return; // Do nothing if p and q have been already connected.
        if (iSize[q] < iSize[p]) {
            paths[q] = rootP;
            iSize[rootP] += iSize[rootQ];
        }
        else {
            paths[p] = rootQ;
            iSize[rootQ] += iSize[rootP];
        }
        numTree--;
    }
    public int count() {
        return numTree;
    }
    private void checkInput(int p) {
        if (p < 0 | p >= size) throw new IllegalArgumentException();
        return;
    }
    public boolean connected(int p, int q) {
        checkInput(p);
        checkInput(q);
        return (find(p) == find(q));
    }
}