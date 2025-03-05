import java.util.*;

public class PackageDelivery {
    
    // Helper function for BFS to calculate distances from the start node
    public static int[] bfs(int start, int n, Map<Integer, List<Integer>> adj) {
        int[] dist = new int[n];
        Arrays.fill(dist, -1); // -1 means unvisited
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(start);
        dist[start] = 0;
        
        while (!queue.isEmpty()) {
            int node = queue.poll();
            for (int neighbor : adj.get(node)) {
                if (dist[neighbor] == -1) {
                    dist[neighbor] = dist[node] + 1;
                    queue.offer(neighbor);
                }
            }
        }
        
        return dist;
    }

    // Main function to calculate the minimum roads to collect packages
    public static int minRoadsToCollectPackages(int[] packages, int[][] roads) {
        int n = packages.length;
        
        // Build the graph (adjacency list)
        Map<Integer, List<Integer>> adj = new HashMap<>();
        for (int i = 0; i < n; i++) {
            adj.put(i, new ArrayList<>());
        }
        for (int[] road : roads) {
            adj.get(road[0]).add(road[1]);
            adj.get(road[1]).add(road[0]);
        }
        
        // Identify all package locations
        List<Integer> packageLocations = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (packages[i] == 1) {
                packageLocations.add(i);
            }
        }
        
        if (packageLocations.isEmpty()) {
            return 0; // No packages to collect
        }

        // Now we need to calculate the minimal roads to traverse
        int minTraversal = Integer.MAX_VALUE;
        
        // Try starting from every location
        for (int start = 0; start < n; start++) {
            int[] dist = bfs(start, n, adj);
            int roadsTraversed = 0;
            
            for (int loc : packageLocations) {
                if (dist[loc] == -1) {
                    return -1; // If a package location is unreachable
                }
                roadsTraversed += dist[loc];
            }
            
            minTraversal = Math.min(minTraversal, roadsTraversed);
        }
        
        return minTraversal;
    }

    public static void main(String[] args) {
        int[] packages1 = {1, 0, 0, 0, 0, 1};
        int[][] roads1 = {{0, 1}, {1, 2}, {2, 3}, {3, 4}, {4, 5}};
        System.out.println(minRoadsToCollectPackages(packages1, roads1));  // Output: 2
        
        int[] packages2 = {0, 0, 0, 1, 1, 0, 0, 1};
        int[][] roads2 = {{0, 1}, {0, 2}, {1, 3}, {1, 4}, {2, 5}, {5, 6}, {5, 7}};
        System.out.println(minRoadsToCollectPackages(packages2, roads2));  // Output: 2
    }
}
