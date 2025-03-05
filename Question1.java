public class Question1 {

    public static int findMinMeasurements(int k, int n) {
        // Create a DP table to store the results of subproblems
        int[][] dp = new int[k + 1][n + 1];

        // Base case: If there are no temperature levels, no measurements are needed
        for (int i = 1; i <= k; i++) {
            dp[i][0] = 0;
        }

        // Base case: If there is only one sample, we need to check each temperature level
        for (int j = 1; j <= n; j++) {
            dp[1][j] = j;
        }

        // Fill the DP table
        for (int i = 2; i <= k; i++) {
            for (int j = 1; j <= n; j++) {
                dp[i][j] = Integer.MAX_VALUE;
                for (int x = 1; x <= j; x++) {
                    int res = 1 + Math.max(dp[i - 1][x - 1], dp[i][j - x]);
                    dp[i][j] = Math.min(dp[i][j], res);
                }
            }
        }
        return dp[k][n];
    }

    public static void main(String[] args) {
        // Example 1
        int k1 = 1;
        int n1 = 2;
        System.out.println("Example 1: k = " + k1 + ", n = " + n1);
        System.out.println("Minimum number of measurements required: " + findMinMeasurements(k1, n1));

        // Example 2
        int k2 = 2;
        int n2 = 6;
        System.out.println("\nExample 2: k = " + k2 + ", n = " + n2);
        System.out.println("Minimum number of measurements required: " + findMinMeasurements(k2, n2));

        // Example 3
        int k3 = 3;
        int n3 = 14;
        System.out.println("\nExample 3: k = " + k3 + ", n = " + n3);
        System.out.println("Minimum number of measurements required: " + findMinMeasurements(k3, n3));
    }
}