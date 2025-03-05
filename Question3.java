import java.util.*;

public class Question3 {
    public static int minCostToConnectDevices(int n, int[] modules, int[][] connections) {
        // Create a list to store all edges (connections)
        List<int[]> edges = new ArrayList<>();

        // Add virtual connections for installing communication modules
        for (int i = 1; i <= n; i++) {
            edges.add(new int[]{0, i, modules[i - 1]});
        }

        // Add the given connections
        for (int[] connection : connections) {
            edges.add(new int[]{connection[0], connection[1], connection[2]});
        }

        // Sort edges by cost (ascending order)
        edges.sort((a, b) -> a[2] - b[2]);

        // Initialize Union-Find data structure
        int[] parent = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            parent[i] = i;
        }

        // Kruskal's Algorithm to find MST
        int totalCost = 0;
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            int cost = edge[2];

            int rootU = find(parent, u);
            int rootV = find(parent, v);

            // If u and v are not in the same set, add this edge to the MST
            if (rootU != rootV) {
                totalCost += cost;
                parent[rootU] = rootV; 
            }
        }

        return totalCost;
    }

    // Helper function for Union-Find (Path Compression)
    private static int find(int[] parent, int x) {
        if (parent[x] != x) {
            parent[x] = find(parent, parent[x]); 
        }
        return parent[x];
    }

    public static void main(String[] args) {
        int n = 3;
        int[] modules = {1, 2, 2};
        int[][] connections = {{1, 2, 1}, {2, 3, 1}};
        System.out.println(minCostToConnectDevices(n, modules, connections)); 
    }
}
