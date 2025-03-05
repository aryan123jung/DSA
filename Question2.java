import java.util.*;

public class Question2 {
    public static int kthSmallestCombinedReturn(int[] returns1, int[] returns2, int k) {
        // Create a list to store all possible combined returns
        List<Integer> combinedReturns = new ArrayList<>();

        // Calculate all possible combined returns
        for (int i = 0; i < returns1.length; i++) {
            for (int j = 0; j < returns2.length; j++) {
                combinedReturns.add(returns1[i] * returns2[j]);
            }
        }

        Collections.sort(combinedReturns);

        System.out.println("All combined returns in ascending order: " + combinedReturns);

        return combinedReturns.get(k - 1);
    }

    public static void main(String[] args) {
        // Example 2
        int[] returns1 = {-4, -2, 0, 3};
        int[] returns2 = {2, 4};
        int k = 6;
        System.out.println("6th smallest combined return: " + kthSmallestCombinedReturn(returns1, 
        returns2, k));
    }
}