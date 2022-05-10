package dev.widowan;

import java.math.BigInteger;
import java.util.List;

public class InterpolationSearcher {
    public List<Integer> list;
    private long cmpCnt = 0;

    public InterpolationSearcher(List<Integer> list) {
        this.list = list;
    }

    public Interpolation interpolation = (e, lo, hi) ->
            // Code below is really hard to read, so here's simplified version:
            // However, without it, it WILL overflow, even with longs
            // lo + (
            //     (e - sortList.get(lo))
            //     * (hi - lo)
            // ) / (sortList.get(hi) - sortList.get(lo))
            BigInteger.valueOf(e)
                .subtract(BigInteger.valueOf(list.get(lo)))
                .multiply(BigInteger.valueOf(hi - lo))
                .divide(
                    BigInteger.valueOf(list.get(hi))
                        .subtract(BigInteger.valueOf(list.get(lo)))
                )
                .add(BigInteger.valueOf(lo))
                .intValue();

    public Result search(int e) {
        return new Result(
                rawSearch(e, 0, list.size() - 1),
                cmpCnt);
    }

    private int rawSearch(int e, int from, int to) {
        if (list.get(from).equals(list.get(to)))
            if (list.get(from) == e)
                return from;
            else
                return -1;

        var splitIdx = interpolation.calc(e, from, to);

        if (splitIdx < from || splitIdx > to) {
            cmpCnt += 1;
            return -1;
        }

        if (list.get(splitIdx) < e) {
            cmpCnt += 1;
            return rawSearch(e, Math.min(splitIdx + 1, to), to);
        }
        else if (list.get(splitIdx) > e) {
            cmpCnt += 2;
            return rawSearch(e, from, Math.max(splitIdx - 1, from));
        }
        else {
            cmpCnt += 2;
            return splitIdx;
        }
    }

    public record Result(int index, long cmpCnt) {}

    @FunctionalInterface
    interface Interpolation {
        int calc(int e, int hi, int lo);
    }
}
