package it.priori;

public class SegmentSum {
    public static SegmentSum getInstance() {
        return new SegmentSum();
    }

    private SegmentSum() {
    }

    public void sum(int[] array, SumArray sumArray) {
        int sum = 0;

        for (int i = 0; i < array.length; i++) {
            sum += array[i];
        }

        sumArray.addSegmentsSum(sum);
    }

}
