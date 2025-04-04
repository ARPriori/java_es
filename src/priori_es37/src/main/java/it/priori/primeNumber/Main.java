package it.priori.primeNumber;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        int[] array = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };

        int numThreads = 3;
        int segmentLength = array.length / numThreads;
        int remainder = array.length % numThreads;

        ArrayList<Integer> allPrimes = new ArrayList<>();

        int start = 0;
        for (int i = 0; i < numThreads; i++) {
            int end = start + segmentLength + (i < remainder ? 1 : 0);
            int[] segment = new int[end - start];

            for (int j = start; j < end; j++) {
                segment[j - start] = array[j];
            }

            Thread t = new Thread(new PrimeSearcher(segment, (segmentPrimes) -> {
                synchronized (allPrimes) {
                    allPrimes.addAll(segmentPrimes);
                    System.out.println("Numeri primi parziali: " + segmentPrimes.toString());
                }
            }));

            t.start();

            start = end;
        }

        try {
            Thread.sleep(1000); // Tempo per consentire ai thread di completare
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Numeri primi nell'array: " + allPrimes.toString());
    }
}
