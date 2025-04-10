package it.priori;

import java.util.Random;

public class Horse implements Runnable {
    private int advance = 0;
    private int id;
    private PrimaryController controller;
    private static final int MAX_PROGRESS = 100;
    private final Random r = new Random();

    public Horse(int id, PrimaryController controller) {
        this.id = id;
        this.controller = controller;
    }

    public int getAdvance() {
        return advance;
    }

    public int getId() {
        return id;
    }

    @Override
    public void run() {
        while (advance < MAX_PROGRESS) {
            try {
                Thread.sleep(r.nextInt(300) + 100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            advance += r.nextInt(5); 
            if (advance > MAX_PROGRESS) advance = MAX_PROGRESS;

            controller.updateProgress(id, advance);
        }

        controller.checkWinner(id); // Only one thread will trigger the winner
    }
}
