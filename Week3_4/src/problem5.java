import java.util.*;

public class problem5 {

    static int linearComparisons = 0;
    static int binaryComparisons = 0;

    // -------- LINEAR SEARCH (FIRST) --------
    static int linearFirst(String[] arr, String target) {
        linearComparisons = 0;

        for (int i = 0; i < arr.length; i++) {
            linearComparisons++;
            if (arr[i].equals(target)) return i;
        }
        return -1;
    }

    // -------- LINEAR SEARCH (LAST) --------
    static int linearLast(String[] arr, String target) {
        int index = -1;

        for (int i = 0; i < arr.length; i++) {
            linearComparisons++;
            if (arr[i].equals(target)) index = i;
        }
        return index;
    }

    // -------- BINARY SEARCH (ANY OCCURRENCE) --------
    static int binarySearch(String[] arr, String target) {
        int low = 0, high = arr.length - 1;
        binaryComparisons = 0;

        while (low <= high) {
            binaryComparisons++;
            int mid = (low + high) / 2;

            if (arr[mid].equals(target)) return mid;

            else if (arr[mid].compareTo(target) < 0)
                low = mid + 1;
            else
                high = mid - 1;
        }
        return -1;
    }

    // -------- COUNT OCCURRENCES (BINARY BASED) --------
    static int countOccurrences(String[] arr, String target) {
        int first = firstOccurrence(arr, target);
        int last = lastOccurrence(arr, target);

        if (first == -1) return 0;
        return last - first + 1;
    }

    static int firstOccurrence(String[] arr, String target) {
        int low = 0, high = arr.length - 1, result = -1;

        while (low <= high) {
            int mid = (low + high) / 2;

            if (arr[mid].equals(target)) {
                result = mid;
                high = mid - 1;
            } else if (arr[mid].compareTo(target) < 0)
                low = mid + 1;
            else
                high = mid - 1;
        }
        return result;
    }

    static int lastOccurrence(String[] arr, String target) {
        int low = 0, high = arr.length - 1, result = -1;

        while (low <= high) {
            int mid = (low + high) / 2;

            if (arr[mid].equals(target)) {
                result = mid;
                low = mid + 1;
            } else if (arr[mid].compareTo(target) < 0)
                low = mid + 1;
            else
                high = mid - 1;
        }
        return result;
    }

    public static void main(String[] args) {

        String[] logs = {"accB", "accA", "accB", "accC"};

        // Linear Search
        int first = linearFirst(logs, "accB");
        int last = linearLast(logs, "accB");

        System.out.println("Linear First accB: index " + first +
                " (" + linearComparisons + " comparisons)");
        System.out.println("Linear Last accB: index " + last);

        // Binary requires sorted array
        String[] sorted = Arrays.copyOf(logs, logs.length);
        Arrays.sort(sorted);

        System.out.println("Sorted logs: " + Arrays.toString(sorted));

        int index = binarySearch(sorted, "accB");
        int count = countOccurrences(sorted, "accB");

        System.out.println("Binary accB: index " + index +
                " (" + binaryComparisons + " comparisons), count=" + count);
    }
}