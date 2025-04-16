package it.priori.primeNumber;

public class PrimeSearcher implements Runnable {
    private SegmentSearch segmentSearch = SegmentSearch.getInstance();
    private int[] array;
    private FindPrimeNumber findPrimeNumber;

    public PrimeSearcher(int[] a, FindPrimeNumber findPrimeNumber) {
        this.array = a;
        this.findPrimeNumber = findPrimeNumber;
    }

    @Override
    public void run() {
        segmentSearch.searchPrimeNumbers(array, findPrimeNumber);
    }

}
