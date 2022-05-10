package dev.widowan;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class Quicksorter<T extends Comparable<? super T>> {
    private List<T> nums;
    private long swapCnt  = 0;
    private long cmpCnt   = 0;
    private long maxDepth = 0;

    public Quicksorter(List<T> nums) {
        this.nums = nums;
    }

    public SortResults<T> sort() {
        if (nums.size() == 0)
            return new SortResults<>(nums, swapCnt, cmpCnt, maxDepth);
        nums = rawSort(nums, 0, nums.size() - 1);
        return new SortResults<>(nums, swapCnt, cmpCnt, maxDepth);
    }

    private List<T> rawSort(List<T> list, int from, int to) {
        maxDepth += 1;
        if (from >= to) return list;
        var pivotIdx = partition(list, from, to);
        rawSort(list, from, pivotIdx);
        rawSort(list, pivotIdx + 1, to);
        return nums;
    }

    private int partition(List<T> list, int from, int to) {
        var pivot = median3(list, from, to);
        var i = from;
        var j = to;

        while (true) {
            while (list.get(i).compareTo(pivot) < 0) { i++; cmpCnt++; }
            while (list.get(j).compareTo(pivot) > 0) { j--; cmpCnt++; }

            // For false conditions above,
            // comparison still been made
            cmpCnt += 2;

            if (i >= j) return j;
            Collections.swap(list, i++, j--);
            swapCnt += 1;
        }
    }

    private T median3(List<T> list, int from, int to) {
        return Stream.of(
                list.get(from),
                list.get((from+to)/2),
                list.get(to))
            .sorted()
            .toList()
            .get(1);
    }

    public boolean isSorted() {
        boolean sorted = true;
        var last = nums.get(0);
        for (var curr : nums.subList(1, nums.size())) {
            if (curr.compareTo(last) < 0) return false;
            last = curr;
        }
        return sorted;
    }

    public record SortResults<T>(
            List<T> nums,
            long swapCnt,
            long cmpCnt,
            long maxDepth)
    {
        @Override
        public String toString() {
            return String.format("""
                    Массив: %s
                    Кол-во сравнений:      %d
                    Кол-во перестановок:   %d
                    Макс. глубина вызовов: %d""",
                    (nums.size() > 10 ? "опущен" : nums.toString()),
                    cmpCnt, swapCnt, maxDepth);
        }
    }
}
