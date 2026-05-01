package util;

import java.util.ArrayList;
import java.util.List;

/**
 * SortingAlgorithms — provides Bubble Sort, Merge Sort, and Insertion Sort.
 * All methods work on ArrayList<T extends Comparable<T>>.
 *
 * Big-O Summary:
 *  - Bubble Sort:    O(n²)  average & worst
 *  - Merge Sort:     O(n log n)  average & worst
 *  - Insertion Sort: O(n²)  average & worst  /  O(n) best (nearly sorted)
 */
public class SortingAlgorithms {

    // ─────────────────────────────────────────────
    // 1. BUBBLE SORT  —  O(n²)
    // ─────────────────────────────────────────────
    public static <T extends Comparable<T>> void bubbleSort(ArrayList<T> list) {
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (list.get(j).compareTo(list.get(j + 1)) > 0) {
                    T temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
    }

    // ─────────────────────────────────────────────
    // 2. MERGE SORT  —  O(n log n)
    // ─────────────────────────────────────────────
    public static <T extends Comparable<T>> void mergeSort(ArrayList<T> list, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(list, left, mid);
            mergeSort(list, mid + 1, right);
            merge(list, left, mid, right);
        }
    }

    private static <T extends Comparable<T>> void merge(ArrayList<T> list, int left, int mid, int right) {
        List<T> leftList  = new ArrayList<>(list.subList(left, mid + 1));
        List<T> rightList = new ArrayList<>(list.subList(mid + 1, right + 1));

        int i = 0, j = 0, k = left;
        while (i < leftList.size() && j < rightList.size()) {
            if (leftList.get(i).compareTo(rightList.get(j)) <= 0) {
                list.set(k++, leftList.get(i++));
            } else {
                list.set(k++, rightList.get(j++));
            }
        }
        while (i < leftList.size())  list.set(k++, leftList.get(i++));
        while (j < rightList.size()) list.set(k++, rightList.get(j++));
    }

    // ─────────────────────────────────────────────
    // 3. INSERTION SORT  —  O(n²) / O(n) best
    // ─────────────────────────────────────────────
    public static <T extends Comparable<T>> void insertionSort(ArrayList<T> list) {
        int n = list.size();
        for (int i = 1; i < n; i++) {
            T key = list.get(i);
            int j = i - 1;
            while (j >= 0 && list.get(j).compareTo(key) > 0) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }
    }

    // ─────────────────────────────────────────────
    // BINARY SEARCH  —  O(log n)  [list must be sorted first]
    // ─────────────────────────────────────────────
    public static <T extends Comparable<T>> int binarySearch(ArrayList<T> list, T target) {
        int low = 0, high = list.size() - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            int cmp = list.get(mid).compareTo(target);
            if (cmp == 0) return mid;
            else if (cmp < 0) low  = mid + 1;
            else              high = mid - 1;
        }
        return -1;
    }
}
