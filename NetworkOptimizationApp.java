import java.util.*;

class NetworkOptimizer {

    static class Edge {
        int source, destination, cost, bandwidth;

        Edge(int source, int destination, int cost, int bandwidth) {
            this.source = source;
            this.destination = destination;
            this.cost = cost;
            this.bandwidth = bandwidth;
        }
    }

    static class Graph {
        int nodes;
        List<Edge> edges;

        Graph(int nodes) {
            this.nodes = nodes;
            this.edges = new ArrayList<>();
        }

        void addEdge(int source, int destination, int cost, int bandwidth) {
            edges.add(new Edge(source, destination, cost, bandwidth));
        }
    }

    // Kruskal's algorithm to find Minimum Spanning Tree (MST)
    static List<Edge> kruskalMST(Graph graph) {
        List<Edge> result = new ArrayList<>();
        Collections.sort(graph.edges, (a, b) -> a.cost - b.cost);

        int[] parent = new int[graph.nodes];
        for (int i = 0; i < graph.nodes; i++) {
            parent[i] = i;
        }

        for (Edge edge : graph.edges) {
            int root1 = find(parent, edge.source);
            int root2 = find(parent, edge.destination);

            if (root1 != root2) {
                result.add(edge);
                parent[root1] = root2;
            }
        }

        return result;
    }

    // Dijkstra's algorithm to find the shortest path
    static int dijkstra(Graph graph, int start, int end) {
        int[] distance = new int[graph.nodes];
        Arrays.fill(distance, Integer.MAX_VALUE);
        distance[start] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        pq.add(new int[]{start, 0});

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int node = current[0], dist = current[1];

            if (node == end) break;

            for (Edge edge : graph.edges) {
                if (edge.source == node) {
                    int newDist = dist + edge.bandwidth;
                    if (newDist < distance[edge.destination]) {
                        distance[edge.destination] = newDist;
                        pq.add(new int[]{edge.destination, newDist});
                    }
                }
            }
        }

        return distance[end];
    }

    // Helper function for Kruskal's algorithm
    static int find(int[] parent, int node) {
        if (parent[node] != node) {
            parent[node] = find(parent, parent[node]);
        }
        return parent[node];
    }

    public static void main(String[] args) {
        // Create a graph
        Graph graph = new Graph(4);
        graph.addEdge(0, 1, 10, 100);
        graph.addEdge(0, 2, 20, 200);
        graph.addEdge(1, 2, 15, 150);
        graph.addEdge(1, 3, 25, 250);
        graph.addEdge(2, 3, 30, 300);

        // Find Minimum Spanning Tree (MST)
        List<Edge> mst = kruskalMST(graph);
        System.out.println("Minimum Spanning Tree (MST):");
        for (Edge edge : mst) {
            System.out.println(edge.source + " -> " + edge.destination + " (Cost: " + edge.cost + ")");
        }

        // Calculate shortest path from node 0 to node 3
        int shortestPath = dijkstra(graph, 0, 3);
        System.out.println("\nShortest Path (Bandwidth) from 0 to 3: " + shortestPath);
    }
}