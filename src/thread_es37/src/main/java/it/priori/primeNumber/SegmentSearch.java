package it.priori.primeNumber;

import java.util.ArrayList;

public class SegmentSearch {

    public static SegmentSearch getInstance() {
        return new SegmentSearch();
    }

    private SegmentSearch() {
    }

    public void searchPrimeNumbers(int[] a, FindPrimeNumber findPrimeNumber) {
        ArrayList<Integer> primes = new ArrayList<>();

        for (int i = 0; i < a.length; i++) {
            if (isPrime(a[i])) {
                primes.add(a[i]);
            }
        }

        findPrimeNumber.collectPrimes(primes);
    }

    public static boolean isPrime(int n) {
        if (n <= 1)
            return false;

        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }

        return true;
    }
}
