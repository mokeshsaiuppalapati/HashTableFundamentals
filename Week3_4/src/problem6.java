import java.util.*;

public class problem6 {

    static int linearComparisons = 0;
    static int binaryComparisons = 0;

    // -------- LINEAR SEARCH --------
    static int linearSearch(int[] arr, int target) {
        linearComparisons = 0;

        for (int i = 0; i < arr.length; i++) {
            linearComparisons++;
            if (arr[i] == target) return i;
        }
        return -1;
    }

    // -------- BINARY SEARCH INSERTION POINT (LOWER BOUND) --------
    static int lowerBound(int[] arr, int target) {
        int low = 0, high = arr.length;
        binaryComparisons = 0;

        while (low < high) {
            binaryComparisons++;
            int mid = (low + high) / 2;

            if (arr[mid] < target)
                low = mid + 1;
            else
                high = mid;
        }
        return low; // insertion index
    }

    // -------- FLOOR (largest <= target) --------
    static int floor(int[] arr, int target) {
        int low = 0, high = arr.length - 1;
        int result = -1;

        while (low <= high) {
            int mid = (low + high) / 2;

            if (arr[mid] <= target) {
                result = arr[mid];
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return result;
    }

    // -------- CEILING (smallest >= target) --------
    static int ceiling(int[] arr, int target) {
        int low = 0, high = arr.length - 1;
        int result = -1;

        while (low <= high) {
            int mid = (low + high) / 2;

            if (arr[mid] >= target) {
                result = arr[mid];
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return result;
    }

    public static void main(String[] args) {

        int[] risks = {10, 25, 50, 100};

        int target = 30;

        // Linear Search
        int index = linearSearch(risks, target);
        System.out.println("Linear: threshold=" + target +
                " → " + (index == -1 ? "not found" : "found") +
                " (" + linearComparisons + " comparisons)");

        // Binary lower bound (insertion point)
        int insertPos = lowerBound(risks, target);
        System.out.println("Insertion index: " + insertPos +
                " (" + binaryComparisons + " comparisons)");

        // Floor & Ceiling
        int fl = floor(risks, target);
        int ce = ceiling(risks, target);

        System.out.println("Floor(" + target + "): " + fl);
        System.out.println("Ceiling(" + target + "): " + ce);
    }
}