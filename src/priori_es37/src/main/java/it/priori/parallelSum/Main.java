package it.priori.parallelSum;

import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {

        int[] array = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

        int numThreads = 3;
        int segmentLength = array.length / numThreads;
        int remainder = array.length % numThreads;

        AtomicInteger totalSum = new AtomicInteger(0);

        int start = 0;
        for (int i = 0; i < numThreads; i++) {
            int end = start + segmentLength + (i < remainder ? 1 : 0);
            int[] segment = new int[end - start];

            for (int j = start; j < end; j++) {
                segment[j - start] = array[j];
            }

            Thread t = new Thread(new SumThread(segment, (segmentSum) -> {
                totalSum.addAndGet(segmentSum);
                System.out.println("Somma parziale: " + segmentSum);
            }));

            t.start();

            start = end;
        }

        try {
            Thread.sleep(1000); // Tempo per consentire ai thread di completare
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Somma totale dell'array: " + totalSum.get());
    }
}