package it.priori;

import java.util.ArrayList;
import java.util.List;

public class Race {
    private final List<Horse> horses = new ArrayList<>();

    public Race(PrimaryController controller) {
        for (int i = 0; i < 5; i++) {
            horses.add(new Horse(i, controller));
        }
    }

    public void start() {
        for (Horse horse : horses) {
            new Thread(horse).start();
        }
    }

    public List<Horse> getHorses() {
        return horses;
    }
}
