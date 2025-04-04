package it.priori.parallelSum;

public class SumThread implements Runnable {
    private SegmentSum segmentSum = SegmentSum.getInstance();
    private int[] array;
    private SumArray sumArrayCallback;

    public SumThread(int[] a, SumArray sumArrayCallback) {
        this.array = a;
        this.sumArrayCallback = sumArrayCallback;
    }

    @Override
    public void run() {

        segmentSum.sum(array, sumArrayCallback);

    }

}
