public class Question2A {
    public static int minRewards(int[] ratings) {
        int n = ratings.length;
        int[] rewards = new int[n];

        // Initialize all employees with at least 1 reward
        for (int i = 0; i < n; i++) {
            rewards[i] = 1;
        }

        // Left-to-Right Pass
        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i - 1]) {
                rewards[i] = rewards[i - 1] + 1;
            }
        }

        // Right-to-Left Pass
        for (int i = n - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                rewards[i] = Math.max(rewards[i], rewards[i + 1] + 1);
            }
        }

        // Calculate total rewards
        int totalRewards = 0;
        for (int reward : rewards) {
            totalRewards += reward;
        }

        return totalRewards;
    }

    public static void main(String[] args) {
        // Example 1
        int[] ratings1 = {1, 0, 2};
        System.out.println("Example 1: " + minRewards(ratings1)); // Output: 5

        // Example 2
        int[] ratings2 = {1, 2, 2};
        System.out.println("Example 2: " + minRewards(ratings2)); // Output: 4
    }
}